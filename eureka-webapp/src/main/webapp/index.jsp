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
    <link href="//fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,400italic,600italic,700italic"
          rel="stylesheet" type="text/css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/eureka.css"/>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <script src="app/js/jquery-2.1.3.min.js"></script>
    <script src="app/js/bootstrap.min.js"></script>
    <script src="app/js/angular.js"></script>
    <script src="app/js/angular-route.js"></script>
    <script src="app/js/angular-messages.js"></script>
    <script src="app/js/angular-validator.min.js"></script>
    <script src="app/eurekaApp.js"></script>
    <script src="app/eurekaApp.routes.js"></script>

    <script src="assets/js/eureka.cohort.js"></script>

    <script src="app/services/registerService.js"></script>
    <script src="app/services/cohortService.js"></script>
    <script src="app/services/editorService.js"></script>
    <script src="app/controllers/mainController.js"></script>
    <script src="app/controllers/cohortController.js"></script>
    <script src="app/controllers/cohortEditController.js"></script>
    <script src="app/controllers/editorController.js"></script>
    <script src="app/controllers/registerController.js"></script>
    <script src="assets/js/jquery.jstree.js"></script>
</head>
<body ng-app="eurekaApp">
<div class="navbar navbar-inverse navbar-static-top" role="navigation" ng-controller="MainController">
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


<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<div class="jumbotron">
			<img id="logo" class="img-responsive" src="${pageContext.request.contextPath}/assets/images/logo.png"/>
			<p class="vert-offset text-center">
				Access to clinical data for quality improvement and research, simplified.
			</p>
		</div>
		<div id="indexPanels">
			<c:choose>
				<c:when test="${applicationScope.webappProperties.demoMode}">
					<div class="container-fluid">
						<div class="row">
							<div class="alert alert-warning">NOTE: This demonstration web site is NOT suitable for
								use with sensitive data including patient data that contains
								identifiers.
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="panel panel-info">
									<c:choose>
										<c:when test="${not userIsActivated}">
											<div class="panel-heading">Want to try it out?</div>
											<div class="panel-body text-center">
												<div class="container-fluid">
													<div class="row">
														<c:choose>
															<c:when test="${applicationScope.webappProperties.registrationEnabled}">
																<div class="col-xs-6">
																	<h4>Learn about Eureka!</h4>
																	<a href="${initParam['aiw-site-url']}/overview.html" target="_blank" 
																	   class="btn btn-primary btn-lg">
																		About Eureka!
																	</a>
																</div>
																<div class="col-xs-6">
																	<h4>Get an account</h4>
																	<a href="${pageContext.request.contextPath}/chooseaccounttype"
																	   class="btn btn-primary btn-lg">
																		Register
																	</a>
																</div>
															</c:when>
															<c:otherwise>
																<div class="col-xs-12">
																	<h4>Learn about Eureka!</h4>
																	<a href="${initParam['aiw-site-url']}/overview.html" target="_blank" 
																	   class="btn btn-primary btn-lg">
																		About Eureka!
																	</a>
																</div>
															</c:otherwise>
														</c:choose>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="panel-heading">Want to learn more?</div>
											<div class="panel-body text-center">
												<div class="container-fluid">
													<div class="row">
														<div class="col-xs-12">
															<h4>Learn more about Eureka!</h4>
															<a href="${initParam['aiw-site-url']}/overview.html" target="_blank" 
															   class="btn btn-primary btn-lg">
																About Eureka!
															</a>
														</div>
													</div>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel panel-info">
									<div class="panel-heading">Want to deploy Eureka! at your institution or lab?</div>
									<div class="panel-body text-center">
										<div class="container-fluid">
											<div class="row">
												<div class="col-xs-6">
													<h4>Get your own copy</h4>
													<a href="${initParam['aiw-site-url']}/get-it.html" target="_blank" 
													   class="btn btn-primary btn-lg">
														Download Eureka!
													</a>
												</div>
												<div class="col-xs-6">
													<h4>Contact us for help</h4>
													<a href="${initParam['aiw-site-url']}/contact.html" target="_blank"
													   class="btn btn-primary btn-lg">
														Contact Us
													</a>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="panel panel-info">
									<div class="panel-heading">News</div>
									<div class="panel-body" id="versionHistory">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel panel-info">
									<div class="panel-heading">Funding</div>
									<div class="panel-body text-center">
										<p>The software powering this site has been supported in part by <span id="support"></span></p>
									</div>
								</div>
							</div>
						</div>
						<div id="versionHistory"></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="container-fluid">
						<c:if test="${applicationScope.webappProperties.ephiProhibited}">
							<div class="row">
								<div class="alert alert-warning">NOTE: This application is NOT suitable for
									use with sensitive data including patient data that contains
									identifiers.
								</div>
							</div>
						</c:if>
						<div class="row">
							<div class="col-md-6">
								<div class="panel panel-info">
									<c:choose>
										<c:when test="${not userIsActivated}">
											<div class="panel-heading">Request an Eureka! account</div>
											<div class="panel-body text-center">
												<div class="container-fluid">
													<div class="row">
														<c:choose>
															<c:when test="${applicationScope.webappProperties.registrationEnabled}">
																<div class="col-xs-6">
																	<h4>Learn about Eureka!</h4>
																	<a href="${initParam['aiw-site-url']}/overview.html" target="_blank" 
																	   class="btn btn-primary btn-lg">
																		About Eureka!
																	</a>
																</div>
																<div class="col-xs-6">
																	<h4>Get an account</h4>
																	<a href="${pageContext.request.contextPath}/chooseaccounttype"
																	   class="btn btn-primary btn-lg">
																		Register
																	</a>
																</div>
															</c:when>
															<c:otherwise>
																<div class="col-xs-12">
																	<h4>Learn about Eureka!</h4>
																	<a href="${initParam['aiw-site-url']}/overview.html" target="_blank" 
																	   class="btn btn-primary btn-lg">
																		About Eureka!
																	</a>
																</div>
															</c:otherwise>
														</c:choose>
													</div>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="panel-heading">News</div>
											<div class="panel-body" id="versionHistory">
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-md-6">
								<div class="panel panel-info">
									<div class="panel-heading">Funding</div>
									<div class="panel-body text-center">
										<p>The software powering this site has been supported in part by <span id="support"></span>. Any publication(s), invention disclosures, patents, etc. that result from a project utilizing Eureka! should cite this grant support.</p>
									</div>
								</div>
							</div>

						</div>
					</div>
				</c:otherwise>
			</c:choose>

		</div>
		<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/eureka.util${initParam['eureka-build-timestamp']}.js"></script>
		<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/js/eureka.index${initParam['eureka-build-timestamp']}.js"></script>
		<script type="text/javascript">
			$(document).ready(function () {
				eureka.index.setup("${pageContext.request.contextPath}");
				eureka.index.writeSupport();
				eureka.index.writeVersionHistory();
			});
		</script>
	</template:content>
</template:insert>
