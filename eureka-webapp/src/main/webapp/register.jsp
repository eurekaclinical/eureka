<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_sidebar.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/informatics.jpg" />
	</template:content>
	
	<template:content name="content">

    <h3 id="registerHeading">Register</h3>
	<c:if test="${applicationScope.webappProperties.demoMode or applicationScope.webappProperties.ephiProhibited}">
    <p><span class="pad"><b>*Disclaimer: Loading real patient data into the system is strictly prohibited.</b> </span></p>
	</c:if>
<form id="signupForm" action="#" method="post" class="pad">

	  		  <table class="white">
	  		  <tr>
	  		  	<td class=" white"><label id="lfirstname" for="firstName">First Name</label></td>
	  		  	<td class="field white"><input id="firstName" name="firstName" type="text"  class="register_field" value="" /></td>
	  		  	<td class="status white shift_left error"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="llastname" for="lastName">Last Name</label></td>
	  			<td class="field white"><input id="lastName" name="lastName" type="text" class="register_field" value=""/></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="organization">Organization</label></td>
	  			<td class="field white"><input id="organization" name="organization" type="text"  class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="ltitle" for="title">Title</label></td>
	  			<td class="field white"><input id="title" name="title" type="text"  class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>	
	  		  <tr>
	  			<td class=" white"><label id="ldepartment" for="department">Department</label></td>
	  			<td class="field white"><input id="department" name="department" type="text"  class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>	
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="email">Email Address</label></td>
	  			<td class="field white"><input id="email" name="email" type="text"  class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>	  		  
	  		  <tr>
	  			<td class=" white"><label id="lemail" for="verifyEmail">Verify Email Address</label></td>
	  			<td class="field white"><input id="verifyEmail" name="verifyEmail" type="text"  class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lpassword" for="password">Password</label></td>
	  			<td class="field white"><input id="password" name="password" type="password" class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>
	  		  <tr>
	  			<td class=" white"><label id="lpassword_confirm" for="verifyPassword">Confirm Password</label></td>
	  			<td class="field white"><input id="verifyPassword" name="verifyPassword" type="password" class="register_field" value="" /></td>
	  			<td class="status white shift_left error"></td>
	  		  </tr>
  			</table>
  	    <div id="passwordChangeFailure">
        	<p class="pw_fail error" id="passwordErrorMessage"></p>
    	</div>
	<div class="small_text pad" style="float:left">
    <p>*Passwords must be at least 8 characters and contain at least one letter & digit</p>
	<c:if test="${applicationScope.webappProperties.demoMode}">
		<input type="checkbox" name="agreement" id="agreement" style="padding:0px" />
		<label for="checkbox" style="display: inline">
			<a style="pad" href="end_user.jsp">End User Agreement</a>
		</label>
	</c:if>
    &nbsp;&nbsp;&nbsp;
<button id="submit" type="submit" class="btn btn-primary submit">Submit</button>
    <br />
    </p>
    </div>
</form>
	<div id="registrationComplete" style="text-align:left; padding-left:150px; font-weight:bold;">
		<p>
        </br> 
        <p> Your request has been successfully submitted.
        </br> You will be notified once your account is activated.
        </p>
	</div>
	</template:content>
</template:insert>
