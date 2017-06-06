(function() {
    'use strict';

    /**
     * @ngdoc directive
     * @name eureka.directive:idleTimeout
     * @element *
     * @function
     * @description
     * idleTimeout directive
     */
    angular
        .module('eureka')
        .directive('idleTimeout', idleTimeout);

    function idleTimeout() {
        return {
			restrict: 'AE',
			scope: false,
            bindToController: true,
            replace: false,
            controller: 'IdleTimeoutCtrl',
            controllerAs: 'vm',
			link: function(scope, element, attrs, ctrl) {
                ctrl.getSessionProperties().then(function(sessionProperties) {
                    let maxInactiveInterval = sessionProperties.maxInactiveInterval;
					$(element).idleTimeout({
						idleTimeLimit: maxInactiveInterval - 30, //Time out with 30 seconds to spare to make sure the server session doesn't expire first
						redirectUrl: 'logout',
						alertDisplayLimit: 60, // Display 60 seconds before send of session.
						sessionKeepAliveTimer: maxInactiveInterval - 15 //Send a keep alive signal with 15 seconds to spare.
					  });
                      console.log('Idle timeout started with inactive interval of ' + maxInactiveInterval + ' seconds.');
				}, function(msg) {
                    scope.$root.idleTimeoutErrorMsg = msg;
                });
            }
		};
    }

}());