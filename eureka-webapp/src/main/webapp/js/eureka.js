$(document).ready(function() {

	
	var fileUploadErrors = {
	    maxFileSize: 'File is too big',
	    minFileSize: 'File is too small',
	    acceptFileTypes: 'Filetype not allowed',
	    maxNumberOfFiles: 'Max number of files exceeded',
	    uploadedBytes: 'Uploaded bytes exceed file size',
	    emptyResult: 'Empty file upload result'
	};
	
	
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
					minlength: 8
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
			},
			messages: {
				firstName: "Enter your firstname",
				lastName: "Enter your lastname",
				organization: "Enter your organization",
				password: {
					required: "Provide a password",
					rangelength: jQuery.format("Enter at least {0} characters")
				},
				verifyPassword: {
					required: "Repeat your password",
					minlength: jQuery.format("Enter at least {0} characters"),
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
	
	$('#editAcctBtn').click(function(){
	     $('#newPasswordTable').show();
	     $('#editAcctBtn').hide();
	     $('#saveAcctBtn').show();
    });
	
	 $("#signupForm").submit(function() {
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
                  $('#signupForm').hide();
                  $('#registerHeading').hide();
                  $('#registrationComplete').show();
			  }
			});

        return false;
		

	 });
	
	

});
	
