<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2015 Emory University
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
<html ng-app="eurekaApp">
<head>
    <title>Eureka! Clinical Analytics</title>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="keywords"
          content="informatics, i2b2, biomedical, clinical research, research, de-identification, clinical data analysis, analytics, medical research, data analysis tool, clinical database, eureka!, eureka, scientific research, temporal patterns, bioinformatics, ontology, ontologies, ontology editor, data mining, etl, cvrg, CardioVascular Research Grid"/>
    <meta name="Description"
          content="A Clinical Analysis Tool for Biomedical Informatics and Data"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/normalize.css"/>
    <link rel="SHORTCUT ICON"
          href="${pageContext.request.contextPath}/favicon.ico">
    <link href="//fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,400italic,600italic,700italic"
          rel="stylesheet" type="text/css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/bootstrap-3.3.4-dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/font-awesome-4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/bootstrap-social-20150401.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/eureka${initParam['eureka-build-timestamp']}.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/jstree-themes/default-3.1.1/style.css"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

    <script src="${pageContext.request.contextPath}/assets/js/jquery-2.1.3.min.js" type="text/javascript"></script>
    <script src="assets/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>
    <script src="app/js/angular.js"></script>
    <script src="app/js/angular-route.js"></script>
    <script src="app/js/angular-messages.js"></script>
    <script src="app/js/angular-validator.min.js"></script>
    <script src="app/eurekaApp${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/eurekaApp.routes${initParam['eureka-build-timestamp']}.js"></script>

    <script src="assets/js/eureka.tree-cohort${initParam['eureka-build-timestamp']}.js"></script>
    <script src="assets/js/eureka.cohort${initParam['eureka-build-timestamp']}.js"></script>

    <script src="app/services/registerService${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/services/cohortService${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/services/editorService${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/mainController${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/cohortController${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/cohortEditController${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/editorController${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/registerController${initParam['eureka-build-timestamp']}.js"></script>
    <script src="app/controllers/tabController${initParam['eureka-build-timestamp']}.js"></script>
    <%--<script src="assets/js/jquery.jstree.js"></script>--%>
    <script src="assets/js/jstree-3.1.1.min.js"></script>
</head>
<body ng-app="eurekaApp">
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
                <c:if test="${userIsActivated}">
                    <li>
                        <a href="${pageContext.request.contextPath}/#/cohort_home" class="btn btn-lg">
                            <span class="glyphicon glyphicon-pencil"></span>
                            Cohorts
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/protected/editorhome" class="btn btn-lg">
                            <span class="glyphicon glyphicon-pencil"></span>
                            Phenotypes
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/protected/jobs">
                            <span class="glyphicon glyphicon-cog"></span>
                            Jobs
                        </a>
                    </li>
                </c:if>
                <li>
                    <a href="${pageContext.request.contextPath}/help.jsp">
                        <span class="glyphicon glyphicon-question-sign"></span>
                        Help
                    </a>
                </li>
                <c:choose>
                    <c:when test="${userIsActivated}">
                        <li>
                            <a href="${pageContext.request.contextPath}/logout"
                               class="dropdown-toggle" data-toggle="dropdown"
                               role="button" aria-expanded="false">
                                <span class="glyphicon glyphicon-user"></span>
                                    ${pageContext.request.remoteUser} <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu" role="menu">
                                <li>
                                    <a href="${pageContext.request.contextPath}/protected/user_acct?action=list">
                                        <span class="glyphicon glyphicon-user"></span>
                                        Account Settings
                                    </a>
                                </li>
                                <c:if test="${myfn:isUserInRole(pageContext.request, 'admin')}">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/protected/admin?action=list">
                                            <span class="glyphicon glyphicon-wrench"></span>
                                            Administration
                                        </a>
                                    </li>
                                </c:if>
                                <li><a href="${pageContext.request.contextPath}/logout" class="idletimeout-logout">
                                    <span class="glyphicon glyphicon-log-out"></span>
                                    Logout
                                </a></li>
                            </ul>

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
<div class="container container-big">
    <ng-view></ng-view>
</div>
<div class="footer">
    Copyright &copy; All rights reserved.
</div>
</body>
</html>



