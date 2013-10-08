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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<template:insert template="/templates/eureka_main2.jsp">

    <%--<template:content name="sidebar">
        <img
            src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>--%>

    <template:content name="content">

        <h3>Submit Job</h3>

		<form id="uploadForm" name="uploadForm" class="pad_top pad"
			  method="post" action="${pageContext.request.contextPath}/protected/upload"
			  ENCTYPE="multipart/form-data" 
			  <c:if test="${not empty param.jobId}">data-jobid="${param.jobId}"</c:if>
			  <c:if test="${not empty requestScope.jobStatus and jobStatus.jobSubmitted}">data-job-running="true"</c:if>
			  accept-charset="utf-8">

			<table>
				<tr>
					<td>Source</td>
					<td>Destination</td>
					<td>Job Status</td>
					<td>Status Date</td>
					<td>Warnings & Errors</td>
				</tr>
				<tr>
					<td>
						<div id="sourceConfig">
							${sourceConfig}
						</div>
					</td>
					<td>
						<div id="destination">
							${destination}
						</div>
					</td>
					<td>
						<div id="status">
							<c:choose>
								<c:when test="${not empty requestScope.jobStatus}">
									${jobStatus.status}
								</c:when>
								<c:otherwise>
									No jobs have been submitted
								</c:otherwise>
							</c:choose>
						</div>
					</td>
					<td>
						<div id="statusDate">
							<c:if test="${not empty requestScope.jobStatus}">
								${jobStatus.statusDate}
							</c:if>
						</div>
					</td>
					<td>
						<div id="messages">
							<c:if test="${not empty requestScope.jobStatus}">
								${jobStatus.firstMessage}
							</c:if>
						</div>
					</td>
				</tr>
			</table>

			<c:choose>
				<c:when test="${empty sources or empty destinations}">
					<p>No configurations are available.</p>
				</c:when>
				<c:otherwise>
					<fieldset id="configuration">
						<legend>Configuration</legend>
						<label>Source:<select name="source">
								<c:forEach var="source" items="${sources}">
									<option value="${source.id}">${source.name}</option>
								</c:forEach>
							</select>
						</label>
						<label>Destination:<select name="destination">
								<c:forEach var="destination" items="${destinations}">
									<option value="${destination.id}">${destination.displayName}</option>
								</c:forEach>
							</select>
						</label>
					</fieldset>
					<c:set var="required" value="${false}"/>
					<c:forEach var="source" items="${sources}">
						<c:if test="${not empty source.uploads}">
							<fieldset id="uploads${source.id}" class="uploads">
								<legend>File upload(s)</legend>
								<c:forEach var="upload" items="${source.uploads}">
									<div id="uploader${upload.sourceId}" class="uploader">
										<label>${upload.sourceId}: <input type="file" name="${upload.sourceId}" class="button browseButton"
																		  <c:if test="${upload.required}">data-required="true"</c:if>
																		  value="Browse" accept="${fn:join(upload.acceptedMimetypes, '|')}"/>
											<c:if test="${upload.required}">
												<span class="uploadRequired">*</span>
												<c:set var="required" value="${true}"/>
											</c:if>
											<c:if test="${not empty upload.sampleUrl}">
												<a href="${upload.sampleUrl}">Download Sample</a>
											</c:if>
										</label>
									</div>
								</c:forEach>
							</fieldset>
						</c:if>
					</c:forEach>
					<fieldset id="dateRange">
						<legend>Date range</legend>
						<span>
							<div class="tabs">
								<ul class="tabNavigation">
									<li><a href="#first">System</a></li>
									<li><a href="#second">User Defined</a></li>
								</ul>
								<div id="first">
									<div id="systemTree"></div>
								</div>
								<div id="second">
									<div id="userTree"></div>
								</div>
							</div>
						</span>
						<span>
							<table>
								<tr>
									<td>Data element for date range:</td>
									<td>
										<div id="dateRangeDataElementKey" class="tree-drop-single jstree-drop">
											<div class="label-info" ><center>Drop Here</center></div>
											<ul data-type="main" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
											</ul>
										</div>
										<input type="hidden" name="dateRangeDataElementKey">
									</td>
								</tr>
								<tr>
									<td><select name="dateRangeEarliestDateSide">
												<c:forEach var="dateRangeSide" items="${dateRangeSides}">
													<option value="${dateRangeSide.id}">${dateRangeSide.displayName}</option>
												</c:forEach>
											</select> <span class="dateRangeDataElementName"/> has earliest date:</td>
									<td><input type="text" id="earliestDate" name="earliestDate"></td>
								</tr>
								<tr>
									<td><select name="dateRangeLatestDateSide">
												<c:forEach var="dateRangeSide" items="${dateRangeSides}">
													<option value="${dateRangeSide.id}">${dateRangeSide.displayName}</option>
												</c:forEach>
											</select> <span class="dateRangeDataElementName"/> has latest date:</td>
									<td><input type="text" id="latestDate" name="latestDate"></td>
								</tr>
							</table>
						</span>
					</fieldset>
					<c:choose>
						<c:when test="${not required}">
							<input type="submit" id="button" class="button" value="Start"> 
						</c:when>
						<c:otherwise>
							<input type="submit" id="button" class="button" value="Start" disabled> 
						</c:otherwise>
					</c:choose>
					<c:if test="${required}">
						<div id="requiredFieldStmt">
							* indicates field is required.
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
		</form>
    </template:content>
    <%--<template:content name="subcontent">
        <div id="jobUpload">
            <h3>Please wait while your data is loading.....</h3>
            <img src="${pageContext.request.contextPath}/images/e-ani.gif"
				 hspace="450" align="middle" />
        </div>
    </template:content>--%>
</template:insert>

