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
<h3>Value Threshold Editor</h3>
<p>
	Computes intervals corresponding to when the specified thresholds below are present.
</p>

<div class="row">
<div class="col-xs-4">
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
<div id="definitionContainer" class="col-xs-8">
<form id="valueThresholdForm" class="form-horizontal vert-offset" role="form">
<div class="form-group">
	<label for="propDisplayName">Name</label>
	<input type="text" id="propDisplayName" value="${proposition.displayName}"
		   class="form-control"/>
</div>
<div class="form-group">
	<label for="propDescription">Description</label>
	<textarea id="propDescription" class="form-control"><c:if
			test="${not empty proposition}">${proposition.description}</c:if></textarea>
</div>
<div class="form-group">
	<label for="valueThresholdType">Value threshold type</label>
	<select id="valueThresholdType" name="valueThresholdType" id="valueThresholdType" class="form-control">
		<c:forEach var="operator" items="${thresholdsOperators}">
			<option value="${operator.id}"
					<c:if test="${propositionType == 'VALUE_THRESHOLD' and proposition.thresholdsOperator == operator.id}">selected="true"</c:if>>${operator.description}</option>
		</c:forEach>
	</select>
</div>
<div class="form-group">
<div id="propDefinition" class="value-thresholds-container">
<c:choose>
<c:when test="${not empty proposition and propositionType == 'VALUE_THRESHOLD' and not empty proposition.valueThresholds}">
	<c:forEach var="threshold" items="${proposition.valueThresholds}" varStatus="status">
		<div class="value-threshold col-sm-12 panel panel-default">
			<div class="row panel-heading"><h4 class="panel-title">Value Threshold <span class="count">${status.count}</span></h4></div>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="sr-only" for="thresholdedDataElement${status.count}">Thresholded data element ${status.count}</label>
						<div id="thresholdedDataElement${status.count}"
							 class="tree-drop tree-drop-single jstree-drop thresholdedDataElement"
							 title="Drag and drop the system or user-defined data element with a numerical value to be thresholded">
							<div class="label-info text-center">
								Drop Thresholded Data Element Here
							</div>
							<ul data-type="threshold1" data-drop-type="single" class="sortable">
								<li data-key="${threshold.dataElement.dataElementKey}" data-desc="${threshold.dataElement.dataElementDisplayName}" data-space="${threshold.dataElement.inSystem ? 'system' : 'user'}">
									<span class="delete-icon glyphicon glyphicon-remove"></span>
									<span class="desc">${empty threshold.dataElement.dataElementDisplayName ? '' : threshold.dataElement.dataElementDisplayName} (${threshold.dataElement.dataElementKey})</span>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="form-group"
					 title="Specify lower and/or upper bounds on the value of the data element to be thresholded">
					<div class="col-md-2">
						<label class="sr-only" for="thresholdLowerComp">From</label>
						<select class="form-control" name="thresholdLowerComp" id="thresholdLowerComp">
							<c:forEach var="valComp" items="${valueComparatorsLower}">
								<option value="${valComp.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and valComp.id == threshold.lowerComp}">selected="selected"</c:if>>${valComp.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="thresholdLowerVal">From value</label>
						<input class="form-control" type="text"
							   name="thresholdLowerVal" id="thresholdLowerVal"
							   value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.lowerValue}</c:if>" />
					</div>
					<div class="col-md-1 control-label" style="text-align: center">
						<label for="thresholdUpperComp">to</label>
					</div>
					<div class="col-md-2">
						<select class="form-control" name="thresholdUpperComp" id="thresholdUpperComp">
							<c:forEach var="valComp" items="${valueComparatorsUpper}">
								<option value="${valComp.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and valComp.id == threshold.upperComp}">selected="selected"</c:if>>${valComp.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="thresholdUpperVal">To value</label>
						<input class="form-control" type="text" class="valueField"
							   name="thresholdUpperVal" id="thresholdUpperVal"
							   value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.upperValue}</c:if>" />
					</div>
				</div>
			<fieldset>
				<legend>Context(s)</legend>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="sr-only" for="thresholdRelatedDataElements${status.count}">Contextual data element ${status.count}</label>
						<div id="thresholdRelatedDataElements${status.count}"
							 class="tree-drop tree-drop-multiple jstree-drop thresholdedRelatedDataElements"
							 title="Drag and drop a contextual system or user-defined data element, an interval of which must be present to match this threshold">
							<div class="label-info text-center">
								Drop Contextual Data Element(s) Here
							</div>
							<ul data-proptype="empty" data-drop-type="multiple" class="sortable">
								<c:forEach var="relatedDataElement" items="${threshold.relatedDataElements}">
									<li data-key="${relatedDataElement.dataElementKey}" data-desc="${relatedDataElement.dataElementDisplayName}" data-type="${relatedDataElement.type}" data-subtype="${relatedDataElement.type == 'CATEGORIZATION' ? relatedDataElement.categoricalType : ''}" data-space="${relatedDataElement.inSystem ? 'system' : 'user'}">
										<span class="delete-icon glyphicon glyphicon-remove"></span>
										<span>${relatedDataElement.dataElementDisplayName} (${relatedDataElement.dataElementKey})</span>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-2">
						<label class="sr-only" for="thresholdWithinAtLeast">From</label>
						<input type="text" class="form-control"
							   name="thresholdWithinAtLeast"
							   id="thresholdWithinAtLeast"
							   placeholder="min"
							   value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.withinAtLeast}</c:if>" />
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="thresholdWithinAtLeastUnits">From units</label>
						<select name="thresholdWithinAtLeastUnits" id="thresholdWithinAtLeastUnits" class="form-control">
							<c:forEach var="unit" items="${timeUnits}">
								<option value="${unit.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and unit.id == threshold.withinAtLeastUnit}">selected="selected"</c:if>>${unit.description}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-1 control-label" style="text-align: center">
						<label for="thresholdWithinAtMost">to</label>
					</div>
					<div class="col-md-2">
						<input type="text" class="form-control"
							   name="thresholdWithinAtMost" id="thresholdWithinAtMost"
							   placeholder="max"
							   value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.withinAtMost}</c:if>" />
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="thresholdWithinAtMostUnits">To units</label>
						<select name="thresholdWithinAtMostUnits" id="thresholdWithinAtMostUnits" class="form-control">
							<c:forEach var="unit" items="${timeUnits}">
								<option value="${unit.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and unit.id == threshold.withinAtMostUnit}">selected="selected"</c:if>>${unit.description}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="sr-only" for="thresholdDataElementTemporalRelation">Temporal relation</label>
						<select name="thresholdDataElementTemporalRelation"
								id="thresholdDataElementTemporalRelation"
								class="form-control"
								style="vertical-align: middle"
								title="Specify a temporal relationship constraint between intervals of the data element matching the threshold and this contextual data element">
							<c:forEach var="op" items="${contextRelationOps}">
								<option value="${op.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and op.id == threshold.relationOperator}">selected="selected"</c:if>>${op.description}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</fieldset>
		</div>
	</c:forEach>
