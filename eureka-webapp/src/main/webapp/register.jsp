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

<template:insert template="/templates/eureka_main.jsp">
<template:content name="content">
<h3 id="registerHeading">Register</h3>
<c:if test="${applicationScope.webappProperties.demoMode or applicationScope.webappProperties.ephiProhibited}">
	<p>
		<span class="pad">
			<b>*Disclaimer: Loading real patient data into the system is
				strictly prohibited.</b>
		</span>
	</p>
</c:if>
<form id="signupForm" action="#" method="POST" novalidate
	  class="form-horizontal" role="form">
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="firstName">First Name</label>
		</div>
		<div class="col-sm-6">
			<input id="firstName" name="firstName" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="lastName">Last Name</label>
		</div>
		<div class="col-sm-6">
			<input id="lastName" name="lastName" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="organization">Organization</label>
		</div>
		<div class="col-sm-6">
			<input id="organization" name="organization" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="title">Title</label>
		</div>
		<div class="col-sm-6">
			<input id="title" name="title" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="department">Department</label>
		</div>
		<div class="col-sm-6">
			<input id="department" name="department" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="email">Email Address</label>
		</div>
		<div class="col-sm-6">
			<input id="email" name="email" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="verifyEmail">Verify Email Address</label>
		</div>
		<div class="col-sm-6">
			<input id="verifyEmail" name="verifyEmail" type="text"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="password">Password</label>
		</div>
		<div class="col-sm-6">
			<input id="password" name="password" type="password"
				   class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-2 control-label">
			<label for="verifyPassword">Confirm Password</label>
		</div>
		<div class="col-sm-6">
			<input id="verifyPassword" name="verifyPassword"
				   type="password" class="form-control" value=""/>
		</div>
		<div class="col-sm-4 help-block">
			<div class="help-inline"></div>
		</div>
	</div>
	<c:if test="${applicationScope.webappProperties.demoMode}">
		<div class="form-group">
			<div class="col-sm-2 control-label">
				<label for="agreement">
					End User Agreement
				</label>
			</div>
			<div class="col-sm-6">
				<input type="checkbox" name="agreement" id="agreement"/>
				I agree to the <a id="agreementAnchor" href="#">End User Agreement</a>
			</div>
			<div class="col-sm-4 help-block">
				<div class="help-inline"></div>
			</div>
		</div>
	</c:if>
	<div class="row">
		<div class="col-sm-8 text-center">
			*Passwords must be at least 8 characters and contain at least
			one letter & digit
		</div>
	</div>
	<div class="row">
		<div id="passwordChangeFailure" class="col-sm-8 default-hidden help-block has-error">
			<div class="help-inline" id="passwordErrorMessage"></div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-8 text-center vert-offset">
			<button id="submit" type="submit" class="btn btn-primary">
				Submit
			</button>
		</div>
	</div>
</form>
<div id="registrationComplete" class="default-hidden">
	<p>
		Your request has been successfully submitted.
	</p>
	<p>
		You will be notified once your account is activated.
	</p>
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
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/jquery.validate.js"></script>
<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/eureka.registration.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
		eureka.registration.setup($('#signupForm'), $('#agreementAnchor'), $('#agreementModal'));
	});
</script>
</template:content>
</template:insert>
