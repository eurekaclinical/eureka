/**
 * Configurable user inactivity timer and logout redirect. Works across 
 * multiple windows and tabs from the same domain. Depends on bootsrap bootbar
 * for displaying a warning alert.
 * 
 * Based upon Jquery-idleTimeout by JillElaine, which is available at
 * http://jillelaine.github.io/jquery-idleTimeout/.
 **/

(function ($) {

	$.fn.idleTimeout = function (options) {

		//##############################
		//## Configuration Variables
		//##############################
		var defaults = {
			idleTimeLimit: 1200, // 'No activity' before auto logout time limit in seconds. 1200 = 20 Minutes
			redirectUrl: '/logout', // redirect to this url on timeout logout. Set to "redirectUrl: false" to disable redirect

			// optional custom callback to perform before logout
			customCallback: false, // set to false for no customCallback
			// customCallback:    function () {    // define optional custom js function
			// perform custom action before logout
			// },

			// configure which activity events to detect
			// http://www.quirksmode.org/dom/events/
			// https://developer.mozilla.org/en-US/docs/Web/Reference/Events
			activityEvents: 'click keypress scroll wheel mousewheel mousemove', // separate each event with a space

			// Warning alert configuration
			enableAlert: true, // Set to false for logout without warning alert
			alertDisplayLimit: 180, // Time to display the warning alert before logout (and optional callback) in seconds. 180 = 3 Minutes
			alertTitle: 'Session Expiration Warning',
			// The alert display message. Must have a span with id countdownDisplay, into which the time remaining will be inserted.
			alertMessage: "You will be logged off in <span id=\"countdownDisplay\"><!-- countdown place holder --></span> due to inactivity. Click anywhere to continue using Eureka!",
			// server-side session keep-alive timer
			sessionKeepAliveTimer: 600 // Ping the server at this interval in seconds. 600 = 10 Minutes
			// sessionKeepAliveTimer: false // Set to false to disable pings
			
			
		},
		//##############################
		//## Private Variables
		//##############################
		opts = $.extend(defaults, options),
				checkHeartbeat = 2, // frequency to check for timeouts in seconds
				localObjectStorage, 
				idleTimer,
				logoutManager, 
				warningAlert,

		//##############################
		//## Private Functions
		//##############################

		// Appropriately translates to/from strings for localStorage.
		localObjectStorage = {
			setItem: function (key, value) {
				localStorage.setItem(key, JSON.stringify(value));
			},
			
			getItem: function (key) {
				return JSON.parse(localStorage.getItem(key));
			}
		},

		idleTimer = (function() {
			var idleTimerIval = null;
	
			checkTimeout = function () {
				var timeNow = $.now(), 
						timeIdleTimeout = activity.getLast() + (opts.idleTimeLimit * 1000),
						timeToShowAlert = timeIdleTimeout - (opts.alertDisplayLimit * 1000),
						warningAlertOpen = warningAlert.isOpen();

				if (timeNow > timeIdleTimeout || logoutManager.isLoggedOut()) {
					logoutManager.logoutUser();
				} else {
					if (opts.enableAlert) {
						if (timeNow > timeToShowAlert) {
							if (!warningAlertOpen) {
								warningAlert.open();
							}
						} else {
							if (warningAlertOpen) {
								warningAlert.close();
							}
						}
					}
				}
			};
			
			return {
				init: function() {
					logoutManager.init();
					idleTimerIval = setInterval(checkTimeout, checkHeartbeat * 1000);
					activity.init();
				}
			};
		})(),
				
		activity = (function() {
			var recentActivity = null, 
					recentActivityIval = null,
					keepSessionIval = null,
					sessionKeepAliveUrl = window.location.href,// set URL to ping to user's current window
					idleTimerLastActivity = null; 
			
			var setLast = function(lastActivity) {
				idleTimerLastActivity = lastActivity;
				localObjectStorage.setItem('idleTimerLastActivity', lastActivity);
			},
			
			getLast = function() {
				return localObjectStorage.getItem('idleTimerLastActivity');
			},
					
			keepSessionAlive = function() {
				// A heuristic to avoid multiple windows invoking the keep alive URL.
				if (idleTimerLastActivity === getLast()) {
					$.get(sessionKeepAliveUrl);
				}
			},
					
			createKeepSessionIval = function() {
				keepSessionIval = setInterval(keepSessionAlive, (opts.sessionKeepAliveTimer * 1000));
			},
					
			check = function() {
				if (recentActivity) {
					setLast(recentActivity);
					recentActivity = null;
				}
			};
			
			return {
				init: function() {
					$('body').on(opts.activityEvents, function () {
						recentActivity = $.now();
					});
					setLast($.now());
					recentActivityIval = setInterval(check, 1000);
					if (opts.sessionKeepAliveTimer) {
						createKeepSessionIval();
					}
				},

				restart: function() {
					clearInterval(keepSessionIval);
					keepSessionAlive();
					createKeepSessionIval();
				},

				getLast: getLast
			};
		})(),

		warningAlert = (function() {
			var warningAlertHtml = null,
					remainingTimer = null,
					origTitle = document.title; // save original browser title

			startCountdownDisplay = function () { // display remaining time on warning dialog
				var countdownDisplay = $('#countdownDisplay');
				
				if (countdownDisplay) {
					var timeDialogTimeout = activity.getLast() + (opts.idleTimeLimit * 1000);
					
					var updateCountdown = function () {
						var dialogDisplaySeconds = Math.max((timeDialogTimeout - $.now()) / 1000, 0);
						var mins = Math.floor(dialogDisplaySeconds / 60); // minutes
						if (mins < 10) {
							mins = '0' + mins;
						}
						var secs = Math.floor(dialogDisplaySeconds - (mins * 60)); // seconds
						if (secs < 10) {
							secs = '0' + secs;
						}
						countdownDisplay.html(mins + ':' + secs);
					};
					
					updateCountdown();
					remainingTimer = setInterval(updateCountdown, 1000);
				}
			};
			
			return {
				open: function () {
					warningAlertHtml = $.bootbar.warning(opts.alertMessage, {
						autoDismiss: false,
						onDismiss: function () {
							clearInterval(remainingTimer);
						}
					});
					startCountdownDisplay();
					document.title = opts.alertTitle;
				},
				
				isOpen: function() {
					if (warningAlertHtml) {
						return true;
					} else {
						return false;
					}
				},

				close: function () {
					warningAlertHtml.close();
					warningAlertHtml = null;
					document.title = origTitle;
				}
			};
		})(),
				
		logoutManager = {
			init: function () {
				localObjectStorage.setItem('idleTimerLoggedOut', false);

				//If exists, hook the logout button or link into idletimeout.
				$(".idletimeout-logout").click(function () {
					localObjectStorage.setItem('idleTimerLoggedOut', true);
				});
			},
			
			isLoggedOut: function() {
				return localObjectStorage.getItem('idleTimerLoggedOut');
			},
					
			logoutUser: function () {
				localObjectStorage.setItem('idleTimerLoggedOut', true);

				if (opts.customCallback) {
					opts.customCallback();
				}

				if (opts.redirectUrl) {
					window.location.href = opts.redirectUrl;
				}
			}
		};

		//###############################
		// Build & Return the instance of the item as a plugin
		//###############################
		return this.each(function () {
			idleTimer.init();
		});
	};
}(jQuery));
