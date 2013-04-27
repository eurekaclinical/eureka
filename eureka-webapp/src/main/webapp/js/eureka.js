/* Eureka WebApp. Copyright (C) 2012 Emory University. Licensed under http://www.apache.org/licenses/LICENSE-2.0. */

$(document).ready(function() {

	/*
     $('#tree').hover(
        function() { $.data(this, 'hover', true); },
        function() { $.data(this, 'hover', false); }
    ).data('hover', false);
    $('#tooltip').hover(
        function() { $.data(this, 'hover', true); },
        function() { $.data(this, 'hover', false); }
    ).data('hover', false);

    $('div.tooltip').mouseout( function() {
        if ($('#tree').data('hover') == false && $('#tooltip').data('hover') == false)
            $('div.tooltip').hide();
    }); 
    */
	
	var fileUploadErrors = {
		maxFileSize: 'File is too big',
		minFileSize: 'File is too small',
		acceptFileTypes: 'Filetype not allowed',
		maxNumberOfFiles: 'Max number of files exceeded',
		uploadedBytes: 'Uploaded bytes exceed file size',
		emptyResult: 'Empty file upload result'
	};
	
	$.validator.addMethod("passwordCheck",
		function(value, element) {
			return value.length>= 8 && /[0-9]+/.test(value) && /[a-zA-Z]+/.test(value);
		},
		"Please enter at least 8 characters with at least 1 digit."
		);
	
	if ($("#editUserForm").length > 0){
		var editUserFormValidator = $("#editUserForm").validate({
			rules: {
				role: "required"
			},
			messages: {
				role: "Select a Role"
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
				
	}
	
	if ($("#signupForm").length > 0){


		var signupFormValidator = $("#signupForm").validate({
			rules: {
				firstName: "required",
				lastName: "required",
				password: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				organization: {
					required: true
				},
				verifyPassword: {
					required: true,
					minlength: 8,
					equalTo: "#password"
				},
				email: {
					required: true,
					email: true,
				},
				verifyEmail: {
					required: true,
					email: true,
					equalTo: "#email"
				},
				agreement: {
					required: true
				}
			},
			messages: {
				firstName: "Enter your firstname",
				lastName: "Enter your lastname",
				organization: "Enter your organization",
				password: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				},
				email: {
					required: "Please enter a valid email address",
					minlength: "Please enter a valid email address",
				},
				verifyEmail: {
					required: "Please enter a valid email address",
					minlength: "Please enter a valid email address",
					equalTo: "Enter the same email as above"					
				},
				agreement: {
					required: "Please check the agreement checkbox"
				}
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		
		
	}

	
	$('#newPasswordTable').hide();
	$('#saveAcctBtn').hide();
	$('#registrationComplete').hide();
	$('#passwordChangeComplete').hide();
	
	$('#editAcctBtn').click(function(){
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
	});

	$('#ChangePasswordbtn').click(function(e){
		e.preventDefault();
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
	});
	
	$('#ChangePasswordbtn').click(function(){
		$('#newPasswordTable').show();
		$('#editAcctBtn').hide();
		$('#saveAcctBtn').show();
		return false;
	});
	
	$("#signupForm").submit(function() {
		if (signupFormValidator.valid()) {
			var firstName = $('#firstName').val();
			var lastName  = $('#lastName').val(); 
			var organization = $('#organization').val();
			var password = $('#password').val();
			var verifyPassword = $('#verifyPassword').val();
			var  email = $('#email').val();
			var verifyEmail = $('#verifyEmail').val();
	
			var dataString = 'firstName='+ firstName+ '&lastName=' + lastName + '&organization=' + organization + 		
			'&password=' + password+ '&verifyPassword=' + verifyPassword + 
			'&email=' + email + '&verifyEmail=' + verifyEmail;		
			$.ajax({
				type: 'POST',
				url: 'register',
				data: dataString,
				success: function() {
					$('#passwordChangeFailure').hide();
					$('#signupForm').hide();
					$('#registerHeading').hide();
					$('#registrationComplete').show();

				}, 
				error: function(xhr, err) {

					$('#passwordChangeFailure').show();
					$('#passwordErrorMessage').text(xhr.responseText)

				} 

			});
		}

		return false;
		

	});
	 
	 
	if ($("#userAcctForm").length > 0){
		var userFormValidator = $("#userAcctForm").validate({
			rules: {
				oldPassword: {
					required: true
				},
				newPassword: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				verifyPassword: {
					required: true,
					minlength: 8,
					equalTo: "#newPassword"
				}
			},
			messages: {
				oldPassword: "Please enter your old password",
				newPassword: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				}
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		 
	}
	 
	$("#ResetPsdForm").submit(function(){
		var email = $('#email').val();
		var dataString = 'email='+ email;
		$.ajax({
			type: 'POST',
			url: 'forgot_password',
			data: dataString,
			success: function() {
				$('#passwordresetComplete').show();
				$('#passwordresetComplete').text("Password has been reset.You will receive an email with the new password.");
				$('#saveAcctBtn').hide();

			}, 
			error: function(xhr, status, error) {

				$('#passwordresetComplete').show();
				$('#passwordresetComplete').text(xhr.responseText);

			} 
		});
		return false;
	});
	 
	if ($("#passwordExpirationfrm").length > 0){
		var pwExpFormValidator = $("#passwordExpirationfrm").validate({
			rules: {
				oldExpPassword: {
					required: true
				},
				newExpPassword: {
					required: true,
					minlength: 8,
					passwordCheck: true
				},
				verifyExpPassword: {
					required: true,
					minlength: 8,
					equalTo: "#newExpPassword"
				}
			},
			messages: {
				oldExpPassword: "Please enter your old password",
				newExpPassword: {
					required: "Provide a password",
					rangelength: jQuery.format("Please enter at least {0} characters")
				},
				verifyExpPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				}
			},
			// the errorPlacement has to take the table layout into account
			errorPlacement: function(error, element) {
				if ( element.is(":radio") )
					error.appendTo( element.parent().next().next() );
				else if ( element.is(":checkbox") )
					error.appendTo ( element.next() );
				else
					error.appendTo( element.parent().next() );
			},
			// specifying a submitHandler prevents the default submit, good for the demo
			//submitHandler: function() {
			//	alert("submitted!");
			//},
			// set this class to error-labels to indicate valid fields
			success: function(label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
		 
	}
	 
	$("#passwordExpirationfrm").submit(function(){
		var oldPassword = $('#oldExpPassword').val();
		var newPassword = $('#newExpPassword').val();
		var verifyPassword = $('#verifyExpPassword').val();
		var userId = '';
		var targetURL=$('#targetURL').val();
			
		var dataString = 'action=save' +
		'&id='+ userId +
		'&oldPassword=' + oldPassword + 
		'&newPassword=' + newPassword + 
		'&verifyPassword=' + verifyPassword;  
	        	
		if (pwExpFormValidator.valid()) {   
			$.ajax({
				type: 'POST',
				url: 'user_acct',
				data: dataString,
				success: function() {
					window.location.href = targetURL;

				}, 
				error: function(xhr, status, error) {

					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text(xhr.responseText);
					$('#passwordChangeComplete').css({
						'font-weight' : 'bold',
						'font-size': 16
					});

				} 
			});
	        
		}
		return false;
	});
	 
	 
	$("#userAcctForm").submit(function(){
		var oldPassword = $('#oldPassword').val();
		var newPassword = $('#newPassword').val();
		var verifyPassword = $('#verifyPassword').val();
		var userId = $('#id').val();
			
		var dataString = 'action=save' +
		'&id='+ userId +
		'&oldPassword=' + oldPassword + 
		'&newPassword=' + newPassword + 
		'&verifyPassword=' + verifyPassword;  
	        
		if (userFormValidator.valid()) {                   		
			$.ajax({
				type: 'POST',
				url: 'user_acct',
				data: dataString,
				success: function() {
					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text("Password has been changed.");
					$('#newPasswordTable').hide(); 
					$('#passwordExpirationMsg').hide();

				}, 
				error: function(xhr, status, error) {

					$('#passwordChangeComplete').show();
					$('#passwordChangeComplete').text(xhr.responseText);

				} 
			});
		}

		return false;
	});
	 
	if ($("#jobUpload").length > 0){
		// Helper functions
		function updateSubmitButtonStatus() {
			var doDisable = false;
			if ($('form#uploadForm').data('job-running')) {
				doDisable = true;
			} else {
				var missingRequired = false;
				$(".browseButton").each(function() {
					if (!$(this).disabled && $(this).data('required') && !$(this).val()) {
						missingRequired = true;
					}
				});
				if (!missingRequired) {
					doDisable = false;
				} else {
					doDisable = true;
				}
			}
			$('input:submit').prop('disabled', doDisable);
		}
		
		function updateInputFields(sourceId) {
			$(".uploads").each(function(i, r) {
				if ($(r).attr('id') === 'uploads' + sourceId) {
					$(r).find("input[type='file']").prop('disabled', false);
					$(r).show();
				} else {
					$(r).find("input[type='file']").prop('disabled', true);
					$(r).hide();
				}
			});
			updateSubmitButtonStatus();
		}
		
		function onFinish() {
			$('#uploadForm').prop('disabled', false);
			$('#jobUpload').hide();
			$('form#uploadForm').data('job-running', false)
			updateSubmitButtonStatus();
		}
		
		function setInitialStatus() {
			var running = false;
			if ($('form#uploadForm').data('job-running')) {
				running = true;
			} else {
				$('#jobUpload').hide();
			}
			var sourceId = $("form#uploadForm").find('select[name="source"]').val();
			updateInputFields(sourceId);
			return running;
		}
		
		// Initialize widgets
		//$("#earliestDate").datepicker();
		//$("#latestDate").datepicker();
		
		// Create event handlers.
		$("form#uploadForm").find('select[name="source"]').change(
			function() {
				var sourceId = $(this).find(":selected").val();
				updateInputFields(sourceId);
			}
		);
		 
		$('input:file').change(
			function(){
				updateSubmitButtonStatus();
			}
			);
		
		$("#uploadForm").submit(function() {
			$('form#uploadForm').data('job-running', true)
			updateSubmitButtonStatus();
		});
		
		
		var running = setInitialStatus();
		
		function poll() {
			var jobId = $('form#uploadForm').data('jobid');
			$.ajax({
				url : "jobpoll" + (jobId != null ? "?jobId=" + jobId : ""),
				success : function(data) {
					$('#status').text(data.status);
					$('#statusDate').text(data.statusDate);
					$('#messages').text(data.firstMessage);
					if (running && !data.jobSubmitted) {
						onFinish();
						running = false;
					}
				},
				error : function(xhr) {
					$('#status').text("Job status unavailable: " + xhr.responseText);
					$('#statusDate').empty();
					$('#messages').empty();
				},
				dataType : "json"
			});

		}
		
		poll();

		(function pollStatus() {
			setTimeout(function() {
				poll();
				pollStatus();	
			}, 5000);
		})();
	}

	if ($("#elements").length > 0) {
		var $dataElements = $("#elements").find('tr.editor-home-data-element');
		$dataElements.each(function(i, dataElement) {
			var viewLink = $(dataElement).find('a.view');
			$(viewLink).click(function() {
				viewElement($(dataElement).data('key'));
			});
			 
			var deleteLink = $(dataElement).find('a.delete');
			$(deleteLink).click(function() {
				deleteElement($(dataElement).data('display-name'), 
					$(dataElement).data('key'));
			});
		});
	}

});
	

// Creates a popup containing a summary of a derived data element.
function viewElement(rowId) {

	//$('<div class="tooltip"><div id="tree"></div></div>').appendTo('body');
	//var rowId = event.target.parentNode.id;
	$('div.tooltip').hide();
	$(".rowdata td").css("background", "white");
	$('#'+rowId + " td").css("background", "yellow");
   
	//positionTooltip(event);
  
	$("#tree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "userpropchildren?propKey=" + encodeURIComponent(rowId)
				}
		},
		"plugins" : [ "themes", "json_data", "ui" ]
	});

	$('div.tooltip').show();


}

function deleteElement(displayName, key) {
	var $dialog = $('<div></div>')
		.html('Are you sure you want to delete data element ' + 
			displayName + '? You cannot undo this action.')
		.dialog({
			title : "Delete Data Element",
			modal: true,
			resizable: false,
			buttons : {
				"Delete" : function() {
					$.ajax({
						type: 'POST',
						url: 'deleteprop?key=' + encodeURIComponent(key),
						success: function(data) {
							$(this).dialog("close");
							window.location.href="editorhome";
						}, 
						error: function(data, statusCode) {
							var $errorDialog = $('<div></div>')
								.html(data.responseText)
								.dialog({
									title : "Error Deleting Data Element",
									buttons: {
										"OK" : function() {
											$(this).dialog("close");
										}
										
									},
									close: function() {
										$dialog.dialog("close");
									}
								})
							$errorDialog.dialog("open");

						} 
					});
				},
				"Cancel" : function() {
					$(this).dialog("close");
				}
			},
			close : function() {
				window.location.href="editorhome";
			}
	});
	$dialog.dialog("open");
}

function positionTooltip(event){
	var tPosX = event.pageX + 20;
	var tPosY = event.pageY - 150;
	$('div.tooltip').css({
		'position': 'absolute',
		'top': tPosY,
		'left': tPosX,
		'width': '200px',
		'height': '200px',
		'border':'1px solid black',
		'backgroundColor':'#FFFFEE',
	});
}; 

function insertMailToTag(userName, domainName) {
	var atSign = "&#64;"
	var email = userName + atSign + domainName;

	document.write("<a href='mail" + "to:" + email + "'>" + email +"</a>");
}