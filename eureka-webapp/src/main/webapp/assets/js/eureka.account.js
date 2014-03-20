// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.account = new function () {
	var self = this;

	self.setup = function (changePasswordButton, contentModal, newPasswordForm, notificationArea, passwordExpirationMsg) {
		self.setupSubmit(newPasswordForm, self.createValidator(newPasswordForm), notificationArea, passwordExpirationMsg);

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

		$.validator.addMethod("passwordCheck",
			function (value, element) {
				return value.length >= 8 && /[0-9]+/.test(value) && /[a-zA-Z]+/.test(value);
			},
			"Please enter at least 8 characters with at least 1 digit."
		);
	};

	self.setupSubmit = function (newPasswordForm, validator, notificationArea, passwordExpirationMsg) {
		$(newPasswordForm).submit(function () {
			var oldPassword = $('#oldPassword').val();
			var newPassword = $('#newPassword').val();
			var verifyPassword = $('#verifyPassword').val();
			var dataString = 'action=save' +
				'&oldPassword=' + oldPassword +
				'&newPassword=' + newPassword +
				'&verifyPassword=' + verifyPassword;

			if (validator.valid()) {
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

	self.createValidator = function (newPasswordForm) {
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
				error.appendTo($(element).closest('.form-group').find('.help-block .help-inline'));
			},
			highlight: function (element) {
				$(element).closest('.form-group').addClass('has-error');
				$(element).closest('.form-group').find('.help-block').removeClass('default-hidden');
			},
			unhighlight: function (element) {
				$(element).closest('.form-group').removeClass('has-error');
				$(element).closest('.form-group').find('.help-block').addClass('default-hidden');
			},
			// set this class to error-labels to indicate valid fields
			success: function (label) {
				// set &nbsp; as text for IE
				label.html("&nbsp;").addClass("checked");
			}
		});
	};
};