</c:when>
<c:otherwise>
<div class="value-threshold col-sm-12 panel panel-default">
	<div class="row panel-heading"><h4 class="panel-title">Value Threshold <span class="count">1</span></h4></div>
		<div class="form-group">
			<div class="col-sm-12">
				<label class="sr-only" for="thresholdedDataElement1">Thresholded data element 1</label>
				<div id="thresholdedDataElement1"
					 class="tree-drop tree-drop-single jstree-drop thresholdedDataElement"
					 title="Drag and drop the system or user-defined data element with a numerical value to be thresholded">
					<div class="label-info text-center">
						Drop Thresholded Data Element Here
					</div>
					<ul data-type="threshold1" data-drop-type="single" class="sortable">
					</ul>
				</div>
			</div>
		</div>
		<div class="form-group"
			 title="Specify lower and/or upper bounds on the value of the data element to be thresholded">
			<div class="col-md-2">
				<label class="sr-only" for="thresholdLowerComp">From</label>
				<select class="form-control" name="thresholdLowerComp" id="thresholdLowerComp">
					<c:forEach var="valComp" items="${valueComparatorsLower}">
						<option value="${valComp.id}">${valComp.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="thresholdLowerVal">From value</label>
				<input class="form-control" type="text"
					   name="thresholdLowerVal" id="thresholdLowerVal"/>
			</div>
			<div class="col-md-1 control-label" style="text-align: center">
				<label for="thresholdUpperComp">to</label>
			</div>
			<div class="col-md-2">
				<select class="form-control" name="thresholdUpperComp" id="thresholdUpperComp">
					<c:forEach var="valComp" items="${valueComparatorsUpper}">
						<option value="${valComp.id}">${valComp.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="thresholdUpperVal">To value</label>
				<input class="form-control" type="text" class="valueField"
					   name="thresholdUpperVal" id="thresholdUpperVal"/>
			</div>
		</div>
	<fieldset>
		<legend>Context(s)</legend>
		<div class="form-group">
			<div class="col-sm-12">
				<label class="sr-only" for="thresholdRelatedDataElements1">Contextual data element 1</label>
				<div id="thresholdRelatedDataElements1"
					 class="tree-drop tree-drop-multiple jstree-drop thresholdedRelatedDataElements"
					 title="Drag and drop a contextual system or user-defined data element, an interval of which must be present to match this threshold">
					<div class="label-info text-center">
						Drop Contextual Data Element(s) Here
					</div>
					<ul data-proptype="empty" data-drop-type="multiple" class="sortable">
					</ul>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="sr-only" for="thresholdWithinAtLeast">From</label>
			<div class="col-md-2">
				<input type="text" class="form-control" placeholder="min"
					   name="thresholdWithinAtLeast" id="thresholdWithinAtLeast" value=""/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="thresholdWithinAtLeastUnits">From units</label>
				<select name="thresholdWithinAtLeastUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-1 control-label" style="text-align: center">
				<label for="thresholdWithinAtMost">to</label>
			</div>
			<div class="col-md-2">
				<input type="text" class="form-control" placeholder="max"
					   name="thresholdWithinAtMost" id="thresholdWithinAtMost" value=""/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="thresholdWithinAtMostUnits">To units</label>
				<select name="thresholdWithinAtMostUnits" id="thresholdWithinAtMostUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>
								${unit.description}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-12">
				<select name="thresholdDataElementTemporalRelation"
						class="form-control"
						style="vertical-align: middle"
						title="Specify a temporal relationship constraint between intervals of the data element matching the threshold and this contextual data element">
					<c:forEach var="op" items="${contextRelationOps}">
						<option value="${op.id}"
								<c:if test="${op.id == defaultRelationOp.id}">selected="selected"</c:if>>${op.description}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</fieldset>
</div>
</c:otherwise>
</c:choose>
</div>
</div>
<div class="form-group">
	<div class="col-sm-10 col-sm-offset-2 text-center">
		<button id="duplicateThresholdButton" type="button" class="btn btn-primary">
			<span class="glyphicon glyphicon-plus"></span>
			Add Threshold
		</button>
		<button id="savePropositionButton" type="button" class="btn btn-primary">Save</button>
	</div>
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
		src="${pageContext.request.contextPath}/assets/js/eureka.editor${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript">
	eureka.editor.setup('VALUE_THRESHOLD', '', ${proposition != null ? proposition.id : 'null'},
			'#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
			'ul.sortable', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css',
			'#searchValidationModal','#searchNoResultsModal','#searchUpdateDiv');
	eureka.editor.setupDuplication('#duplicateThresholdButton', '#propDefinition', '.value-threshold', eureka.editor.duplicateThreshold);
</script>
</template:content>
</template:insert>
