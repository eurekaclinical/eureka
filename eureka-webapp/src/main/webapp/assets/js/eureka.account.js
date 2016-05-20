/* global self */

// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.account = new function () {
	var self = this;

	self.setup = function (changePasswordButton, cancelButton, newInfoForm, notificationFailure, notificationFailureMsg, notificationSuccess, contentModal, newPasswordForm, notificationArea, passwordExpirationMsg) {
		$(newInfoForm).submit(this.submit);
                self.setupPasswordSubmit(newPasswordForm, self.createPasswordValidator(newPasswordForm), notificationArea, passwordExpirationMsg);
		$(contentModal).modal({
			keyboard: true,
			backdrop: 'static',
			show: false
		});
                
		$(changePasswordButton).click(function (e) {
			e.preventDefault();
			$(contentModal).modal('show');
			return false;
		});

		$(cancelButton).click(function (e) {
			e.preventDefault();
			e.stopPropagation();
			window.location.href =ctx;
		});
                
		$.validator.addMethod("passwordCheck",
			function (value, element) {
				return value.length >= 8 && /[0-9]+/.test(value) && /[a-zA-Z]+/.test(value);
			},
			"Please enter at least 8 characters with at least 1 digit."
		);
	};
        
        self.validator = $("#userInfoForm").validate({ 
                        errorElement: 'span',
                        errorClass: null,                        
                        rules: {
                                firstName: "required",
                                lastName: "required",
                                organization: {
                                        required: true
                                },
                                email: {
                                        required: true,
                                        email: true
                                },
                                title: {
                                        required: true
                                },
                                department: {
                                        required: true
                                }
                        },
                        messages: {
                                firstName: "Enter your firstname",
                                lastName: "Enter your lastname",
                                organization: "Enter your organization",
                                email: {
                                        required: "Please enter a valid email address",
                                        minlength: "Please enter a valid email address"
                                },
                                title: "Enter your title",
                                department: "Enter your department"
                        },
                        errorPlacement: function (error, element) {
                                $(element).closest('.form-group').find('.help-block').empty();
                                error.appendTo($(element).closest('.form-group').find('.help-block'));
                        },
                        highlight: function (element) {
                                $(element).closest('.form-group').addClass('has-error');
                                $(element).closest('.form-group').find('.help-block').removeClass('default-hidden');
                        },
                        unhighlight: function (element) {
                                $(element).closest('.form-group').removeClass('has-error');
                                $(element).closest('.form-group').find('.help-block').addClass('default-hidden');
                        },
                        success: function (label) {
                                label.html("&nbsp;").addClass("checked");
                        }
                });
                
    	self.submit = function () {
		if (self.validator.valid()) {                  
                        var id = $('#id').val();
                        var firstName = $('#firstName').val();
                        var lastName = $('#lastName').val();
                        var organization = $('#organization').val(); 
                        var email = $('#email').val();
                        var title;
                        var department;
                        if ($('#username').val() === 'superuser') {
                                title = $('#title_superuser').val();
                                department = $('#department_superuser').val();                             
                        } else {
                                title = $('#title').val();
                                department = $('#department').val();                            
                        }
                        
                        var dataString ='action=saveinfo' +
                                '&id=' + id +
                                '&firstName=' + firstName + 
                                '&lastName=' + lastName + 
                                '&organization=' + organization +
                                '&email=' + email + 
                                '&title=' + title + 
                                '&department=' + department;   
                        $.ajax({
                                type: 'POST',
                                url: 'user_acct',
                                data: dataString,                    
                                success: function () {
                                        $('#infoNotificationFailure').hide();
                                        $('#userInfoForm').hide();
                                        $('#infoNotificationSuccess').show();                                     
                                },
                                error: function (xhr){
                                        $('#infoNotificationFailure').show();
                                        $('#infoNotificationFailureMsg').text(xhr.responseText);
                                }
                        });                        
		}
		return false;
	};

	self.setupPasswordSubmit = function (newPasswordForm, passwordValidator, notificationArea, passwordExpirationMsg) {
		$(newPasswordForm).submit(function () {
			var oldPassword = $('#oldPassword').val();
			var newPassword = $('#newPassword').val();
			var verifyPassword = $('#verifyPassword').val();
			var dataString = 'action=savepassword' +
				'&oldPassword=' + oldPassword +
				'&newPassword=' + newPassword +
				'&verifyPassword=' + verifyPassword;

			if (passwordValidator.valid()) {                          
				$.ajax({
					type: 'POST',
					url: 'user_acct',
					data: dataString,
					success: function () {
						$(notificationArea).text("Password has been changed.");
						$(notificationArea).show();
						$(newPasswordForm).find('.form-group').hide();
						$(newPasswordForm).find(':submit').hide();
						$(passwordExpirationMsg).hide();
					},
					error: function (xhr, status, error) {
						$(notificationArea).text(xhr.responseText);
						$(notificationArea).show();
					}
				});
			}
			return false;
		});
	};

	self.createPasswordValidator = function (newPasswordForm) {
		return $(newPasswordForm).validate({
			errorElement: 'span',
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
					rangelength: $.validator.format("Please enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: $.validator.format("Please enter at least {0} characters"),
					equalTo: "Enter the same password as above"
				}
			},
			errorPlacement: function (error, element) {
				error.appendTo($(element).closest('.form-group').find('.help-inline'));
				$('.help-inline').css("color", "#b94a48");                                
			},
			highlight: function (element) {
				$(element).closest('.form-group').addClass('has-error');
				$(element).closest('.form-group').find('.help-block').removeClass('default-hidden');
			},
			unhighlight: function (element) {
				$(element).closest('.form-group').removeClass('has-error');
				$(element).closest('.form-group').find('.help-block').addClass('default-hidden');
			},
			success: function (label) {
				label.html("&nbsp;").addClass("checked");
			}
		});
	};
};