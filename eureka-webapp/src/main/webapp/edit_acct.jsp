<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
	<script type="text/javascript">



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
	       Old Password:
	       <input id="oldPassword" type="password" name="oldPassword" />

	       <br />
	       <br />
	       Password:
	       <input id="newPassword" type="password" name="Password1" />
	       <br />
	       <br />
	       Re-Enter Password:
	       <input id="verifyPassword" type="password" name="Password2" />
	       <br />
	       <br />
	       <input id="button" type="submit" value="Submit" />
	     </p>
	   </form>
	 
	 
	 </div>

	</template:content>
</template:insert>
