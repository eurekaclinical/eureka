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
<h3>Frequency Editor</h3>
<p>
	Computes an interval over the temporal extent of the intervals contributing to the specified frequency
	count below.
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
			</div>
			<div id="userElems" class="tab-pane fade">
				<div id="userTree"></div>
			</div>
		</div>
	</div>
	<div id="definitionContainer" class="col-xs-7">
		<form id="frequencyForm" class="form-horizontal vert-offset" role="form">
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
			<!--<label>Definition</label>-->
			<fieldset>
			<legend>Threshold</legend>
			<div class="form-group">
				<div class="col-sm-4">
					<label class="sr-only" for="freqTypes">Type</label>
					<select id="freqTypes" class="form-control" name="freqTypes" id="freqTypes"
							title="Specify whether only the first n intervals will be matched (First) or any n intervals (At least)">
						<c:forEach var="freqType" items="${frequencyTypes}">
							<option value="${freqType.id}"
									<c:if test="${propositionType == 'FREQUENCY' ? freqType.id == proposition.frequencyType : freqType.id == defaultFrequencyType.id}">selected="selected"</c:if>>${freqType.description}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-4">
					<label class="sr-only" for="freqAtLeastField">Count</label>
					<input type="number" id="frequencyCountField" name="freqAtLeastField" id="freqAtLeastField" min="1"
						   value="<c:choose><c:when test="${propositionType == 'FREQUENCY'}">${proposition.atLeast}</c:when><c:otherwise><c:out value="1"/></c:otherwise></c:choose>"
						   title="Specify the frequency count" class="form-control"/>
				</div>
				<div class="col-sm-4">
						<label id="valueThresholdConsecutiveLabel" class="checkbox-inline<c:if test="${empty proposition or proposition.dataElement.type != 'VALUE_THRESHOLD'}"> default-hidden</c:if>">
						<input type="checkbox" value="true" name="freqIsConsecutive" class="form-control"
								  title="For value threshold data elements, specifies whether no intervening values are present that do not match the threshold or any duration or property constraints specified below"
							   <c:if test="${propositionType == 'FREQUENCY' and proposition.isConsecutive}">checked="checked"</c:if> />consecutive
					</label>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-12">
					<label class="sr-only" for="freqMainDataElement">Data element</label>
					<div id="freqMainDataElement" class="tree-drop tree-drop-single jstree-drop"
						 title="Specify a system or user-defined data element">
						<div class="label-info text-center">Drop Here</div>
						<ul data-type="main" data-drop-type="single" class="sortable">
							<c:if test="${not empty proposition and propositionType == 'FREQUENCY'}">
								<li data-key="${proposition.dataElement.dataElementKey}"
									data-desc="${proposition.dataElement.dataElementDisplayName}"
									data-space="${proposition.dataElement.inSystem ? 'system' : 'user'}">
									<span class="glyphicon glyphicon-remove delete-icon"></span>
									<span class="desc">${empty proposition.dataElement.dataElementDisplayName ? '' : proposition.dataElement.dataElementDisplayName} (${proposition.dataElement.dataElementKey})</span>
								</li>
							</c:if>
						</ul>
					</div>
				</div>
			</div>
			</fieldset>
			<fieldset>
				<legend><input class="checkbox-inline" type="checkbox" value="true"
					   name="freqDataElementSpecifyDuration"
					   <c:if test="${propositionType == 'FREQUENCY' and proposition.dataElement.hasDuration}">checked="checked"</c:if>>
				Duration</legend>
			<div class="form-group">
				<div class="col-md-2">
					<label class="sr-only" for="freqDataElementMinDurationValue">From</label>
					<input type="text" class="form-control"
						   name="freqDataElementMinDurationValue"
						   id="freqDataElementMinDurationValue"
						   placeholder="min"
						   value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.dataElement.minDuration}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" for="freqDataElementMinDurationUnits">From units</label>
					<select name="freqDataElementMinDurationUnits" id="freqDataElementMinDurationUnits" class="form-control">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.dataElement.minDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 control-label" style="text-align: center">
					<label for="freqDataElementMaxDurationValue">to</label>
				</div>
				<div class="col-md-2">
					<input type="text" class="form-control"
						   name="freqDataElementMaxDurationValue"
						   placeholder="max"
						   value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.dataElement.maxDuration}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" for="freqDataElementMaxDurationUnits">To units</label>
					<select name="freqDataElementMaxDurationUnits" id="freqDataElementMaxDurationUnits" class="form-control">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.dataElement.maxDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			</fieldset>
			<fieldset>
			<legend>
				<input type="checkbox" class="checkbox-inline propertyValueConstraint"
					   <c:if test="${propositionType == 'FREQUENCY' and proposition.dataElement.hasPropertyConstraint}">checked="checked"</c:if>
					   name="freqDataElementSpecifyProperty"/>
				Property value
			</legend>
			<div class="form-group">
				<div class="col-sm-6">
					<label class="sr-only" for="freqDataElementPropertyName">Property name</label>
					<select name="freqDataElementPropertyName" id="freqDataElementPropertyName" class="form-control"
							data-properties-provider="freqMainDataElement">
						<c:if test="${propositionType == 'FREQUENCY'}">
							<c:forEach var="property"
									   items="${properties[proposition.dataElement.dataElementKey]}">
								<option value="${property}"
										<c:if test="${proposition.dataElement.property == property}">selected="selected"</c:if>>${property}</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
				<div class="col-sm-6">
					<label class="sr-only" for="freqDataElementPropertyValue">Property value</label>
					<input type="text" class="form-control propertyValueField"
						   name="freqDataElementPropertyValue"
						   id="freqDataElementPropertyValue"
						   value="<c:if test="${propositionType == 'FREQUENCY' and not empty proposition.dataElement.propertyValue}">${proposition.dataElement.propertyValue}</c:if>"/>
				</div>
			</div>
			</fieldset>
			<fieldset>
			<legend>
				<input type="checkbox" value="true" name="freqIsWithin" class="checkbox-inline"
					   <c:if test="${propositionType == 'FREQUENCY' and proposition.isWithin}">checked="checked"</c:if>>
				Distance between
			</legend>
			<div class="form-group">
				<div class="col-md-2">
					<label class="sr-only" for="freqWithinAtLeast">From</label>
					<input type="text" class="form-control" name="freqWithinAtLeast"
						   id="freqWithinAtLeast"
						   placeholder="min"
						   value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.withinAtLeast}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" for="freqWithinAtLeastUnits">Time units</label>
					<select name="freqWithinAtLeastUnits" id="freqWithinAtLeastUnits" class="form-control">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.withinAtLeastUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-md-1 control-label" style="text-align: center">
					<label for="freqWithinAtMost">to</label>
				</div>
				<div class="col-md-2">
					<input type="text" class="form-control" name="freqWithinAtMost"
						   placeholder="max" id="freqWithinAtMost"
						   value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.withinAtMost}</c:if>"/>
				</div>
				<div class="col-md-3">
					<label class="sr-only" for="freqWithinAtMostUnits">Time units</label>
					<select name="freqWithinAtMostUnits" class="form-control" id="freqWithinAtMostUnits">
						<c:forEach var="unit" items="${timeUnits}">
							<option value="${unit.id}"
									<c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.withinAtMostUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			</fieldset>
			<div class="form-group text-centered">
				<button id="savePropositionButton" type="button" class="btn btn-primary">
					Save
				</button>
			</div>
		</form>
	</div>
</div>
<div id="deleteModal" class="modal" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 id="deleteModalLabel" class="modal-title">
					Delete Element
				</h4>
			</div>
			<div id="deleteContent" class="modal-body">
			</div>
			<div class="modal-footer">
				<button id="confirmButton" type="button" class="btn btn-primary">Delete</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<div id="errorModal" class="modal" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
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
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/jquery.jstree.js"></script>
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/eureka.tree${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript" src="${pageContext.request.contextPath}/assets/js/eureka.editor${initParam['eureka-build-timestamp']}.js"></script>
<script language="JavaScript">
	eureka.editor.setup('FREQUENCY', '', ${proposition != null ? proposition.id : 'null'},
			'#systemTree', '#userTree', '#definitionContainer', '#savePropositionButton', 'span.delete-icon',
			'ul.sortable', '${pageContext.request.contextPath}/assets/css/jstree-themes/default/style.css');
</script>
</template:content>
</template:insert>
