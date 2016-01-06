<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2014 Emory University
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>

<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<h3>Categorization Editor</h3>
		<p>
			This category data element may be used wherever its member data elements are accepted.
		</p>

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
						<div id="searchUpdateDiv" class="searchUpdateMessage"></div>
					</div>
					<div id="userElems" class="tab-pane fade">
						<div id="userTree"></div>
					</div>
				</div>
			</div>
			<div id="definitionContainer" class="col-xs-7">
				<form id="categorizationForm" class="vert-offset" role="form">
					<div class="form-group">
						<label for="propDisplayName" class="control-label">Name</label>
						<input type="text" id="propDisplayName" value="${proposition.displayName}"
							   class="form-control"/>
					</div>
					<div class="form-group">
						<label for="propDescription" class="control-label">Description</label>
						<textarea id="propDescription" class="form-control"><c:if
								test="${not empty proposition}">${proposition.description}</c:if></textarea>
					</div>
					<div class="form-group">
						<label for="propDefinition" class="control-label">Members</label>
						<div id="propDefinition"
							 class="jstree-drop tree-drop tree-drop-multiple"
							 title="Drop your category's system and/or user-defined data element members in here">
							<div class="label-info text-center">
								Drop Here
							</div>
							<ul class="sortable" data-drop-type="multiple" data-proptype="empty">
								<c:if test="${not empty proposition}">
									<c:forEach var="child" items="${proposition.children}">
										<li data-key="${child.dataElementKey}"
                                                                                    data-desc="${child.dataElementDisplayName}" data-type="${child.type}"
                                                                                    data-subtype="${child.type == 'CATEGORIZATION' ? child.categoricalType : ''}"
                                                                                    data-space="${proposition.inSystem ? 'system' : 'user'}">
                                                                                    <span class="glyphicon glyphicon-remove delete-icon"
                                                                                          title="Remove this data element from the category">
                                                                                    </span>
                                                                                    <span>${child.dataElementDisplayName} (${child.dataElementKey})</span>
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
					</div>
					<div class="form-group text-center vert-offset">
							<button id="savePropositionButton" type="button" class="btn btn-primary">Save</button>
					</div>
				</form>
			</div>
		</div>
		<div id="deleteModal" class="modal fade" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 id="deleteModalLabel" class="modal-title">Delete Element</h4>
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
	<div id="replaceModal" class="modal fade" role="dialog" aria-labelledby="replaceModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 id="replaceModalLabel" class="modal-title">Replace Element</h4>
				</div>
				<div id="replaceContent" class="modal-body">
				</div>
				<div class="modal-footer">
					<button id="replaceButton" type="button" class="btn btn-primary">Replace</button>
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
		<div id="searchModal" class="modal fade" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 id="searchModalLabel" class="modal-title">
							Broad Search Update
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
        				<h4 id="searchModalLabel" class="modal-title">
        					Search String Validation Failed
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
        <div id="searchNoResultsModal" class="modal fade" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
        	<div class="modal-dialog modal-lg">
        		<div class="modal-content">
        			<div class="modal-header">
        				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        				<h4 id="searchModalLabel" class="modal-title">
        					No Search Results
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
		<script language="JavaScript"
				src="${pageContext.request.contextPath}/assets/js/jquery.jstree.js"></script>
		<script language="JavaScript"
				src="${pageContext.request.contextPath}/assets/js/eureka.tree-phenotype${initParam['eureka-build-timestamp']}.js"></script>
		<script language="JavaScript"
				src="${pageContext.request.contextPath}/assets/js/eureka.editor${initParam['eureka-build-timestamp']}.js"></script>
		<script language="JavaScript">
			eureka.editor.setup('CATEGORIZATION', '${propositionType == 'CATEGORIZATION' ? proposition.categoricalType : ''}',
					${proposition != null ? proposition.id : 'null'},
					'#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
					'ul.sortable', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css', '#searchModal',
					'#searchValidationModal','#searchNoResultsModal','#searchUpdateDiv');
		</script>
	</template:content>
</template:insert>
