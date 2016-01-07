<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
  %%
  This program is dual licensed under the Apache 2 and GPLv3 licenses.
  
  Apache License, Version 2.0:
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
  GNU General Public License version 3:
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
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
				<a href="editcohort" class="btn btn-primary"><span class="glyphicon glyphicon-plus-sign"></span>Define New Cohort
				</a>
				<table class="table table-responsive vert-offset">
					<tr>
						<th>Action</th>
						<th>Name</th>
						<th>Description</th>
						<th>Created Date</th>
						<th>Last Modified</th>
					</tr>
					<c:forEach items="${cohorts}" var="cohort">
						<c:url value="/protected/editcohort" var="editUrl">
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
		<div id="deleteModal" class="modal fade" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 id="deleteModalLabel" class="modal-title">
							Delete Element
						</h4>
					</div>
					<div id="deleteContent" class="modal-body">
					</div>
					<div class="modal-footer">
						<button id="deleteButton" type="button" class="btn btn-primary">Delete</button>
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
						<h4 id="errorModalLabel" class="modal-title">
							Error
						</h4>
					</div>
					<div id="errorContent" class="modal-body">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
		<script language="JavaScript">

			$('span.delete-icon').on('click', function () {
				var $tr = $(this).closest('tr');
				var displayName = $tr.data('display-name');
				var key = $tr.data('key');
				var dialog = $('#deleteModal');
				$(dialog).find('#deleteContent').html('Are you sure you want to remove cohort &quot;' + displayName.trim() + '&quot;?');
				$(dialog).find('#deleteButton').on('click', function (e) {
					$(dialog).modal('hide');
					$.ajax({
						type: "POST",
						url: 'deletecohort?key=' + encodeURIComponent(key),
						success: function (data) {
							window.location.href = 'cohorthome'
						},
						error: function (data, statusCode, errorThrown) {
							var content = 'Error while deleting &quot;' + displayName.trim() + '&quot;. ' + data.responseText + '. Status Code: ' + statusCode;
							$('#errorModal').find('#errorContent').html(content);
							$('#errorModal').modal('show');
							if (errorThrown != null) {
								console.log(errorThrown);
							}
						}
					});
				});
				$(dialog).modal("show");
			});
		</script>
	</template:content>
</template:insert>
