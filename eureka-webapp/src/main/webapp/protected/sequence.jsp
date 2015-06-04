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
<h3>Sequence Editor</h3>
<p>Computes intervals with the same start and stop time as the Main data element below when the temporal
	relationships below are satisfied.
</p>
<div class="row">
<div class="col-xs-5">
	<ul class="nav nav-tabs">
		<li class="active"><a href="#systemElems" data-toggle="tab">System</a></li>
		<li><a href="#userElems" data-toggle="tab">User</a></li>
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
<div id="definitionContainer" class="col-xs-7" data-definition-container="true">
<form id="sequenceForm" class="form-horizontal vert-offset" role="form">
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
<div class="row">
	<div id="propDefinition" class="col-sm-12 panel panel-default">
		<div class="row panel-heading"><h4 class="panel-title">Main Data Element</h4></div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="sr-only" for="mainDataElement">Main data element</label>
					<div id="mainDataElement" class="tree-drop tree-drop-single jstree-drop"
						 title="Drag a system or user-defined data element in here">
						<div class="label-info text-center">Drop Here</div>
						<ul data-type="main" data-drop-type="single" data-count="1" class="sortable"
							style="width: 100%; height: 100%">
							<c:if test="${not empty proposition and propositionType == 'SEQUENCE'}">
								<li data-key="${proposition.primaryDataElement.dataElementKey}"
									data-desc="${proposition.primaryDataElement.dataElementDisplayName} (${proposition.primaryDataElement.dataElementKey})"
									data-space="${proposition.primaryDataElement.inSystem ? 'system' : 'user'}"><span
										class="delete-icon glyphicon glyphicon-remove"></span> <span
										class="desc">${empty proposition.primaryDataElement.dataElementDisplayName ? '' : proposition.primaryDataElement.dataElementDisplayName}
																	(${proposition.primaryDataElement.dataElementKey})</span>
								</li>
							</c:if>
						</ul>
					</div>
				</div>
			</div>
			<fieldset>
			<legend><input id="mainDataElementSpecifyDuration" type="checkbox"
																value="true" class="checkbox-inline"
																name="mainDataElementSpecifyDuration"
																<c:if test="${propositionType == 'SEQUENCE' and proposition.primaryDataElement.hasDuration}">checked="checked"</c:if> />
				Duration
			</legend>
			<div class="form-group">
				<div class="col-md-2">
					<label class="sr-only" id="mainDataElementMinDurationValue">From</label>
					<input type="number" class="form-control" name="mainDataElementMinDurationValue"
						   id="mainDataElementMinDurationValue"
						   placeholder="min"
						   value="<c:if test="${propositionType == 'SEQUENCE'}">${proposition.primaryDataElement.minDuration}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" id="mainDataElementMinDurationUnits">From time units</label>
					<select name="mainDataElementMinDurationUnits" id="mainDataElementMinDurationUnits" class="form-control">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryDataElement.minDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 control-label" style="text-align: center">
					<label for="mainDataElementMaxDurationValue">to</label>
				</div>
				<div class="col-md-2">
					<input type="number" class="form-control" name="mainDataElementMaxDurationValue"
						   id="mainDataElementMaxDurationValue"
						   placeholder="max"
						   value="<c:if test="${propositionType == 'SEQUENCE'}">${proposition.primaryDataElement.maxDuration}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" id="mainDataElementMaxDurationUnits">From time units</label>
					<select name="mainDataElementMaxDurationUnits" id="mainDataElementMaxDurationUnits" class="form-control">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryDataElement.maxDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			</fieldset>
			<fieldset>
			<legend>
				<input type="checkbox" class="checkbox-inline propertyValueConstraint" value="true"
					   name="mainDataElementSpecifyProperty"
					   <c:if test="${propositionType == 'SEQUENCE' and proposition.primaryDataElement.hasPropertyConstraint}">checked="checked"</c:if>
					   <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryDataElement.dataElementKey]}">disabled="disabled"</c:if> />
				Property value
			</legend>
			<div class="form-group">
				<div class="col-sm-6">
					<label class="sr-only" for="mainDataElementPropertyName">Property name</label>
					<select name="mainDataElementPropertyName" class="form-control"
							id="mainDataElementPropertyName"
							data-properties-provider="mainDataElement">
						<c:if test="${propositionType == 'SEQUENCE'}">
							<c:forEach var="property"
									   items="${properties[proposition.primaryDataElement.dataElementKey]}">
								<option value="${property}"
										<c:if test="${proposition.primaryDataElement.property == property}">selected="selected"</c:if>>${property}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="col-sm-6">
					<label class="sr-only" for="mainDataElementPropertyValue">Property value</label>
					<input type="text" class="form-control propertyValueField" name="mainDataElementPropertyValue" id="mainDataElementPropertyValue"
						   value="<c:if test="${propositionType == 'SEQUENCE' and not empty proposition.primaryDataElement.propertyValue}">${proposition.primaryDataElement.propertyValue}</c:if>"
						   <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryDataElement.dataElementKey]}">disabled="disabled"</c:if> />
				</div>
			</div>
			</fieldset>
	</div>
