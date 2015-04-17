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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<template:insert template="/templates/eureka_main.jsp">
<template:content name="content">
<h3>Submit Job</h3>
<form id="uploadForm" name="uploadForm" role="form"
	  <c:if test="${not empty param.jobId}">data-jobid="${param.jobId}"</c:if>
	  <c:if test="${not empty requestScope.jobStatus and jobStatus.jobSubmitted}">data-job-running="true"</c:if>>
<fieldset>
	<legend>Job Information</legend>
	<div class="row">
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="sourceConfig">
					Source
				</label>
				<div id="sourceConfig">
						${sourceConfig}
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="destinationConfig">
					Destination
				</label>
				<div id="destinationConfig">
						${destination}
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="jobStatus">Job Status</label>
				<div id="jobStatus">
					<c:choose>
						<c:when test="${not empty requestScope.jobStatus}">
							${jobStatus.status}
						</c:when>
						<c:otherwise>
							No jobs have been submitted
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="startedDate">
					Started
				</label>
				<div id="startedDate">
					<c:if test="${not empty requestScope.jobStatus}">
						${jobStatus.startedDateFormatted}
					</c:if>
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="finishedDate">
					Finished
				</label>
				<div id="finishedDate">
					<c:if test="${not empty requestScope.jobStatus}">
						${jobStatus.finishedDateFormatted}
					</c:if>
				</div>
			</div>
		</div>
		<div class="col-md-2 col-sm-4">
			<div class="form-group">
				<label class="control-label" for="links">
					Links
				</label>
				<div id="links">
					<c:if test="${not empty requestScope.jobStatus}">
						<c:forEach var="link" items="${requestScope.jobStatus.links}">
							<a href="${link.url}">${link.displayName}</a><br>
						</c:forEach>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</fieldset>
