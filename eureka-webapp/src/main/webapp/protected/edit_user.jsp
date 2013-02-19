<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 Emory University
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
<%@ taglib uri="/WEB-INF/tlds/function.tld" prefix="myfn"%>



<template:insert template="/templates/eureka_main.jsp">


    <template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>

    <template:content name="content">

		<h3>Edit User ${user.email}</h3>
		<div class="pad pad_top">
			<form action="admin" method="GET" id="editUserForm">
                <table>
                    <tr>
                        <td width="124">Name:</td>
						<td colspan="4">${user.firstName} ${user.lastName}
							(<a href="${pageContext.request.contextPath}/protected/ping?id=${user.id}">Ping</a>)
							<input type="hidden" name="id" value="${user.id}" />
							<input type="hidden" name="action" value="save" />
                        </td>
                    </tr>
                    <tr>
                        <td>Organization:</td>
                        <td colspan="4">${user.organization}</td>
                    </tr>
                    <tr>
                        <td>Email:</td>
                        <td colspan="4">${user.email}</td>
                    </tr>
                    <tr>
                        <td>Role:</td>
                        <td colspan="4">
                            <c:forEach var="role" items="${roles}">
                                <c:set var="hasRole" value="0"></c:set>
                                <c:set var="isSuperUser" value="0"></c:set>

                                <c:forEach var="userRole" items="${user.roles}">

                                    <c:if test="${userRole.id ==  role.id}">
                                        <c:set var="hasRole" value="1"></c:set>
                                    </c:if>
                                    <c:if test="${userRole.name eq 'superuser'}">
                                        <c:set var="isSuperUser" value="1"></c:set>
                                    </c:if>


                                </c:forEach> 
                                <c:choose>
                                    <c:when test="${hasRole == 1}">
                                        <input type="checkbox" name="role" id="role" value="${role.id}" checked ${role.name eq  'superuser' ? "disabled" : ''}/>${role.name}<span class="status"></span><br />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="role" id="role" value="${role.id}" />${role.name}<span class="status"></span><br />

                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>                        
                        </td>
                    </tr>
                    <tr>
                        <td>Activated</td>
                        <td colspan="4">
							<c:choose>
								<c:when test="${user.active == true}">
									<input type="checkbox" name="active" id="active"  checked ${isSuperUser ==  1 ? "disabled" : ''}/><span class="status"></span><br />
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="active" id="active" /><span class="status"></span><br />

								</c:otherwise>
							</c:choose>
                        </td>
                    </tr>

                    <tr>
                        <td>Verification Status:</td>
                        <td colspan="4">
							<c:choose>
								<c:when test="${user.verified == true}">
									Verified<span class="status"></span><br />
								</c:when>
								<c:otherwise>
									Un-verified<span class="status"></span><br />                                   
								</c:otherwise>
							</c:choose>
                        </td>
                    </tr>
                    <tr>
						<td>&nbsp;</td>
						<td><button class="fltrt">Save</button></td>
					</tr>

                </table>

            </form>

        </div>
    </template:content>
    <template:content name="subcontent">
        <%@ include file="../common/rss.jspf" %>
    </template:content>

</template:insert>
