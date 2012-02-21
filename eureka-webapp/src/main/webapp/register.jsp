<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/informatics.jpg" />
	</template:content>
	
	<template:content name="content">

    <h3 id="registerHeading">Register</h3>
    <p><span class="pad">*Disclaimer: Loading real patient data into the system is strictly prohibited. </span></p>
<form id="signupForm" action="#" method="post" class="pad">

	  		  <table class="white">
	  		  <tr>
	  		  	<td class=" white"><label id="lfirstname" for="firstName">First Name</label></td>
	  		  	<td class="field white"><input id="firstName" name="firstName" type="text"  class="register_field" value="" /></td>
	  		  	<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="llastname" for="lastName">Last Name</label></td>
	  			<td class="field white"><input id="lastName" name="lastName" type="text" class="register_field" value=""/></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="organization">Organization</label></td>
	  			<td class="field white"><input id="organization" name="organization" type="text"  class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="email">Email Address</label></td>
	  			<td class="field white"><input id="email" name="email" type="text"  class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>	  		  
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="verifyEmail">Verify Email Address</label></td>
	  			<td class="field white"><input id="verifyEmail" name="verifyEmail" type="text"  class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lpassword" for="password">Password</label></td>
	  			<td class="field white"><input id="password" name="password" type="password" class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lpassword_confirm" for="verifyPassword">Confirm Password</label></td>
	  			<td class="field white"><input id="verifyPassword" name="verifyPassword" type="password" class="register_field" value="" /></td>
	  			<td class="status white"></td>
	  		  </tr>
  			</table>
	<div class="small_text pad">*Passwords must be at least 8 characters and contain at least one letter and digit

    <br />
    <input type="checkbox" name="checkbox" id="checkbox" style="padding:0px" />
    <label for="checkbox" style="display: inline">Click here to join our mailing list</label>
   &nbsp;
    <button id="submit" type="submit" class="btn btn-primary submit">Submit</button>
    <br />
    </div>
</form>
	<div id="registrationComplete">
		<h2>Your request has been successfully submitted. You will be notified once your account is activated.</h2>
	</div>
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