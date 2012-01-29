<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
	<script type="text/javascript">

	$(document).ready(function() {
		$.ajax({
			  url: '/eureka/api/users/'+ $.getUrlVar('id'),
			  dataType: 'json',
			  success: function(data) {
				  console.log(data);
				  $('#fname').val(data.firstName);
				  $('#lname').val(data.lastName);
				  $('#org').val(data.organization);
				  $('#email').val(data.email);
				  $('#password1').val(data.password);
				  $('#password2').val(data.password);
			  }
			});
	
	});

</script>
    <h3>Edit Account</h3>
	 <div>
	   <form id="form2" action="form_action.asp" method="get">
	     <p>First Name :
	       <input name="fname" type="text" id="fname" />
	       <br />
	       <br />
	       Last Name:
	       <input id="lname" type="text" name="lname" />
	     </p>
	     <p>Organization :
	       <input id="org" type="text" name="org" />
	       <br />
	       User Name (email):
	       <input id="email" type="text" name="email" />
	       <br />
	       <br />
	       Password:
	       <input id="password1" type="password" name="Password1" />
	       <br />
	       <br />
	       Re-Enter Password:
	       <input id="password2" type="password" name="Password2" />
	       <br />
	       <br />
	       <input id="button" type="submit" value="Submit" />
	     </p>
	   </form>
	 
	 
	 </div>

	</template:content>
</template:insert>