</div>
<div class="form-group">
<div id="sequenceRelatedDataElements" class="sequence-relations-container">
<c:choose>
<c:when test="${not empty proposition and propositionType == 'SEQUENCE' and not empty proposition.relatedDataElements}">
	<c:forEach var="relation" items="${proposition.relatedDataElements}" varStatus="status">
		<div class="sequence-relation drop-parent panel panel-default col-sm-12">
			<div class="row panel-heading"><h4 class="panel-title">Related data element <span class="count">${status.count}</span></h4></div>
				<div class="form-group">
					<div class="col-sm-12">
						<label class="sr-only" for="relatedDataElement${status.count}">Related data element ${status.count}</label>
						<div id="relatedDataElement${status.count}"
							 class="tree-drop tree-drop-single jstree-drop sequencedDataElement"
							 title="Drag another system or user-defined data element in here">
							<div class="label-info text-center">Drop Here</div>
							<ul class="sortable" data-type="related" data-drop-type="single"
								data-count="${status.count + 1}">
								<li data-key="${relation.dataElementField.dataElementKey}"
									data-desc="${relation.dataElementField.dataElementDisplayName} (${relation.dataElementField.dataElementKey})"
									data-space="${relation.dataElementField.inSystem ? 'system' : 'user'}">
									<span class="delete-icon glyphicon glyphicon-remove"></span>
									<span class="desc">${empty relation.dataElementField.dataElementDisplayName ? '' : relation.dataElementField.dataElementDisplayName} (${relation.dataElementField.dataElementKey})</span>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<fieldset>
				<legend>
					<input type="checkbox" value="true"
						   name="sequenceRelDataElementSpecifyDuration"
						   <c:if test="${relation.dataElementField.hasDuration}">checked="checked"</c:if> />
					Duration
				</legend>
				<div class="form-group">
					<div class="col-md-2">
						<label class="sr-only" for="sequenceRelDataElementMinDurationValue">From</label>
						<input type="number" class="form-control" name="sequenceRelDataElementMinDurationValue"
							   id="sequenceRelDataElementMinDurationValue"
							   placeholder="min"
							   value="${relation.dataElementField.minDuration}"/>
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="sequenceRelDataElementMinDurationUnits">From units</label>
						<select name="sequenceRelDataElementMinDurationUnits" class="form-control">
							<c:forEach var="unit" items="${timeUnits}">
								<option value="${unit.id}"
										<c:if test="${unit.id == relation.dataElementField.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-1 control-label" style="text-align: center">
						<label for="sequenceRelDataElementMaxDurationValue">to</label>
					</div>
					<div class="col-md-2">
						<input type="number" class="form-control" name="sequenceRelDataElementMaxDurationValue"
							   id="sequenceRelDataElementMaxDurationValue"
							   placeholder="max"
							   value="${relation.dataElementField.maxDuration}"/>
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="sequenceRelDataElementMaxDurationUnits">To units</label>
						<select name="sequenceRelDataElementMaxDurationUnits" class="form-control">
							<c:forEach var="unit" items="${timeUnits}">
								<option value="${unit.id}"
										<c:if test="${unit.id == relation.dataElementField.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				</fieldset>
				<fieldset>
				<legend>
					<input type="checkbox" class="checkbox-inline propertyValueConstraint" value="true"
						   name="sequenceRelDataElementSpecifyProperty"
						   <c:if test="${relation.dataElementField.hasPropertyConstraint}">checked="checked"</c:if>
						   <c:if test="${propositionType == 'SEQUENCE' and empty properties[relation.dataElementField.dataElementKey]}">disabled="disabled"</c:if> />
					Property value
				</legend>
				<div class="form-group">
					<div class="col-sm-6">
						<label class="sr-only" for="sequenceRelDataElementPropertyName">Property name</label>
						<select name="sequenceRelDataElementPropertyName" class="form-control"
								id="sequenceRelDataElementPropertyName"
								data-properties-provider="relatedDataElement${status.count}">
							<c:forEach var="property" items="${properties[relation.dataElementField.dataElementKey]}">
								<option value="${property}"
										<c:if test="${relation.dataElementField.property == property}">selected="selected"</c:if>>${property}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-6">
						<label class="sr-only" for="sequenceRelDataElementPropertyValue">Property value</label>
						<input type="text" class="form-control propertyValueField"
							   name="sequenceRelDataElementPropertyValue"
							   id="sequenceRelDataElementPropertyValue"
							   value="<c:if test="${not empty relation.dataElementField.propertyValue}">${relation.dataElementField.propertyValue}</c:if>"
							   <c:if test="${empty properties[relation.dataElementField.dataElementKey]}">disabled="disabled"</c:if> />
					</div>
				</div>
				</fieldset>
				<fieldset>
				<legend>Temporal Relationship</legend>
				<div class="form-group">
					<div class="col-sm-6">
						<label class="sr-only" for="sequenceRelDataElementTemporalRelation">Temporal relation</label>
						<select name="sequenceRelDataElementTemporalRelation"
								id="sequenceRelDataElementTemporalRelation"
								class="form-control">
							<c:forEach var="op" items="${sequentialRelationOps}">
								<option value="${op.id}" <c:if test="${op.id == relation.relationOperator}">selected="selected"</c:if>>${op.description}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-6">
						<label class="sr-only" for="propositionSelect">Other data element</label>
						<select name="propositionSelect" class="form-control"
								data-sourceid="${relation.sequentialDataElementSource}"></select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-1 control-label" style="text-align: center">
						<label for="sequenceRhsDataElementMinDistanceValue">by</label>
					</div>
					<div class="col-md-2">
						<input type="text" class="form-control" name="sequenceRhsDataElementMinDistanceValue"
							   id="sequenceRhsDataElementMinDistanceValue"
							   placeholder="min"
							   value="${relation.relationMinCount}"/>
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="sequenceRhsDataElementMinDistanceUnits">By units</label>
						<select name="sequenceRhsDataElementMinDistanceUnits" id="sequenceRhsDataElementMinDistanceUnits" class="form-control">
							<c:forEach var="unit" items="${timeUnits}">
								<option value="${unit.id}"
										<c:if test="${unit.id == relation.relationMinUnits}">selected="selected"</c:if>>${unit.description}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-1 control-label" style="text-align: center">
						<label for="sequenceRhsDataElementMaxDistanceValue">to</label>
					</div>
					<div class="col-md-2">
						<input type="text" class="form-control"
							   placeholder="max"
							   name="sequenceRhsDataElementMaxDistanceValue"
							   id="sequenceRhsDataElementMaxDistanceValue"
							   value="${relation.relationMaxCount}" />
					</div>
					<div class="col-md-3">
						<label class="sr-only" for="sequenceRhsDataElementMaxDistanceUnits">To units</label>
						<select name="sequenceRhsDataElementMaxDistanceUnits" class="form-control">
							<<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}" <c:if test="${unit.id == relation.relationMaxUnits}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
						</select>
					</div>
				</div>
				</fieldset>
		</div>
	</c:forEach>
