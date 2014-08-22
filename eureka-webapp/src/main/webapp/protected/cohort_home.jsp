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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<div class="row">
			<div class="col-sm-12">
				<h3>Cohorts</h3>
				<p>Define a cohort to identify the patient population in your datasets.
				</p>
				<a href="${pageContext.request.contextPath}/protected/editCohort" class="btn btn-primary">Define New Cohort
				</a>
				<table class="table table-responsive vert-offset">
					<tr>
						<th>Action</th>
						<th>Name</th>
						<th>Description</th>
						<th>Currently in Use</th>
						<th>Created Date</th>
						<th>Last Modified</th>
						<th></th>
					</tr>
					<c:forEach items="${cohorts}" var="cohort">
						<c:url value="/protected/editCohort" var="editUrl">
							<c:param name="key" value="${cohort.attr['key']}"/>
						</c:url>
						<tr data-key="${cohort.attr['key']}"
							data-display-name="${cohort.attr['displayName']}">
							<td>
								<%--<span class="glyphicon glyphicon-eye-open view-icon" title="View"></span>--%>
								<a href="${editUrl}" title="Edit">
									<span class="glyphicon glyphicon-pencil edit-icon" title="Edit"></span>
								</a>
								<span class="glyphicon glyphicon-remove delete-icon" title="Delete"></span>
							</td>
							<td>${cohort.attr['displayName']}</td>
							<td>${cohort.attr['description']}</td>
							<td>${cohort.attr['createdDate']}</td>
							<td>${cohort.attr['lastModifiedDate']}</td>
							<%-- <td><a href="${pageContext.request.contextPath}/protected/editCohort" class="btn btn-primary"
							title="The generated patient list will be the patient population for the next job that is submitted through the Submit Job page.">Generate Patient List
							</a></td>--%>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</template:content>
</template:insert>