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
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader(
			"Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html>
<html>
<head>
	<title>Eureka! Clinical Analytics</title>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="keywords"
		  content="informatics, i2b2, biomedical, clinical research, research, de-identification, clinical data analysis, analytics, medical research, data analysis tool, clinical database, eureka!, eureka, scientific research, temporal patterns, bioinformatics, ontology, ontologies, ontology editor, data mining, etl, cvrg, CardioVascular Research Grid"/>
	<meta name="Description"
		  content="A Clinical Analysis Tool for Biomedical Informatics and Data"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<%--
		<link href="${pageContext.request.contextPath}/css/jquery-ui.css" type="text/css" rel="stylesheet"/>
		<link href="${pageContext.request.contextPath}/css/styles${initParam['eureka-build-timestamp']}.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" />
	--%>
	<link rel="SHORTCUT ICON"
		  href="${pageContext.request.contextPath}/favicon.ico">
	<link href="//fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,400italic,600italic,700italic"
		  rel="stylesheet" type="text/css">
	<link rel="stylesheet"
		  href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css"/>
	<link rel="stylesheet"
		  href="${pageContext.request.contextPath}/assets/css/eureka${initParam['eureka-build-timestamp']}.css"/>

	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
	<script src="${pageContext.request.contextPath}/assets/js/jquery-1.10.2.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
	<%--
		<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jstree.js"></script>
		<script src="${pageContext.request.contextPath}/js/eureka${initParam['eureka-build-timestamp']}.js"></script>
	--%>
</head>
<body>
<div class="navbar navbar-inverse navbar-static-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a href="${pageContext.request.contextPath}/" class="navbar-brand">
				<span class="brand-text">Eureka!</span>
			</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right menu-text">
				<li>
					<a href="${pageContext.request.contextPath}/about.jsp">
						<span class="glyphicon glyphicon-globe"></span>
						About
					</a>
				</li>
				<li>
					<c:if test="${not userIsActivated}">
						<a href="${pageContext.request.contextPath}/register.jsp">
							<span class="glyphicon glyphicon-user"></span>
							Register
						</a>
					</c:if>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/contact.jsp">
						<span class="glyphicon glyphicon-envelope"></span>
						Contact
					</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/help.jsp">
						<span class="glyphicon glyphicon-question-sign"></span>
						Help
					</a>
				</li>
				<c:choose>
					<c:when test="${pageContext.request.remoteUser != null}">
						<li>
							<a href="${pageContext.request.contextPath}/logout">
								<span class="glyphicon glyphicon-log-out"></span>
								Logout ${pageContext.request.remoteUser}
							</a>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<a href="${pageContext.request.contextPath}/protected/login">
								<span class="glyphicon glyphicon-log-in"></span>
								Login
							</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</div>
<c:if test="${userIsActivated}">
	<div class="navbar navbar-static-top" role="navigation">
		<div class="container sub-nav rounded-bottom">
			<ul class="nav navbar-nav navbar-right menu-text">
				<li>
					<a href="${pageContext.request.contextPath}/protected/cohortHome">
						<span class="glyphicon glyphicon-pencil"></span>
						Cohorts
					</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/protected/editorhome">
						<span class="glyphicon glyphicon-pencil"></span>
						Phenotypes
					</a>
				</li>
				<li>
					<a href="/i2b2/" target="_blank">
						<span class="glyphicon glyphicon-stats"></span>
						i2b2
					</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/protected/jobs">
						<span class="glyphicon glyphicon-cog"></span>
						Submit Job
					</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/protected/user_acct?action=list">
						<span class="glyphicon glyphicon-user"></span>
						Account Settings
					</a>
				</li>
				<c:if test="${pageContext.request.remoteUser != null and myfn:isUserInRole(pageContext.request, 'admin')}">
					<li>
						<a href="${pageContext.request.contextPath}/protected/admin?action=list">
							<span class="glyphicon glyphicon-wrench"></span>
							Administration
						</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>
</c:if>
<div class="container container-big">
	<template:get name="content"/>
</div>
<div class="footer">
	Copyright &copy; ${initParam['inception-year']}-${initParam['current-year']} ${initParam['eureka-organization-name']} -- All
	Rights Reserved
</div>
</body>
</html>
