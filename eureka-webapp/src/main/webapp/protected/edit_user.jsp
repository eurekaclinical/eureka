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
<%@ taglib uri="/WEB-INF/tlds/function.tld" prefix="myfn" %>


<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<h3>Edit User ${user.email}</h3>

		<form id="userform" action="admin" method="GET" id="editUserForm" role="form" class="">
			<div class="form-group">
				<label>
					Name
				</label>
				<div class="form-control-static">
						${user.firstName} ${user.lastName}
					(<a href="${pageContext.request.contextPath}/protected/ping?id=${user.id}">Ping</a>)
				</div>
			</div>
			<div class="form-group">
				<label>
					Organization
				</label>
				<div class="form-control-static">
						${user.organization}
				</div>
			</div>
			<div class="form-group">
				<label>
					Title
				</label>
				<div class="form-control-static">
						${user.title}
				</div>
			</div>
			<div class="form-group">
				<label>
					Department
				</label>
				<div class="form-control-static">
						${user.department}
				</div>
			</div>
			<div class="form-group">
				<label>
					Email
				</label>
				<div class="form-control-static">
						${user.email}
				</div>
			</div>
			<div class="form-group">
				<label>
					Role
				</label>
				<div class="form-control-static">
					<c:set var="isSuperUser" value="0"></c:set>
					<c:forEach var="role" items="${roles}">
						<c:set var="hasRole" value="0"></c:set>
						<c:forEach var="userRole" items="${user.roles}">
							<c:if test="${userRole ==  role.id}">
								<c:set var="hasRole" value="1"></c:set>
								<c:if test="${role.name eq 'superuser'}">
									<c:set var="isSuperUser" value="1"></c:set>
								</c:if>
							</c:if>
						</c:forEach>
						<c:choose>
							<c:when test="${hasRole == 1}">
								<input type="checkbox" name="role" value="${role.id}"
									   checked="checked" ${role.name eq  'superuser' ? 'disabled="disabled"' : ''}/>${role.name}
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="role" value="${role.id}"/>${role.name}
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
			<div class="form-group">
				<div class="checkbox checkbox-inline">
					<label>
						<c:choose>
							<c:when test="${user.active == true}">
								<input type="checkbox" name="active" id="active"
									   checked ${isSuperUser ==  1 ? "disabled" : ''}/>
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="active" id="active"/>
							</c:otherwise>
						</c:choose>
						Activated
					</label>
				</div>
			</div>
			<div class="form-group">
				<label>
					Verification Status:
				</label>
				<div class="form-control-static">
					<c:choose>
						<c:when test="${user.verified == true}">
							Verified<span class="status"></span><br />
						</c:when>
						<c:otherwise>
							Un-verified<span class="status"></span><br />
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="id" value="${user.id}"/>
				<input type="hidden" name="action" value="save"/>
				<button type="submit" class="btn btn-primary">Save</button>
			</div>
		</form>
		<script type="text/javascript">
			$(document).ready(function () {
				$('#userform').submit(function () {
					$('#userform :input').each(function () {
						$(this).removeAttr('disabled');
					});
					return true;
				});
			});
		</script>
	</template:content>

</template:insert>
