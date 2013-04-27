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

<template:insert template="/templates/eureka_main.jsp">

    <template:content name="sidebar">
        <img
            src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>

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
					<td width="252">Job Status</td>
					<td width="204">Status Date</td>
					<td width="151">Warnings & Errors</td>
				</tr>
				<tr>
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
					<div id="configuration">
						<p>Choose a configuration:</p>
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
					</div>
					<c:set var="required" value="${false}"/>
					<c:forEach var="source" items="${sources}">
						<c:if test="${not empty source.uploads}">
							<div id="uploads${source.id}" class="uploads">
								<p>Upload file(s):</p>
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
							</div>
						</c:if>
					</c:forEach>
					<!--<div id="dateRange">
						<p>Choose a date range:</p>
						<label>Earliest date:<input type="text" id="earliestDate" name="earliestDate"></label>
						<label>Latest date:<input type="text" id="latestDate" name="latestDate"></label>
					</div>-->
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
    <template:content name="subcontent">
        <div id="jobUpload">
            <h3>Please wait while your project is being uploaded.....</h3>
            <img src="${pageContext.request.contextPath}/images/e-ani.gif"
                hspace="450" align="middle" />
        </div>
    </template:content>
</template:insert>

