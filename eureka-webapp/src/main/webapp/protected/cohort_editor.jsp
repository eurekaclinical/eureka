<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2014 Emory University
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>
<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<h3>Cohort Editor</h3>
		<p>
			Select the system and user-defined elements from the ontology that will define the patient cohort.
		</p>
		<div class = "row">
			<div class="col-xs-4">
				<ul class="nav nav-tabs">
					<li class = "active">
						<a href = "#systemElems" data-toggle="tab">System</a>
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
			<div id="definitionContainer" class="col-xs-8">
				<form id="categorizationForm" class="vert-offset" role="form">
					<div class="form-group">
						<label for="patCohortDefName" class="control-label">Name</label>
						<input type="text" id="patCohortDefName" value="${name}"
							   class="form-control"/>
					</div>
					<div class="form-group">
						<label for="patCohortDescription" class="control-label">Description</label>
						<textarea id="patCohortDescription" class="form-control">${description}</textarea>
					</div>
					<div class="form-group">
						<label for="patCohortDefinition" class="control-label">Members</label>
						<div id="patCohortDefinition"
							 class="jstree-drop tree-drop tree-drop-multiple"
							 title="Drop the system and/or user-defined data element members that define your patient cohort in here">
							<ul class="sortable" data-drop-type="multiple" data-proptype="empty">
								<c:forEach var="phenotype" items="${phenotypes}">
									<li data-key="${phenotype.dataElementKey}"
										data-desc="${phenotype.dataElementDisplayName}" data-type="${phenotype.type}"
										data-subtype="${phenotype.type == 'CATEGORIZATION' ? phenotype.categoricalType : ''}"
										>
										<span class="glyphicon glyphicon-remove delete-icon"
											  title="Remove this phenotype from the category"></span><span>${phenotype.dataElementDisplayName} (${phenotype.dataElementKey})</span>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<!--<div class="form-group">
						<div class="row">
							<div class="col-xs-5">
								<label for="earliestDate" class="control-label">Earliest Date</label>
							</div>
						</div>
						<div class="row">
						<div class="col-xs-5">
							<input type="text" id="earliestDate" name="earliestDate" class="form-control">
						</div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-xs-5">
								<label for="latesttDate" class="control-label">Latest Date</label>
							</div>
						</div>
						<div class="row">
						<div class="col-xs-5">
							<input type="text" id="latesttDate" name="latesttDate" class="form-control">
						</div>
						</div>
					</div>-->
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
				src="${pageContext.request.contextPath}/assets/js/eureka.tree${initParam['eureka-build-timestamp']}.js"></script>
		<script language="JavaScript"
				src="${pageContext.request.contextPath}/assets/js/eureka.cohort${initParam['eureka-build-timestamp']}.js"></script>
		<script language="JavaScript">
			eureka.editor.setup(${destId != null ? destId : 'null'},
					'#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
					'ul.sortable', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css', '#searchModal',
					'#searchValidationModal','#searchNoResultsModal','#searchUpdateDiv');
		</script>
	</template:content>
</template:insert>