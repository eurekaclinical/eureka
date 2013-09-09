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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

    <template:content name="sidebar">
            <img src="${pageContext.request.contextPath}/images/data_analysis.jpg" />
    </template:content>

    <template:content name="content">

    <h3>Administration </h3>

    
    <form id="form" name="form1" method="post" action="" class="pad_top pad">
      <table>
      <tr class="bold">
        <td width="350">User Name</td>
        <td width="100">Last Login</td>
        <td width="300">Role</td>
        <td width="217">Email</td>
        <td width="179">Organization</td>
        <td width="100">Status</td>
        <td width="200">Title</td>
        <td width="200">Department</td>
      </tr>
       <c:forEach items="${users}" var="user">
        <tr>
            <td>
                <c:set var="is_admin" value="false" />
                <c:set var="is_inactive" value="false" />
                <c:forEach var="role" items="${user.roles}">
                    <c:if test="${roles[role].name == 'admin'}">
                        <c:set var="is_admin" value="true" />                   
                    </c:if>                     
                </c:forEach>            
                <c:if test="${user.active == false}">
                    <c:set var="is_inactive" value="true" />                    
                </c:if> 
                <c:choose>
                    <c:when test="${is_inactive == true}">
                            <img src="${pageContext.request.contextPath}/images/New_User.gif"/>                     
                    </c:when>
                    
                    <c:otherwise>
                      <c:choose>
                          <c:when test="${is_admin == true}">
                            <img src="${pageContext.request.contextPath}/images/Role_Admin.gif"/>
                          </c:when> 
                          <c:otherwise>
                            <img src="${pageContext.request.contextPath}/images/Role_Researcher.gif"/>                          
                          </c:otherwise>
                  </c:choose>
                    
                    </c:otherwise>
                </c:choose>
                
                <a href="${pageContext.request.contextPath}/protected/admin?id=${user.id}&action=edit">${user.firstName} ${user.lastName}</a></td>
            <td>
                <fmt:formatDate value="${user.lastLogin}" type="both" dateStyle="short" timeStyle="short" /> 
            </td>
            <td>
                <c:forEach var="role" items="${user.roles}">
                ${roles[role].name}
                </c:forEach>            
            </td>
            <td>${user.email}</td>
            <td>${user.organization}</td>
            <td>
            <c:choose>
            <c:when test="${user.active == true}">
                Active
            </c:when>
            <c:otherwise>
                Inactive
            </c:otherwise>
            </c:choose>
            </td>
            <td>${user.title}</td>
            <td>${user.department}</td>
        </tr>
      </c:forEach>
      <tr>
      <td colspan="6">
      *Click on a user's name to edit his/her role(s) and other information
      </td>
      </tr>
    </table>
 </form>

    </template:content>

<template:content name="subcontent">
        <%@ include file="../common/rss.jspf" %>
    </template:content>

</template:insert>
