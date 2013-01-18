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
	$('#jobUpload').hide();
	
	
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

                  }, error: function(xhr, err) {

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

         }, error: function(xhr, status, error) {

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
	 
	 if ($("#status").length > 0){
		 
		 
			 $('input:file').change(
					 function(){
						 if ($(this).val()) {
							 $('input:submit').attr('disabled',false);
							 // or, as has been pointed out elsewhere:
							 // $('input:submit').removeAttr('disabled'); 
						 } 
					 }
					 );
		 
		 
		 $("#uploadForm").submit(function(){ 
			 $("input[type=submit]", this).attr('disabled', true); 
		 });
		 
	 }
	 
	 if ($("#jobUpload").length > 0){
		poll();

		(function pollStatus() {
			setTimeout(function() {
			  poll();
			  pollStatus();	
			}, 5000);
		})();

     }


     function poll() {

				$.ajax({
					url : "jobpoll",
					success : function(data) {

						if (data['currentStep'] != undefined) {
							if (data['currentStep'] < data['totalSteps']) {
								$('#browseButton').attr('disabled', true);
								$('#status').text(
										data['currentStep'] + " out of "
												+ data['totalSteps']);
			                    $('#jobUpload').show();
							} else {
								$('#browseButton').attr('disabled', false);
								$('#status').text('Complete');
			                    			$('#jobUpload').hide();

							}

							var d = new Date(data['uploadTime']);
							var dateStr = (d.getMonth()+1) + "/" + d.getDate()
									+ "/" + d.getFullYear() + " "
									+ d.toLocaleTimeString();

							$('#statusDate').text(dateStr);
							if (data['messages'][0] == null) {
								$('#messages').text('No errors reported');
							} else {
								$('#messages').text(data['messages'][0]);
							}
						} else {
							$('#browseButton').attr('disabled', false);
							$('#status').text('No jobs have been submitted.');
							$('#statusDate').empty();
							$('#messages').empty();
						}

					},
					dataType : "json"
				});

     }


	

});
	

// Creates a popup containing a summary of a derived data element.
function showPopup(event, rowId) {

    //$('<div class="tooltip"><div id="tree"></div></div>').appendTo('body');
    //var rowId = event.target.parentNode.id;
    $('div.tooltip').hide();
    $(".rowdata td").css("background", "white");
    $('#'+rowId + " td").css("background", "yellow");
   
    //positionTooltip(event);
  
    $("#tree").jstree({
        "json_data" : {
            "ajax" : { "url" : "userpropchildren?propKey=" + rowId}
        },
    "plugins" : [ "themes", "json_data", "ui" ]
    });

    $('div.tooltip').show();


}

function deleteElement(event, rowId) {

     $("#dialog").dialog({
            buttons : {
                "Confirm" : function() {
                    $.ajax({
	                    type: 'POST',
	                    url: 'deleteprop?key='+rowId,
                        success: function(data) {
                            $(this).dialog("close");
                            window.location.href="editorhome";
                    
                    
                        }, error: function(data, statusCode) {
                            $(this).dialog("close");
                            alert(data.statusText);
                    
                    
                        } 
                
    	            });
                },
                "Cancel" : function() {
                    $(this).dialog("close");
                }
            }
        });
    $("#dialog").html("Confirm Deletion");
    $("#dialog").dialog("open");

   
  


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