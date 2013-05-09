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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>

<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/scientific_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>Login</h3>
		<form method="POST" action="j_security_check" style="width: 80%; text-align: right">
			<p>
				Enter your e-mail address and password:
			</p>
			<span style="text-align: right">
				E-mail Address:
				<input type="text" name="j_username" class="login_field required" tabindex="1" accesskey="e" size="25" autocomplete="false"/>
				<br>
				<br>
				Password:
				<input type="password" name="j_password" class="login_field required" tabindex="2" accesskey="p" size="25" autocomplete="false"/>
				<br>
				<br>
			</span>
			<c:if test="${applicationScope.webappProperties.demoMode}">
			*Please note that loading real patient data into the system is strictly prohibited!
			</c:if>
			<br>
			<br>
			<div class="row btn-row">
				<input name="submit" id="submit" class="submit" accesskey="l" value="Login" tabindex="3" type="submit" />
				<input name="reset" id="reset" class="btn-reset" accesskey="c" value="Clear" tabindex="4" type="reset" />
				<br>
				<br>
				<span class="sub_text">
					<a href="${pageContext.request.contextPath}/forgot_password.jsp">Login Help</a>
				</span>
			</div>
		</form>
	</template:content>

	<template:content name="subcontent">
		<%@ include file="common/rss.jspf" %>
	</template:content>
</template:insert>
