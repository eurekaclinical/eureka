// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.reset = new function () {
	var self = this;

	self.setup = function (resetPasswordFormElem, emailValueElem) {
		$(resetPasswordFormElem).submit(function () {
			var email = $(emailValueElem).val();
			var dataString = 'email=' + email;
			$.ajax({
				type: 'POST',
				url: 'forgot_password',
				data: dataString,
				success: function () {
					$('#passwordresetComplete').show();
					$('#passwordresetComplete').text("Password has been reset. You will receive an email with the new password.");
					$('#saveAcctBtn').hide();

				},
				error: function (xhr /*, status, error */) {
					$('#passwordresetComplete').show();
					$('#passwordresetComplete').text(xhr.responseText);
				}
			});
			return false;
		});
	}
}
