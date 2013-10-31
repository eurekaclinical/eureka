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
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="informatics, i2b2, biomedical, clinical research, research, de-identification, clinical data analysis, analytics, medical research, data analysis tool, clinical database, eureka!, eureka, scientific research, temporal patterns, bioinformatics, ontology, ontologies, ontology editor, data mining, etl, cvrg, CardioVascular Research Grid" />

<title>Eureka! Clinical Analytics</title>

<!--[if lte IE 7]>
<link href="${pageContext.request.contextPath}/css/ie7.css" rel="stylesheet" type="text/css">
<style>
.container { width:1024px; margin: 0 auto;}
ul.nav a { zoom: 1; }
#submit { border:none;}
</style>
<![endif]-->



<link href="${pageContext.request.contextPath}/css/jquery-ui.css" type="text/css" rel="stylesheet"/>
<link href="${pageContext.request.contextPath}/css/styles${initParam['eureka-build-timestamp']}.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js"></script>
<script src="${pageContext.request.contextPath}/js/eureka${initParam['eureka-build-timestamp']}.js"></script>
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" />



<link rel="SHORTCUT ICON" href="${pageContext.request.contextPath}/favicon.ico">
<meta name="Description" content="A Clinical Analysis Tool for Biomedical Informatics and Data" />


</head>

<body style="overflow: auto">

<div class="container">
<div>
<div class="header">
    <span><a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/images/tag_line.gif" alt="Data Analysis Tool" width="238" align="absmiddle" /></a></span>
</div>

 <div class="nav">
  <ul class="left-nav">
      <li><a href="${pageContext.request.contextPath}/about.jsp"><img src="${pageContext.request.contextPath}/images/about_icon.gif" alt="About" width="30" height="30" align="absmiddle" />About</a></li>
      <c:choose>
        <c:when test="${pageContext.request.remoteUser == null}">
          <li><a href="${pageContext.request.contextPath}/register.jsp"><img src="${pageContext.request.contextPath}/images/reg_icon.gif" alt="Register" width="30" height="30" align="absmiddle" />Register</a></li>
        </c:when>
      </c:choose>
      <c:choose>
        <c:when test="${pageContext.request.remoteUser != null}">
          <li><a href="${pageContext.request.contextPath}/protected/user_acct?action=list"><img src="${pageContext.request.contextPath}/images/acct_icon.gif" alt="Account" width="30" height="30" align="absmiddle" />Account</a></li>
        </c:when>
      </c:choose>

      <li><a href="${pageContext.request.contextPath}/contact.jsp"><img src="${pageContext.request.contextPath}/images/contact_icon.gif" alt="Contact" width="30" height="30" align="absmiddle" />Contact</a></li>
      <li><a href="${pageContext.request.contextPath}/help.jsp"><img src="${pageContext.request.contextPath}/images/help_icon.gif" alt="Help" width="30" height="30" align="absmiddle" />Help</a></li>

 	<c:if test="${pageContext.request.remoteUser != null}">
 		<c:if test="${myfn:isUserInRole(pageContext.request, 'admin')}">
			<li><a href="${pageContext.request.contextPath}/protected/admin?action=list"><img src="${pageContext.request.contextPath}/images/admin_icon.gif" alt="Administration" width="30" height="30" align="absmiddle" />Administration</a></li>
        </c:if>
 	  </c:if>
  </ul>
	  <ul class="right-nav">
	  <c:choose>
	  	<c:when test="${pageContext.request.remoteUser != null}">
	  	  <li>Welcome, <span id="welcomeUsername">${pageContext.request.remoteUser}</span> | <a href="${pageContext.request.contextPath}/logout">Logout</a></li>
      	  <li><a href="${pageContext.request.contextPath}/protected/editorhome">Editor</a></li>
	      <li><a href="/i2b2/" target="_blank"><img src="${pageContext.request.contextPath}/images/i2b2_icon.gif" alt="i2b2" width="30" height="30" align="absmiddle" />i2b2</a></li>
	      <li><a href="${pageContext.request.contextPath}/protected/jobs"><img src="${pageContext.request.contextPath}/images/rsch_icon.gif" alt="Submit Job" width="30" height="30" align="absmiddle" />Submit Job</a></li>
	  	</c:when>
	  	<c:otherwise>
	      <li><a href="${pageContext.request.contextPath}/protected/login"><img src="${pageContext.request.contextPath}/images/login_icon.gif" alt="Login" width="30" height="30" align="absmiddle" />Login</a></li>   <%--</div>--%>
	  	</c:otherwise>
	  </c:choose>
    </ul>
  </div>

  <div class="sidebar1">
	<template:get name="sidebar" />
  </div>

  <div class="content-sidebar">
		<template:get name="content" />
  </div>

  <%--<div class="sub-content">
		<template:get name="subcontent" />
  </div>--%>

  <div class="footer">
    <p>Copyright &copy; 2012-2013 ${initParam['eureka-organization-name']} -- All Rights Reserved</p>
  </div>
  <!-- end .container -->
</div>
</div>
</body>
</html>