</c:when>
<c:otherwise>
<div class="sequence-relation drop-parent panel panel-default col-sm-12">
	<div class="row panel-heading">
		<h4 class="panel-title">Related data element <span class="count">1</span></h4>
	</div>
	<div class="form-group">
		<div class="col-sm-12">
			<div id="relatedDataElement1"
				 class="tree-drop tree-drop-single jstree-drop sequencedDataElement"
				 title="Drag another system or user-defined data element in here">
				<div class="label-info text-center">Drop Here</div>
				<ul class="sortable" data-type="related" data-drop-type="single"
					data-count="2">
				</ul>
			</div>
		</div>
	</div>
	<fieldset>
		<legend>
			<input type="checkbox" value="true"
				   name="sequenceRelDataElementSpecifyDuration"/>
			Duration
		</legend>
		<div class="form-group">
			<div class="col-md-2">
				<label class="sr-only" for="sequenceRelDataElementMinDurationValue">Min duration</label>
				<input type="number" class="form-control"
					   placeholder="min"
					   name="sequenceRelDataElementMinDurationValue"/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="sequenceRelDataElementMinDurationUnits">Min duration time units</label>
				<select name="sequenceRelDataElementMinDurationUnits" 
						id="sequenceRelDataElementMinDurationUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-1 control-label" style="text-align: center">
				<label for="sequenceRelDataElementMaxDurationValue">to</label>
			</div>
			<div class="col-md-2">
				<input type="number" class="form-control"
					   placeholder="max"
					   name="sequenceRelDataElementMaxDurationValue"
					   id="sequenceRelDataElementMaxDurationValue"/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="sequenceRelDataElementMaxDurationUnits">Max duration time units</label>
				<select name="sequenceRelDataElementMaxDurationUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</fieldset>
	<fieldset>
		<legend>
			<input type="checkbox" class="checkbox-inline propertyValueConstraint" value="true"
				   name="sequenceRelDataElementSpecifyProperty"/>
			Property value
		</legend>
		<div class="form-group">
			<div class="col-sm-6">
				<label class="sr-only" for="sequenceRelDataElementPropertyName">Property name</label>
				<select name="sequenceRelDataElementPropertyName"
						id="sequenceRelDataElementPropertyName"
						class="form-control"
						data-properties-provider="relatedDataElement1">
				</select>
			</div>
			<div class="col-sm-6">
				<label class="sr-only" for="sequenceRelDataElementPropertyValue">Property value</label>
				<input type="text" class="form-control propertyValueField"
					   name="sequenceRelDataElementPropertyValue"
					   id="sequenceRelDataElementPropertyValue"/>
			</div>
		</div>
	</fieldset>
	<fieldset>
		<legend>Temporal relationship</legend>
		<div class="form-group">
			<div class="col-sm-6">
				<label class="sr-only" for="sequenceRelDataElementTemporalRelation">Temporal relation</label>
				<select name="sequenceRelDataElementTemporalRelation"
						id="sequenceRelDataElementTemporalRelation"
						class="form-control">
					<c:forEach var="op" items="${sequentialRelationOps}">
						<option value="${op.id}"
								<c:if test="${op.id == defaultRelationOp.id}">selected="selected"</c:if>>${op.description}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-sm-6">
				<label class="sr-only" for="propositionSelect">Selected data element</label>
				<select name="propositionSelect" id="propositionSelect" class="form-control"></select>
			</div>
		</div>
		<div class="form-group">
			<div class="col-md-1 control-label" style="text-align: center">
				<label for="sequenceRhsDataElementMinDistanceValue">by</label>
			</div>
			<div class="col-md-2">
				<input type="text" class="form-control"
					   placeholder="min"
					   name="sequenceRhsDataElementMinDistanceValue"
					   id="sequenceRhsDataElementMinDistanceValue"/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="sequenceRhsDataElementMinDistanceUnits">By units</label>
				<select name="sequenceRhsDataElementMinDistanceUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-1 control-label" style="text-align: center">
				<label for="sequenceRhsDataElementMaxDistanceValue">to</label>
			</div>
			<div class="col-md-2">
				<input type="text" class="form-control"
					   placeholder="max"
					   name="sequenceRhsDataElementMaxDistanceValue"
					   id="sequenceRhsDataElementMaxDistanceValue"/>
			</div>
			<div class="col-md-3">
				<label class="sr-only" for="sequenceRhsDataElementMaxDistanceUnits">To units</label>
				<select name="sequenceRhsDataElementMaxDistanceUnits" class="form-control">
					<c:forEach var="unit" items="${timeUnits}">
						<option value="${unit.id}"
								<c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</fieldset>
</div>
</div>
</c:otherwise>
</c:choose>
</div>
<div class="form-group">
	<div class="col-sm-12 text-center">
		<button id="duplicateRelatedButton" type="button" class="btn btn-primary">
			<span class="glyphicon glyphicon-plus"></span> Add Related Data Element
		</button>
		<button id="savePropositionButton" type="button" class="btn btn-primary">Save</button>
	</div>
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
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/jquery.jstree.js"></script>
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/eureka.tree-phenotype${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/eureka.editor${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript">
	eureka.editor.setup('SEQUENCE', '', ${proposition != null ? proposition.id : 'null'},
			'#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
			'ul.sortable', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css', '#searchModal',
			'#searchValidationModal','#searchNoResultsModal','#searchUpdateDiv');
	eureka.editor.setupDuplication('#duplicateRelatedButton', '.sequence-relations-container', '.sequence-relation',
			eureka.editor.duplicateSequenceRelation, eureka.editor.setPropositionSelects);
</script>
</template:content>
</template:insert>