<c:choose>
	<c:when test="${empty sources or empty destinations}">
		<p>No configurations are available.</p>
	</c:when>
	<c:otherwise>
			<div class="row">
				<div class="col-md-6">
					<fieldset id="data">
						<legend>Data</legend>
						<div class="form-group">
							<select name="source" class="form-control">
								<c:forEach var="source" items="${sources}">
									<option value="${source.id}">${not empty source.displayName ? source.displayName : source.id}</option>
								</c:forEach>
							</select>
						</div>
						<c:set var="required" value="${false}"/>
						<c:forEach var="source" items="${sources}">
							<div class="uploads" data-source-id="${source.id}">
								<c:forEach var="section" items="${source.dataSourceBackends}" varStatus="status">
									<div class="help-inline section" data-section-id="${section.id}">
										<c:forEach var="option" items="${section.options}">
											<c:choose>
												<c:when test="${option['class'].name == 'edu.emory.cci.aiw.cvrg.eureka.common.comm.UriSourceConfigOption'}">
													<a href="${option.value}">${option.displayName}</a>
												</c:when>
												<c:otherwise>
													<c:if test="${option.prompt}">
														<div data-index="${status.index}" data-option-name="${option.name}" class="uploader form-group">
															<c:choose>
																<c:when test="${option['class'].name == 'edu.emory.cci.aiw.cvrg.eureka.common.comm.FileSourceConfigOption'}">
																	<label>
																		<c:if test="${option.required}">
																			<span class="uploadRequired">*</span>
																			<c:set var="required" value="${true}"/>
																		</c:if>
																		${option.displayName}
																		<input type="file" class="form-control"
																		   name="${status.index}_${option.name}"
																		   <c:if test="${option.required}">data-required="true"</c:if>
																		   value="Browse"
																		   accept="${fn:join(option.acceptedMimetypes, '|')}">
																	</label>
																</c:when>
																<c:when test="${option.propertyType == 'INTEGER' or option.propertyType == 'LONG' or option.propertyType == 'DOUBLE' or option.propertyType == 'FLOAT'}">
																	<label>
																		<c:if test="${option.required}">
																			<span class="uploadRequired">*</span>
																			<c:set var="required" value="${true}"/>
																		</c:if>
																		${option.displayName}
																		<input type="number" class="form-control"
																		   name="${status.index}_${option.name}"
																		   <c:if test="${option.required}">data-required="true"</c:if>>
																	</label>
																</c:when>
																<c:otherwise>
																	<label>
																		<c:if test="${option.required}">
																			<span class="uploadRequired">*</span>
																			<c:set var="required" value="${true}"/>
																		</c:if>
																		${option.displayName}
																		<input type="text" class="form-control"
																		   name="${status.index}_${option.name}"
																		   <c:if test="${option.required}">data-required="true"</c:if>>
																	</label>
																</c:otherwise>
															</c:choose>
														</div>
													</c:if>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<c:if test="${required}">
											<div id="requiredFieldStmt">
												* indicates field is required.
											</div>
										</c:if>
									</div>
								</c:forEach>
							</div>
						</c:forEach>
					</fieldset>
				</div>
				<div class="col-md-6">
					<fieldset id="action">
						<legend>Action</legend>
						<div class="form-group">
							<select name="destination" class="form-control">
								<c:forEach var="destination" items="${destinations}">
									<option value="${destination.name}">${destination.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<div class="form-control">
								<label class="radio-inline">
									<input type="radio" id="appendDataFalse" name="appendData" value="false" checked>
									Replace data
								</label>
								<label class="radio-inline">
									<input type="radio" id="appendDataTrue" name="appendData" value="true">
									Append data
								</label>
							</div>
						</div>
					</fieldset>
				</div>
			</div>
		<fieldset id="dateRange">
			<legend>Date range</legend>
			<div class="row">
				<div class="col-xs-5">
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#systemElems" data-toggle="tab">System</a>
						</li>
						<li>
							<a href="#userElems" data-toggle="tab">User</a>
						</li>
					</ul>
					<div id="treeContent" class="tab-content proposition-tree">
						<div id="systemElems" class="tab-pane fade active in">
							<div id="systemTree"></div>
							<div id="searchUpdateDivJob" class="searchUpdateMessage"></div>
						</div>
						<div id="userElems" class="tab-pane fade">
							<div id="userTree"></div>
						</div>
					</div>
				</div>
				<div class="col-xs-7">
					<div class="row">
						<div class="col-xs-12">
							<div class="form-group">
								<label for="dateRangeDataElementKey">
									Data element for date range
								</label>
								<div id="dateRangeDataElementKey"
									 class="tree-drop tree-drop-single jstree-drop form-control-static">
									<div class="label-info text-center">
										Drop Here
									</div>
									<ul data-type="main" data-drop-type="single" class="sortable">
									</ul>
								</div>
								<input type="hidden" name="dateRangeDataElementKey">
							</div>
						</div>
					</div>
					<div class="row form-inline vert-offset">
						<div class="col-xs-12">
							<div class="form-group">
								<select name="dateRangeEarliestDateSide" class="form-control">
									<c:forEach var="dateRangeSide" items="${dateRangeSides}">
										<option value="${dateRangeSide.id}">${dateRangeSide.displayName}</option>
									</c:forEach>
								</select>
								<span class="dateRangeDataElementName"/>
								<label>
									has earliest date:
								</label>
								<input type="text" id="earliestDate" name="earliestDate" class="form-control">
							</div>
						</div>
					</div>
					<div class="row form-inline vert-offset">
						<div class="col-xs-12">
							<div class="form-group">
								<select name="dateRangeLatestDateSide" class="form-control">
									<c:forEach var="dateRangeSide" items="${dateRangeSides}">
										<option value="${dateRangeSide.id}">${dateRangeSide.displayName}</option>
									</c:forEach>
								</select>
								<span class="dateRangeDataElementName"/>
								<label>
									has latest date:
								</label>
								<input type="text" id="latestDate" name="latestDate" class="form-control">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12 text-center vert-offset-2x">
							<c:choose>
								<c:when test="${not required}">
									<input type="submit" id="startButton" class="btn btn-primary" value="Start">
								</c:when>
								<c:otherwise>
									<input type="submit" id="startButton" class="btn btn-primary" value="Start" disabled>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</fieldset>
	</c:otherwise>
</c:choose>
</form>
<div id="deleteModal" class="modal" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 id="deleteModalLabel" class="modal-title">
					Delete Element
				</h4>
			</div>
			<div id="deleteContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button id="confirmButton" type="button" class="btn btn-primary">Delete</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<div id="searchModal" class="modal" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 id="searchModalLabel" class="modal-title">
					Search
				</h4>
			</div>
			<div id="searchContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<div id="searchValidationModal" class="modal fade" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="searchVadlidationModalLabel" class="modal-title">
					Search String Validation Failed
				</h4>
			</div>
			<div id="searchValidationContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<div id="searchNoResultsModal" class="modal fade" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="searchNoResultsModalLabel" class="modal-title">
					No Search Results
				</h4>
			</div>
			<div id="searchNoResultsContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<div id="errorModal" class="modal fade" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="errorModalLabel" class="modal-title">Error</h4>
			</div>
			<div id="errorContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script language="JavaScript"
		src="${pageContext.request.contextPath}/assets/js/jquery.jstree.js"></script>
<script language="JavaScript"
		src="${pageContext.request.contextPath}/assets/js/eureka.tree${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript"
		src="${pageContext.request.contextPath}/assets/js/eureka.job${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript"
		src="${pageContext.request.contextPath}/assets/js/moment.min.js"></script>
<script language="JavaScript"
		src="${pageContext.request.contextPath}/assets/js/bootstrap-datetimepicker.min.js"></script>
<script language="JavaScript">
	eureka.job.setup('#systemTree', '#userTree', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css',
			'form#uploadForm', '#earliestDate', '#latestDate', '${pageContext.request.contextPath}/assets/css/bootstrap-datetimepicker.min.css',
			'#jobStatus', '#searchModal','#searchValidationModal', '#searchNoResultsModal','#searchUpdateDivJob');
</script>
</template:content>
</template:insert>

