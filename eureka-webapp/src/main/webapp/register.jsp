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
<form id="form2" action="form_action.asp" method="get">
First Name : 
    <input name="fname" type="text" id="first_name" class="register_field" />
    
  <br />
  <br />
Last Name: 
<input id="last_name" class="register_field" type="text" name="lname" />

  <br />
  <br />
Organization : 
    
    <input id="organization" class="register_field" type="text" name="org" />
    <br />
    <br />
Email Address: 
      <input id="email_address" class="register_field" type="text" name="org" />
       <br />
    <br />
    Confirm Email: 
    <input id="user_name" class="register_field" type="text" name="UserName" />
    
    <br />
    <br />
Password: 
<input id="password" class="register_field" type="text" name="Password" />
<br />
<br />
Re-Enter Password: 
<input id="re-enter_password" class="register_field" type="text" name="Password" />
<br />
    <input type="checkbox" name="checkbox" id="checkbox" />
    <label for="checkbox">Click here to join our mailing list</label>
<br />
    <br />
    <input id="button" type="submit" value="Submit"/>
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