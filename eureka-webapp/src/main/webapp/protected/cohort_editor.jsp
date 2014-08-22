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
						<div id="systemTree"> The tree will be displayed here.</div>
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
									<li class="ui-state-default">${phenotype}</li>
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
					<div class="form-group text-centered vert-offset">
						<button id="savePropositionButton" type="button" class="btn btn-primary">Save</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	</template:content>
  </template:insert>