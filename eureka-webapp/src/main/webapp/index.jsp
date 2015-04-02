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
										<p>The software powering this site has been supported in part by <span id="support"></span></p>
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
