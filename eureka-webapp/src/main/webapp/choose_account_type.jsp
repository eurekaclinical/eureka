<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2015 Emory University
  %%
  This program is dual licensed under the Apache 2 and GPLv3 licenses.
  
  Apache License, Version 2.0:
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
  GNU General Public License version 3:
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>

<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<div id="chooseAccountTypeHeading">
			<h3>Select an Account Type</h3>
		</div>
		<div class="container">
			<div class="row">
				<div class="col-sm-6 col-sm-offset-3">
					<c:if test="${googleAuthEnabled}">
						<a href="${Google2ProviderUrl}" class="btn btn-block btn-social btn-lg btn-google-plus" title="Register using your existing Google account">
							<i class="fa fa-google-plus"></i>
							Use an existing Google account
						</a>
					</c:if>
					<c:if test="${gitHubAuthEnabled}">
						<a href="${GitHubProviderUrl}" class="btn btn-block btn-social btn-lg btn-github" title="Register using your existing GitHub account">
							<i class="fa fa-github"></i>
							Use an existing GitHub account
						</a>
					</c:if>
					<c:if test="${twitterAuthEnabled}">
						<a href="${SSLTwitterProviderUrl}" class="btn btn-block btn-social btn-lg btn-twitter" title="Register using your existing Twitter account">
							<i class="fa fa-twitter"></i>
							Use an existing Twitter account
						</a>
					</c:if>
					<c:if test="${globusAuthEnabled}">
						<a href="${GlobusProviderUrl}" 
						   class="btn btn-block btn-primary btn-lg"
						   title="Register using your existing Globus account">Use an existing Globus account</a>
					</c:if>
					<c:if test="${localAccountRegistrationEnabled}">
						<a href="${pageContext.request.contextPath}/register.jsp"
						   class="btn btn-block btn-primary btn-lg">Create a traditional user account</a>
					</c:if>
				</div>
			</div>
		</div>
	</template:content>
</template:insert>
