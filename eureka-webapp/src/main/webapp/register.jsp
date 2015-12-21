<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
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
<%@ taglib uri="/WEB-INF/tlds/function.tld" prefix="myfn" %>

<template:insert template="/templates/eureka_main.jsp">
<template:content name="content">
<c:choose>
<c:when test="${pageContext.request.remoteUser != null and myfn:isUserInRole(pageContext.request, 'researcher')}">
	<div class="alert alert-danger">
		You are already logged in!  If you would like to register a new user, please
		<a href="${pageContext.request.contextPath}/logout">logout</a> and try again.
	</div>
</c:when>
<c:otherwise>
<div id="registerHeading">
	<c:choose>
		<c:when test="${not empty authenticationMethod}">
			<h3>Register using your ${not empty accountTypeDisplayName ? accountTypeDisplayName : authenticationMethod} account</h3>
			<p>Please review and complete your profile.</p>
		</c:when>
		<c:otherwise>
			<h3>Register</h3>
			<c:set var="authenticationMethod" value="LOCAL"/>
		</c:otherwise>
	</c:choose>
	<c:if test="${applicationScope.webappProperties.demoMode or applicationScope.webappProperties.ephiProhibited}">
		<div class="alert alert-warning">
				<strong>Disclaimer: Loading real patient data into the system is strictly prohibited!</strong>
		</div>
	</c:if>
</div>
<form id="signupForm" action="#" method="POST" novalidate role="form">
	<input type="hidden" name="authenticationMethod" id="authenticationMethod" value="${authenticationMethod}"/>
	<input type="hidden" name="fullName" id="fullName" value="${fullName}" />
	<input type="hidden" name="username" id="username" value="${username}" />
	<c:if test="${authenticationMethod == 'OAUTH'}">
		<input type="hidden" name="providerUsername" id="providerUsername" value="${providerUsername}" />
		<input type="hidden" name="oauthProvider" id="oauthProvider" value="${oauthProvider}" />
	</c:if>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="firstName" class="control-label">First Name</label>
				<input id="firstName" name="firstName" type="text"
					   class="form-control" value="${firstName}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="lastName" class="control-label">Last Name</label>
				<input id="lastName" name="lastName" type="text"
					   class="form-control" value="${lastName}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="organization" class="control-label">Organization</label>
				<input id="organization" name="organization" type="text"
					   class="form-control" value="${organization}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="title" class="control-label">Title</label>
				<input id="title" name="title" type="text"
					   class="form-control" value="${title}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="department" class="control-label">Department</label>
				<input id="department" name="department" type="text"
					   class="form-control" value="${department}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="email" class="control-label">Email Address</label>
				<input id="email" name="email" type="text"
					   class="form-control" value="${email}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="verifyEmail" class="control-label">Verify Email Address</label>
				<input id="verifyEmail" name="verifyEmail" type="text"
					   class="form-control" value="${email}"/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<c:if test="${authenticationMethod == 'LOCAL'}">
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="password" class="control-label">Password</label>
				<input id="password" name="password" type="password"
					   class="form-control" value=""/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="form-group">
				<label for="verifyPassword" class="control-label">Confirm Password</label>
				<input id="verifyPassword" name="verifyPassword"
					   type="password" class="form-control" value=""/>
				<span class="help-block default-hidden"></span>
			</div>
		</div>
	</div>
	</c:if>
	<c:if test="${applicationScope.webappProperties.demoMode}">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="agreement" class="control-label">
						<input type="checkbox" name="agreement" id="agreement" class="checkbox checkbox-inline"/>
						I agree to the <a id="agreementAnchor" href="#">End User Agreement</a>
					</label>
					<span class="help-block default-hidden"></span>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${authenticationMethod == 'LOCAL'}">
	<div class="row">
		<div class="col-sm-12">
			<div class="form-group">
				*Passwords must be at least 8 characters and contain at least
				one letter & digit
			</div>
		</div>
	</div>
	</c:if>
	<div class="row">
		<div id="passwordChangeFailure" class="default-hidden help-block has-error">
			<div id="passwordErrorMessage"></div>
		</div>
	</div>
	<button id="submit" type="submit" class="btn btn-primary">
		Submit
	</button>
</form>
<div id="registrationComplete" class="default-hidden alert alert-success">
	<p><strong>Your request has been submitted.</strong> You will be notified once your account is activated.</p>
</div>
<div id="agreementModal" class="modal" role="dialog" aria-labelledby="agreementModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 id="agreementModalLabel" class="modal-title">
					End User Agreement
				</h4>
			</div>
			<div class="modal-body">
				<strong>This is a demonstration website. It is not suitable for use with sensitive data
					including patient data that contains identifiers. Do not under any circumstances load
					sensitive data to this site.</strong>
				<p>This is beta-quality software. There exists the risk that data that you upload to this site
					could accidentally be exposed, deleted or corrupted. You agree to accept the risk that this
					web site and the software on it may delete, corrupt or publicly disclose any data you upload
					to this site and agree to hold Emory University and the individual contributors to this web
					site harmless from any negative consequence of you uploading data to this site.
				</p>
				<p>By uploading data to this web site, you warrant that you have the legal right to do so. You
					also agree that if you upload data to this web set that you are not legally entitled to
					upload, you will hold Emory University and all other parties connected with this web site
					harmless from the consequences of your action.
				</p>
				<p>This web site, the software on the web site and any other intellectual property on the web
					site are provided &quot;as is&quot; without warranty of any kind. Emory University and the
					individual contributors to this web site, its software and content disclaim all warranties,
					express and implied, including and without limitation, any implied warranties of
					merchantability, fitness for a particular purpose or noninfringement.
				</p>
				<p>In no event shall Emory University or the individual contributors to this web site, its
					software or other intellectual property be liable for any indirect, incidental, punitive or
					consequential damages, or damages for loss of profits, revenue, data or data use, incurred
					by you or any third party, whether in an action in contract or tort, even if Emory
					University or any of the individual contributors to this web site, its software or other
					intellectual property have been advised of the possibility of such damages.
				</p>
				<p>In no event shall the entire liability of Emory University or any of the individual
					contributors to this web site exceed one thousand dollars ($1,000).
				</p>
				<p>This agreement is governed by the substantive and procedural laws of the State of Georgia.
					You, Emory University and the individual contributors to this web site agree to submit to
					the exclusive jurisdiction of, and venue in, the courts of Dekalb or Fulton counties in the
					State of Georgia in any dispute arising out of or relating to this agreement.
				</p>
				<p>If any provision of this Agreement is held to be unenforceable, this Agreement will remain in
					effect with the provision omitted.
				</p>
				<p>Please do provide feedback to the developers using the email address on the site's Contact
					page if you discover these or any other bugs in the software.
				</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/jquery.validate.js"></script>
<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/eureka.registration${initParam['eureka-build-timestamp']}.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
		eureka.registration.setup($('#signupForm'), $('#agreementAnchor'), $('#agreementModal'));
	});
</script>
</c:otherwise>
</c:choose>
</template:content>
</template:insert>
