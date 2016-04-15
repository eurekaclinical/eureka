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


<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<h3>Account Settings ${user.username}</h3>
		<div id="passwordExpirationMsg" class="passwordExpirationMsg">
			<%=request.getAttribute("passwordExpiration")%>
			<br>
		</div>
		<form id="userInfoForm" action="#" method="POST" role="form">                 
                        <input type="hidden" name="fullName" id="fullName" value="${user.fullName}" />
                        <input type="hidden" name="username" id="username" value="${user.username}" />                        
                        <div class="row">
                                <div class="col-sm-6">
                                        <div class="form-group">
                                                <label for="firstName" class="control-label">First Name</label>
                                                <input id="firstName" name="firstName" type="text"
                                                           class="form-control" value="${user.firstName}"/>
                                                <span class="help-block default-hidden"></span>
                                        </div>
                                </div>
                        </div>
                        <div class="row">
                                <div class="col-sm-6">
                                        <div class="form-group">
                                                <label for="lastName" class="control-label">Last Name</label>
                                                <input id="lastName" name="lastName" type="text"
                                                           class="form-control" value="${user.lastName}"/>
                                                <span class="help-block default-hidden"></span>
                                        </div>
                                </div>
                        </div>                               
                        <div class="row">
                                <div class="col-sm-6">
                                        <div class="form-group">
                                                <label for="organization" class="control-label">Organization</label>
                                                <input id="organization" name="organization" type="text"
                                                           class="form-control" value="${user.organization}"/>
                                                <span class="help-block default-hidden"></span>
                                        </div>
                                </div>
                        </div>
                        <c:choose>  
                                <c:when test="${user.username == 'superuser'}">
                                        <div class="row">
                                                <div class="col-sm-6">
                                                        <div class="form-group">
                                                                <label for="title_superuser" class="control-label">Title</label>
                                                                <input id="title_superuser" name="title_superuser" type="text"
                                                                           class="form-control" 
                                                                           <c:choose>
                                                                                <c:when test="${user.title == 'undefined' || user.title == '' || user.title == 'null' }">
                                                                                    value=""
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    value="${user.title}"
                                                                                </c:otherwise>
                                                                           </c:choose>
                                                                />
                                                                <span class="help-block default-hidden"></span>
                                                        </div>
                                                </div>
                                        </div>
                                        <div class="row">
                                                <div class="col-sm-6">
                                                        <div class="form-group">
                                                                <label for="department_superuser" class="control-label">Department</label>
                                                                <input id="department_superuser" name="department_superuser" type="text"
                                                                           class="form-control"  
                                                                           <c:choose>
                                                                                <c:when test="${user.department == 'undefined' || user.department == '' || user.department == 'null' }">
                                                                                    value=""
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    value="${user.department}"
                                                                                </c:otherwise>
                                                                           </c:choose>                                                                           
                                                                />
                                                                <span class="help-block default-hidden"></span>
                                                        </div>
                                                </div>
                                        </div>
                                </c:when>
                                <c:otherwise> 
                                        <div class="row">
                                                <div class="col-sm-6">
                                                        <div class="form-group">
                                                                <label for="title" class="control-label">Title</label>
                                                                <input id="title" name="title" type="text"
                                                                           class="form-control" value="${user.title}"/>
                                                                <span class="help-block default-hidden"></span>
                                                        </div>
                                                </div>
                                        </div>
                                        <div class="row">
                                                <div class="col-sm-6">
                                                        <div class="form-group">
                                                                <label for="department" class="control-label">Department</label>
                                                                <input id="department" name="department" type="text"
                                                                           class="form-control" value="${user.department}"/>
                                                                <span class="help-block default-hidden"></span>
                                                        </div>
                                                </div>
                                        </div>                                    
                                </c:otherwise>    
                        </c:choose>                        
                        <div class="row">
                                <div class="col-sm-6">
                                        <div class="form-group">
                                                <label for="email" class="control-label">Email Address</label>
                                                <input id="email" name="email" type="text"
                                                           class="form-control" value="${user.email}"/>
                                                <span class="help-block default-hidden"></span>
                                        </div>
                                </div>
                        </div>        
                        <div class="row">
                                <div id="infoNotificationFailure" class="default-hidden help-block has-error">
                                            <div id="infoNotificationFailureMsg"></div>                                    
                                </div>
                        </div>                                                    
                        <div class="row">
                                <div class="col-sm-6">
                                        <div class="form-group">  
                                                <c:if test="${user['class'].name == 'edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUser'}">                        
                                                        <button id="changePasswordBtn" class="btn btn-primary text-center">Change Password</button>
                                                </c:if>
                                                <a href="${pageContext.request.contextPath}/#/index" class="btn btn-primary">
                                                        <span></span>
                                                        Cancel
                                                </a>
                                                <input type="hidden" name="id" id="id" value="${user.id}" /> 
                                                <input type="hidden" name="action" value="save"/>
                                                <input type="submit" value="Save Changes" id="saveEditBtn" class="btn btn-primary text-center"/>  
                                        </div>
                                </div>
                        </div>                            
		</form>
		<div id="infoNotificationSuccess" class="default-hidden alert alert-success">
                        <p><strong>Your changes has been saved successfully.</strong></p>
		</div>                                        
		<c:if test="${user['class'].name == 'edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUser'}">
                        <div id="newPasswordModal" class="modal fade" role="dialog" aria-labelledby="newPasswordModalLabel"
                                 aria-hidden="true">
                                <div class="modal-dialog">
                                        <div class="modal-content">
                                                <form id="userCredentialForm" class="form-horizontal" action="#" method="post"
                                                          role="form">
                                                        <div class="modal-header">
                                                                <h4 id="newPasswordModalLabel" class="modal-title">
                                                                        Change Password
                                                                </h4>
                                                        </div>
                                                        <div class="modal-body">
                                                            
                                                                <div class="form-group">
                                                                    <label class="col-sm-3 control-label" for="oldPassword">Old Password:</label>                                                                
                                                                </div>
                                                                <div class="form-group">
                                                                        <div class="col-sm-7">                                                                 
                                                                                <input type="password" name="oldPassword" id="oldPassword"
                                                                                           class="form-control"/>
                                                                        </div>
                                                                        <span class="col-sm-5 help-inline"></span>
                                                                </div>   
                                                                
                                                                <div class="form-group">
                                                                        <label class="col-sm-3 control-label" for="newPassword">New Password:</label>
                                                                </div>
                                                                <div class="form-group">
                                                                        <div class="col-sm-7">                                                                        
                                                                                <input type="password" name="newPassword" id="newPassword"
                                                                                           class="form-control"/>
                                                                        </div>
                                                                        <span class="col-sm-5 help-inline"></span>
                                                                </div>  
                                                            
                                                          
                                                                <div class="form-group">
                                                                        <label class="col-sm-4 control-label" for="verifyPassword">Re-enter New Password:</label>  
                                                                </div>
                                                                <div class="form-group">
                                                                        <div class="col-sm-7">                                                                      
                                                                                <input type="password" name="verifyPassword" id="verifyPassword"
                                                                                           class="form-control col-sm-4"/>
                                                                        </div>
                                                                        <span class="col-sm-5 help-inline"></span>
                                                                </div>

                                                            
                                                                <div class="row">
                                                                        <div id="passwordChangeNotificationMsg" class="col-sm-10 default-hidden">
                                                                        </div>
                                                                </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                                <input type="hidden" name="action" value="save"/>
                                                                <input type="submit" value="Save Password" id="savePasswordBtn" class="btn btn-primary"/>
                                                                <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                                                        </div>
                                                </form>
                                        </div>
                                </div>
                        </div>
		</c:if>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/eureka.account${initParam['eureka-build-timestamp']}.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				eureka.account.setup('#changePasswordBtn', '#userInfoForm', '#infoNotificationFailure', '#infoNotificationFailureMsg', '#infoNotificationSuccess', '#newPasswordModal', '#userCredentialForm', '#passwordChangeNotificationMsg', '#passwordExpirationMsg');
			});
		</script>
	</template:content>
</template:insert>

