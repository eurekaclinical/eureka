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
<h3>Sequence Editor</h3>
<p>Computes intervals with the same start and stop time as the Main phenotype below when the temporal
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
                                <textarea id="propDescription" class="form-control">
                                    <c:if test="${not empty proposition}">${proposition.description}
                                    </c:if>
                                </textarea>
                        </div>
                        <div class="form-group">
                                <div id="propDefinition" class="col-sm-12 panel panel-default">
                                        <div class="row panel-heading"><h4 class="panel-title">Main Phenotype</h4></div>
                                                <div class="form-group">
                                                        <div class="col-sm-12">
                                                                <label class="sr-only" for="mainPhenotype">Main phenotype</label>
                                                                <div id="mainPhenotype" class="tree-drop tree-drop-single jstree-drop"
                                                                         title="Drag a system or user-defined phenotype in here">
                                                                        <div class="label-info text-center">Drop Here</div>
                                                                        <ul data-type="main" data-drop-type="single" data-count="1" class="sortable"
                                                                                style="width: 100%; height: 100%">
                                                                                <c:if test="${not empty proposition and propositionType == 'SEQUENCE'}">
                                                                                        <li data-key="${proposition.primaryPhenotype.phenotypeKey}"
                                                                                            data-desc="${proposition.primaryPhenotype.phenotypeDisplayName} (${proposition.primaryPhenotype.phenotypeKey})"
                                                                                            data-space="${proposition.primaryPhenotype.inSystem ? 'system' : 'user'}">
                                                                                            <span class="delete-icon glyphicon glyphicon-remove"></span> 
                                                                                            <span class="desc">${empty proposition.primaryPhenotype.phenotypeDisplayName ? '' : proposition.primaryPhenotype.phenotypeDisplayName}
                                                                                                (${proposition.primaryPhenotype.phenotypeKey})
                                                                                            </span>
                                                                                        </li>
                                                                                </c:if>
                                                                        </ul>
                                                                </div>
                                                        </div>
                                                </div>
                                                <fieldset>
                                                <legend><input id="mainPhenotypeSpecifyDuration" type="checkbox" value="true" class="checkbox-inline" name="mainPhenotypeSpecifyDuration"
                                                            <c:if test="${propositionType == 'SEQUENCE' and proposition.primaryPhenotype.hasDuration}">
                                                                checked="checked"
                                                            </c:if> 
                                                        />
                                                        Duration
                                                </legend>
                                                <div class="form-group">
                                                        <div class="col-md-2">
                                                                <label class="sr-only" id="mainPhenotypeMinDurationValue">From</label>
                                                                <input type="number" class="form-control" name="mainPhenotypeMinDurationValue"
                                                                           id="mainPhenotypeMinDurationValue"
                                                                           placeholder="min"
                                                                           value="
                                                                           <c:if test="${propositionType == 'SEQUENCE'}">
                                                                               ${proposition.primaryPhenotype.minDuration}
                                                                           </c:if>"
                                                                />
                                                        </div>
                                                        <div class="col-md-3">
                                                                <label class="sr-only" id="mainPhenotypeMinDurationUnits">From time units</label>
                                                                <select name="mainPhenotypeMinDurationUnits" id="mainPhenotypeMinDurationUnits" class="form-control">
                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                <option value="${unit.id}"
                                                                                        <c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryPhenotype.minDurationUnits : unit.id == defaultTimeUnit.id}">
                                                                                            selected="selected"
                                                                                        </c:if>>${unit.description}
                                                                                </option>
                                                                        </c:forEach>
                                                                </select>
                                                        </div>
                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                <label for="mainPhenotypeMaxDurationValue">to</label>
                                                        </div>
                                                        <div class="col-md-2">
                                                                <input type="number" class="form-control" name="mainPhenotypeMaxDurationValue"
                                                                           id="mainPhenotypeMaxDurationValue"
                                                                           placeholder="max"
                                                                           value="
                                                                           <c:if test="${propositionType == 'SEQUENCE'}">
                                                                               ${proposition.primaryPhenotype.maxDuration}
                                                                           </c:if>"
                                                                />
                                                        </div>
                                                        <div class="col-md-3">
                                                                <label class="sr-only" id="mainPhenotypeMaxDurationUnits">From time units</label>
                                                                <select name="mainPhenotypeMaxDurationUnits" id="mainPhenotypeMaxDurationUnits" class="form-control">
                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                <option value="${unit.id}"
                                                                                        <c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryPhenotype.maxDurationUnits : unit.id == defaultTimeUnit.id}">
                                                                                            selected="selected"
                                                                                        </c:if>>${unit.description}
                                                                                </option>
                                                                        </c:forEach>
                                                                </select>
                                                        </div>
                                                </div>
                                                </fieldset>
                                                <fieldset>
                                                <legend>
                                                        <input type="checkbox" class="checkbox-inline propertyValueConstraint" value="true"
                                                                   name="mainPhenotypeSpecifyProperty"
                                                                   <c:if test="${propositionType == 'SEQUENCE' and proposition.primaryPhenotype.hasPropertyConstraint}">
                                                                       checked="checked"
                                                                   </c:if>
                                                                   <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryPhenotype.phenotypeKey]}">
                                                                       disabled="disabled"
                                                                   </c:if> 
                                                        />
                                                        Property value
                                                </legend>
                                                <div class="form-group">
                                                        <div class="col-sm-6">
                                                                <label class="sr-only" for="mainPhenotypePropertyName">Property name</label>
                                                                <select name="mainPhenotypePropertyName" class="form-control"
                                                                                id="mainPhenotypePropertyName"
                                                                                data-properties-provider="mainPhenotype">
                                                                        <c:if test="${propositionType == 'SEQUENCE'}">
                                                                                <c:forEach var="property" items="${properties[proposition.primaryPhenotype.phenotypeKey]}">
                                                                                        <option value="${property}"
                                                                                                <c:if test="${proposition.primaryPhenotype.property == property}">
                                                                                                    selected="selected"
                                                                                                </c:if>>${property}
                                                                                        </option>
                                                                                </c:forEach>
                                                                        </c:if>
                                                                </select>
                                                        </div>
                                                        <div class="col-sm-6">
                                                                <label class="sr-only" for="mainPhenotypePropertyValue">Property value</label>
                                                                <input type="text" class="form-control propertyValueField" name="mainPhenotypePropertyValue" id="mainPhenotypePropertyValue"
                                                                           value="<c:if test="${propositionType == 'SEQUENCE' and not empty proposition.primaryPhenotype.propertyValue}">${proposition.primaryPhenotype.propertyValue}</c:if>"
                                                                           <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryPhenotype.phenotypeKey]}">disabled="disabled"</c:if> />
                                                        </div>
                                                </div>
                                                </fieldset>
                                </div>
                        </div>
                        <div class="form-group">
                                <div id="sequenceRelatedPhenotypes" class="sequence-relations-container">
                                        <c:choose>
                                                <c:when test="${not empty proposition and propositionType == 'SEQUENCE' and not empty proposition.relatedPhenotypes}">
                                                        <c:forEach var="relation" items="${proposition.relatedPhenotypes}" varStatus="status">
                                                                <div class="sequence-relation drop-parent panel panel-default col-sm-12">
                                                                        <div class="row panel-heading"><h4 class="panel-title">Related phenotype <span class="count">${status.count}</span></h4></div>
                                                                                <div class="form-group">
                                                                                        <div class="col-sm-12">
                                                                                                <label class="sr-only" for="relatedPhenotype${status.count}">Related phenotype ${status.count}</label>
                                                                                                <div id="relatedPhenotype${status.count}"
                                                                                                         class="tree-drop tree-drop-single jstree-drop sequencedPhenotype"
                                                                                                         title="Drag another system or user-defined phenotype in here">
                                                                                                        <div class="label-info text-center">Drop Here</div>
                                                                                                        <ul class="sortable" data-type="related" data-drop-type="single"
                                                                                                                data-count="${status.count + 1}">
                                                                                                                <li data-key="${relation.phenotypeField.phenotypeKey}"
                                                                                                                        data-desc="${relation.phenotypeField.phenotypeDisplayName} (${relation.phenotypeField.phenotypeKey})"
                                                                                                                        data-space="${relation.phenotypeField.inSystem ? 'system' : 'user'}">
                                                                                                                        <span class="delete-icon glyphicon glyphicon-remove"></span>
                                                                                                                        <span class="desc">${empty relation.phenotypeField.phenotypeDisplayName ? '' : relation.phenotypeField.phenotypeDisplayName} (${relation.phenotypeField.phenotypeKey})</span>
                                                                                                                </li>
                                                                                                        </ul>
                                                                                                </div>
                                                                                        </div>
                                                                                </div>
                                                                                <fieldset>
                                                                                <legend>
                                                                                        <input type="checkbox" value="true"
                                                                                                   name="sequenceRelPhenotypeSpecifyDuration"
                                                                                                   <c:if test="${relation.phenotypeField.hasDuration}">checked="checked"</c:if> />
                                                                                        Duration
                                                                                </legend>
                                                                                <div class="form-group">
                                                                                        <div class="col-md-2">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypeMinDurationValue">From</label>
                                                                                                <input type="number" class="form-control" name="sequenceRelPhenotypeMinDurationValue"
                                                                                                           id="sequenceRelPhenotypeMinDurationValue"
                                                                                                           placeholder="min"
                                                                                                           value="${relation.phenotypeField.minDuration}"/>
                                                                                        </div>
                                                                                        <div class="col-md-3">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypeMinDurationUnits">From units</label>
                                                                                                <select name="sequenceRelPhenotypeMinDurationUnits" class="form-control">
                                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                                <option value="${unit.id}"
                                                                                                                                <c:if test="${unit.id == relation.phenotypeField.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
                                                                                                        </c:forEach>
                                                                                                </select>
                                                                                        </div>
                                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                                <label for="sequenceRelPhenotypeMaxDurationValue">to</label>
                                                                                        </div>
                                                                                        <div class="col-md-2">
                                                                                                <input type="number" class="form-control" name="sequenceRelPhenotypeMaxDurationValue"
                                                                                                           id="sequenceRelPhenotypeMaxDurationValue"
                                                                                                           placeholder="max"
                                                                                                           value="${relation.phenotypeField.maxDuration}"/>
                                                                                        </div>
                                                                                        <div class="col-md-3">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypeMaxDurationUnits">To units</label>
                                                                                                <select name="sequenceRelPhenotypeMaxDurationUnits" class="form-control">
                                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                                <option value="${unit.id}"
                                                                                                                                <c:if test="${unit.id == relation.phenotypeField.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
                                                                                                        </c:forEach>
                                                                                                </select>
                                                                                        </div>
                                                                                </div>
                                                                                </fieldset>
                                                                                <fieldset>
                                                                                <legend>
                                                                                        <input type="checkbox" class="checkbox-inline propertyValueConstraint" value="true"
                                                                                                   name="sequenceRelPhenotypeSpecifyProperty"
                                                                                                   <c:if test="${relation.phenotypeField.hasPropertyConstraint}">checked="checked"</c:if>
                                                                                                   <c:if test="${propositionType == 'SEQUENCE' and empty properties[relation.phenotypeField.phenotypeKey]}">disabled="disabled"</c:if> />
                                                                                        Property value
                                                                                </legend>
                                                                                <div class="form-group">
                                                                                        <div class="col-sm-6">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypePropertyName">Property name</label>
                                                                                                <select name="sequenceRelPhenotypePropertyName" class="form-control"
                                                                                                                id="sequenceRelPhenotypePropertyName"
                                                                                                                data-properties-provider="relatedPhenotype${status.count}">
                                                                                                        <c:forEach var="property" items="${properties[relation.phenotypeField.phenotypeKey]}">
                                                                                                                <option value="${property}"
                                                                                                                                <c:if test="${relation.phenotypeField.property == property}">selected="selected"</c:if>>${property}</option>
                                                                                                        </c:forEach>
                                                                                                </select>
                                                                                        </div>
                                                                                        <div class="col-sm-6">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypePropertyValue">Property value</label>
                                                                                                <input type="text" class="form-control propertyValueField"
                                                                                                           name="sequenceRelPhenotypePropertyValue"
                                                                                                           id="sequenceRelPhenotypePropertyValue"
                                                                                                           value="<c:if test="${not empty relation.phenotypeField.propertyValue}">${relation.phenotypeField.propertyValue}</c:if>"
                                                                                                           <c:if test="${empty properties[relation.phenotypeField.phenotypeKey]}">disabled="disabled"</c:if> />
                                                                                        </div>
                                                                                </div>
                                                                                </fieldset>
                                                                                <fieldset>
                                                                                <legend>Temporal Relationship</legend>
                                                                                <div class="form-group">
                                                                                        <div class="col-sm-6">
                                                                                                <label class="sr-only" for="sequenceRelPhenotypeTemporalRelation">Temporal relation</label>
                                                                                                <select name="sequenceRelPhenotypeTemporalRelation"
                                                                                                                id="sequenceRelPhenotypeTemporalRelation"
                                                                                                                class="form-control">
                                                                                                        <c:forEach var="op" items="${sequentialRelationOps}">
                                                                                                                <option value="${op.id}" <c:if test="${op.id == relation.relationOperator}">selected="selected"</c:if>>${op.description}</option>
                                                                                                        </c:forEach>
                                                                                                </select>
                                                                                        </div>
                                                                                        <div class="col-sm-6">
                                                                                                <label class="sr-only" for="propositionSelect">Other phenotype</label>
                                                                                                <select name="propositionSelect" class="form-control"
                                                                                                                data-sourceid="${relation.sequentialPhenotypeSource}"></select>
                                                                                        </div>
                                                                                </div>
                                                                                <div class="form-group">
                                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                                <label for="sequenceRhsPhenotypeMinDistanceValue">by</label>
                                                                                        </div>
                                                                                        <div class="col-md-2">
                                                                                                <input type="text" class="form-control" name="sequenceRhsPhenotypeMinDistanceValue"
                                                                                                           id="sequenceRhsPhenotypeMinDistanceValue"
                                                                                                           placeholder="min"
                                                                                                           value="${relation.relationMinCount}"/>
                                                                                        </div>
                                                                                        <div class="col-md-3">
                                                                                                <label class="sr-only" for="sequenceRhsPhenotypeMinDistanceUnits">By units</label>
                                                                                                <select name="sequenceRhsPhenotypeMinDistanceUnits" id="sequenceRhsPhenotypeMinDistanceUnits" class="form-control">
                                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                                <option value="${unit.id}"
                                                                                                                                <c:if test="${unit.id == relation.relationMinUnits}">selected="selected"</c:if>>${unit.description}</option>
                                                                                                        </c:forEach>
                                                                                                </select>
                                                                                        </div>
                                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                                <label for="sequenceRhsPhenotypeMaxDistanceValue">to</label>
                                                                                        </div>
                                                                                        <div class="col-md-2">
                                                                                                <input type="text" class="form-control"
                                                                                                           placeholder="max"
                                                                                                           name="sequenceRhsPhenotypeMaxDistanceValue"
                                                                                                           id="sequenceRhsPhenotypeMaxDistanceValue"
                                                                                                           value="${relation.relationMaxCount}" />
                                                                                        </div>
                                                                                        <div class="col-md-3">
                                                                                                <label class="sr-only" for="sequenceRhsPhenotypeMaxDistanceUnits">To units</label>
                                                                                                <select name="sequenceRhsPhenotypeMaxDistanceUnits" class="form-control">
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
                                                                <h4 class="panel-title">Related phenotype<span class="count">1</span></h4>
                                                        </div>
                                                        <div class="form-group">
                                                                <div class="col-sm-12">
                                                                        <div id="relatedPhenotype1"
                                                                                 class="tree-drop tree-drop-single jstree-drop sequencedPhenotype"
                                                                                 title="Drag another system or user-defined phenotype in here">
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
                                                                                   name="sequenceRelPhenotypeSpecifyDuration"/>
                                                                        Duration
                                                                </legend>
                                                                <div class="form-group">
                                                                        <div class="col-md-2">
                                                                                <label class="sr-only" for="sequenceRelPhenotypeMinDurationValue">Min duration</label>
                                                                                <input type="number" class="form-control"
                                                                                           placeholder="min"
                                                                                           name="sequenceRelPhenotypeMinDurationValue"/>
                                                                        </div>
                                                                        <div class="col-md-3">
                                                                                <label class="sr-only" for="sequenceRelPhenotypeMinDurationUnits">Min duration time units</label>
                                                                                <select name="sequenceRelPhenotypeMinDurationUnits" 
                                                                                                id="sequenceRelPhenotypeMinDurationUnits" class="form-control">
                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                <option value="${unit.id}"
                                                                                                                <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
                                                                                        </c:forEach>
                                                                                </select>
                                                                        </div>
                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                <label for="sequenceRelPhenotypeMaxDurationValue">to</label>
                                                                        </div>
                                                                        <div class="col-md-2">
                                                                                <input type="number" class="form-control"
                                                                                           placeholder="max"
                                                                                           name="sequenceRelPhenotypeMaxDurationValue"
                                                                                           id="sequenceRelPhenotypeMaxDurationValue"/>
                                                                        </div>
                                                                        <div class="col-md-3">
                                                                                <label class="sr-only" for="sequenceRelPhenotypeMaxDurationUnits">Max duration time units</label>
                                                                                <select name="sequenceRelPhenotypeMaxDurationUnits" class="form-control">
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
                                                                                   name="sequenceRelPhenotypeSpecifyProperty"/>
                                                                        Property value
                                                                </legend>
                                                                <div class="form-group">
                                                                        <div class="col-sm-6">
                                                                                <label class="sr-only" for="sequenceRelPhenotypePropertyName">Property name</label>
                                                                                <select name="sequenceRelPhenotypePropertyName"
                                                                                                id="sequenceRelPhenotypePropertyName"
                                                                                                class="form-control"
                                                                                                data-properties-provider="relatedPhenotype1">
                                                                                </select>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                                <label class="sr-only" for="sequenceRelPhenotypePropertyValue">Property value</label>
                                                                                <input type="text" class="form-control propertyValueField"
                                                                                           name="sequenceRelPhenotypePropertyValue"
                                                                                           id="sequenceRelPhenotypePropertyValue"/>
                                                                        </div>
                                                                </div>
                                                        </fieldset>
                                                        <fieldset>
                                                                <legend>Temporal relationship</legend>
                                                                <div class="form-group">
                                                                        <div class="col-sm-6">
                                                                                <label class="sr-only" for="sequenceRelPhenotypeTemporalRelation">Temporal relation</label>
                                                                                <select name="sequenceRelPhenotypeTemporalRelation"
                                                                                                id="sequenceRelPhenotypeTemporalRelation"
                                                                                                class="form-control">
                                                                                        <c:forEach var="op" items="${sequentialRelationOps}">
                                                                                                <option value="${op.id}"
                                                                                                                <c:if test="${op.id == defaultRelationOp.id}">selected="selected"</c:if>>${op.description}</option>
                                                                                        </c:forEach>
                                                                                </select>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                                <label class="sr-only" for="propositionSelect">Selected phenotype</label>
                                                                                <select name="propositionSelect" id="propositionSelect" class="form-control"></select>
                                                                        </div>
                                                                </div>
                                                                <div class="form-group">
                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                <label for="sequenceRhsPhenotypeMinDistanceValue">by</label>
                                                                        </div>
                                                                        <div class="col-md-2">
                                                                                <input type="text" class="form-control"
                                                                                           placeholder="min"
                                                                                           name="sequenceRhsPhenotypeMinDistanceValue"
                                                                                           id="sequenceRhsPhenotypeMinDistanceValue"/>
                                                                        </div>
                                                                        <div class="col-md-3">
                                                                                <label class="sr-only" for="sequenceRhsPhenotypeMinDistanceUnits">By units</label>
                                                                                <select name="sequenceRhsPhenotypeMinDistanceUnits" class="form-control">
                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                <option value="${unit.id}"
                                                                                                                <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
                                                                                        </c:forEach>
                                                                                </select>
                                                                        </div>
                                                                        <div class="col-md-1 control-label" style="text-align: center">
                                                                                <label for="sequenceRhsPhenotypeMaxDistanceValue">to</label>
                                                                        </div>
                                                                        <div class="col-md-2">
                                                                                <input type="text" class="form-control"
                                                                                           placeholder="max"
                                                                                           name="sequenceRhsPhenotypeMaxDistanceValue"
                                                                                           id="sequenceRhsPhenotypeMaxDistanceValue"/>
                                                                        </div>
                                                                        <div class="col-md-3">
                                                                                <label class="sr-only" for="sequenceRhsPhenotypeMaxDistanceUnits">To units</label>
                                                                                <select name="sequenceRhsPhenotypeMaxDistanceUnits" class="form-control">
                                                                                        <c:forEach var="unit" items="${timeUnits}">
                                                                                                <option value="${unit.id}"
                                                                                                                <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
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
                                <div class="col-sm-12 text-center">
                                        <button id="duplicateRelatedButton" type="button" class="btn btn-primary">
                                                <span class="glyphicon glyphicon-plus"></span> Add Related Phenotype
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
