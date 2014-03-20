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
		<h3>Account</h3>
		<div id="passwordExpirationMsg" class="passwordExpirationMsg">
			<%=request.getAttribute("passwordExpiration")%>
		</div>
		<form role="form">
			<fieldset>
				<div class="form-group">
					<label for="userName">Name</label>
					<p class="form-control-static" id="userName">
							${user.firstName} ${user.lastName}
					</p>
				</div>
				<div class="form-group">
					<label for="organization">
						Organization
					</label>
					<p class="form-control-static" id="organization">
							${user.organization}
					</p>
				</div>
				<div class="form-group">
					<label for="email">
						Email
					</label>
					<p class="form-control-static" id="email">
							${user.email}
					</p>
				</div>
				<div class="form-group">
					<label for="title">
						Title
					</label>
					<p class="form-control-static" id="title">
							${user.title}
					</p>
				</div>
				<div class="form-group">
					<label for="department">
						Department
					</label>
					<p class="form-control-static" id="department">
							${user.department}
					</p>
				</div>
				</div>
				<div class="form-group text-centered">
					<button id="changePasswordBtn" class="btn btn-primary">Change Password</button>
				</div>
			</fieldset>
		</form>

		<div id="newPasswordModal" class="modal fade" role="dialog" aria-labelledby="newPasswordModalLabel"
			 aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<form id="userAcctForm" class="form-horizontal" action="#" method="post"
						  role="form">
						<div class="modal-header">
							<h4 id="newPasswordModalLabel" class="modal-title">
								Change Password
							</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<label class="control-label" for="oldPassword">Old Password:</label>
								<input type="password" name="oldPassword" id="oldPassword"
									   class="form-control"/>
								<div class="help-block default-hidden">
									<div class="help-inline"></div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label" for="newPassword">New Password:</label>
								<input type="password" name="newPassword" id="newPassword"
									   class="form-control"/>
								<div class="help-block default-hidden">
									<div class="help-inline"></div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label" for="verifyPassword">Re-enter New
									Password:</label>
								<input type="password" name="verifyPassword" id="verifyPassword"
									   class="form-control"/>
								<div class="help-block default-hidden">
									<div class="help-inline"></div>
								</div>
							</div>
							<div class="row">
								<div id="passwordChangeComplete" class="default-hidden">
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<input type="hidden" name="action" value="save"/>
							<input type="submit" value="Save" id="saveAcctBtn" class="btn btn-primary"/>
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/eureka.account.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				eureka.account.setup('#changePasswordBtn', '#newPasswordModal', '#userAcctForm', '#passwordChangeComplete', '#passwordExpirationMsg');
			});
		</script>
	</template:content>
</template:insert>

