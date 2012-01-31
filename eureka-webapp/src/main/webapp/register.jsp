<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/informatics.jpg" />
	</template:content>
	
	<template:content name="content">
	<script type="text/javascript">

	$(document).ready(function() {
		
		$('#form2').submit(function() {
			  var fname = $('#fname').val(data.firstName);
			  var lname = $('#lname').val(data.lastName);
			  var org 	= $('#org').val(data.organization);
			  var email = $('#email').val(data.email);
			  var pwd1  = $('#password1').val(data.password);
			  var pwd2  = $('#password2').val(data.password);
			  var json_obj = 
			  	{ 
					  "first_name": fname, 
					  "last_name": 	lname, 
					  "email": 		email, 
					  "organization": org
				};
			  
			$.ajax({
				  type: "POST",
				  data: json_obj,
				  url: '/eureka/api/user/0',
				  dataType: 'json',
				  success: function(data) {
					  console.log(data);
				  }
				});

		});
		
	
	});

</script>
    <h3>Register</h3>
<form id="signupForm" action="register" method="get">

	  		  <table class="white">
	  		  <tr>
	  		  	<td class="label white"><label id="lfirstname" for="firstName">First Name</label></td>
	  		  	<td class="field white"><input id="firstName" name="firstName" type="text" value="" class="register_field" /></td>
	  		  	<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class="label white"><label id="llastname" for="lastName">Last Name</label></td>
	  			<td class="field white"><input id="lastName" name="lastName" type="text" value="" class="register_field" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class="label white"><label id="lemail" for="organization">Organization</label></td>
	  			<td class="field white"><input id="organization" name="organization" type="text" value="" class="register_field" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class="label white"><label id="lemail" for="email">Email Address</label></td>
	  			<td class="field white"><input id="email" name="email" type="text" value="" class="register_field" /></td>
	  			<td class="status white"></td>
	  		  </tr>	  		  
	  		  <tr>
	  			<td class="label white"><label id="lemail" for="verifyEmail">Verify Email Address</label></td>
	  			<td class="field white"><input id="verifyEmail" name="verifyEmail" type="text" value="" class="register_field" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class="label white"><label id="lpassword" for="password">Password</label></td>
	  			<td class="field white"><input id="password" name="password" type="password" class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class="label white"><label id="lpassword_confirm" for="verifyPassword">Confirm Password</label></td>
	  			<td class="field white"><input id="verifyPassword" name="verifyPassword" type="password" class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
  			</table>

	<div class="small_text">* Passwords must be at least 8 characters and contain at least one letter and digit
    <br />
    * Disclaimer: Loading real patient data into the system is strictly prohibited.<br />
    <br />
    <input type="checkbox" name="checkbox" id="checkbox" />
    <label for="checkbox">Click here to join our mailing list</label>
   &nbsp;<input id="button" type="submit" value="Submit"/></div>
</form>

	</template:content>
	
	<template:content name="subcontent">
		<div id="release_notes">
			<h3>
				<img src="${pageContext.request.contextPath}/images/rss.png"
					border="0" /> Related News <a href="xml/rss_news.xml" class="rss"></a>
			</h3>
			<script language="JavaScript"
				src="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=1&utf=y"
				charset="UTF-8" type="text/javascript"></script>

			<noscript>
				<a style="padding-left: 35px"
					href="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=200>1&utf=y&html=y">View
					RSS feed</a>
			</noscript>

		</div>
	</template:content>
</template:insert>