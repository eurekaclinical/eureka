<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2015 Emory University
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
				<h3>Phenotype Editor</h3>
				<p>Specify the phenotypes that you want to compute in your
					datasets below.
					Phenotypes are patient features inferred from sequence,
					frequency and other temporal patterns in the events and
					observations in your dataset.
					These features are computed as intervals with a start time
					and a stop time representing when they are present.
				</p>

				<div id="dialog" title="Delete Data Element"></div>
				<div class="btn-group">
					<div class="btn-group">
						<button id="typeDropdown" class="btn btn-primary" data-toggle="dropdown">
							<span class="glyphicon glyphicon-plus-sign"></span>
							Create New Element
						</button>
						<ul class="dropdown-menu" role="menu" aria-labelledby="typeDropdown">
							<li>
								<a href="${pageContext.request.contextPath}/protected/editprop?type=categorization">
									<dt>
										Categorization
									</dt>
									<dd>
										<fmt:message key="dataelementtype.CATEGORIZATION.description"/>
									</dd>
								</a>
							</li>
							<li>
								<a href="${pageContext.request.contextPath}/protected/editprop?type=sequence">
									<dt>
										Sequence
									</dt>
									<dd>
										<fmt:message key="dataelementtype.SEQUENCE.description"/>
									</dd>
								</a>
							</li>
							<li>
								<a href="${pageContext.request.contextPath}/protected/editprop?type=frequency">
									<dt>
										Frequency
									</dt>
									<dd>
										<fmt:message key="dataelementtype.FREQUENCY.description"/>
									</dd>
								</a>
							</li>
							<li>
								<a href="${pageContext.request.contextPath}/protected/editprop?type=value_threshold">
									<dt>
										Value Threshold
									</dt>
									<dd>
										<fmt:message key="dataelementtype.VALUE_THRESHOLD.description"/>
									</dd>
								</a>
							</li>
						</ul>
					</div>
					<a class="btn btn-default" href="${initParam['eureka-help-url']}/phenotypes.html#select-type"
					   target="eureka-help">
						<span class="glyphicon glyphicon-question-sign"></span>
					</a>
				</div>
				<table class="table table-responsive vert-offset">
					<tr>
						<th>Action</th>
						<th>Name</th>
						<th>Description</th>
						<th>Type</th>
						<th>Created Date</th>
						<th>Last Modified</th>
					</tr>
					<c:forEach items="${props}" var="prop">
						<c:url value="/protected/editprop" var="editUrl">
							<c:param name="key" value="${prop.attr['key']}"/>
						</c:url>
						<tr data-key="${prop.attr['key']}"
							data-display-name="${prop.attr['displayName']}">
							<td>
								<%--<span class="glyphicon glyphicon-eye-open view-icon" title="View"></span>--%>
								<a href="${editUrl}" title="Edit">
									<span class="glyphicon glyphicon-pencil edit-icon" title="Edit"></span>
								</a>
								<span class="glyphicon glyphicon-remove delete-icon" title="Delete"></span>
							</td>
							<td>${prop.attr['displayName']}</td>
							<td>${prop.attr['description']}</td>
							<td>${prop.attr['type']}</td>
							<td>${prop.attr['created']}</td>
							<td>${prop.attr['lastModified']}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<%--<div class="col-sm-2">--%>
				<%--<div class="tooltip" id="tooltip" style="text-align: left">--%>
					<%--<div id="tree">--%>
					<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
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
				$(dialog).find('#deleteContent').html('Are you sure you want to remove data element &quot;' + displayName.trim() + '&quot;?');
				$(dialog).find('#deleteButton').on('click', function (e) {
					$(dialog).modal('hide');
					$.ajax({
						type: "POST",
						url: 'deleteprop?key=' + encodeURIComponent(key),
						success: function (data) {
							window.location.href = 'editorhome'
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
