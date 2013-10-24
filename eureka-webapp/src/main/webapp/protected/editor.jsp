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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_editor.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

		<table align="center" border="0" cellpadding="0" cellspacing="0">
			<tr><td>
					<!-- Tabs -->
					<div id="wizard" class="swMain">
						<ul id="steps">
							<li><a href="#step-1">
									<label class="stepNumber">1</label>
									<span class="stepDesc">
										Select Type<%--<br />
										<small>Select Type of Element</small>--%>
									</span>
								</a></li>
							<li><a href="#step-2">
									<label class="stepNumber">2</label>
									<span class="stepDesc">
										Specify<%--<br />
										<small>Select Elements<br />from
											Ontology</small>--%>
									</span>
								</a>
							<li><a href="#step-3">
									<label class="stepNumber">3</label>
									<span class="stepDesc">
										<c:choose>
											<c:when test="${not empty proposition}">
												Update Name<%--<br />
												<small>Update the
													Element&apos;s Name</small>--%>
												</c:when>
												<c:otherwise>
												Name<%--<br />
												<small>Select a Name<br />for
													the Element</small>--%>
												</c:otherwise>
											</c:choose>
									</span>
								</a></li>
							<li><a href="#step-4">
									<label class="stepNumber">4</label>
									<span class="stepDesc">
										Save<%--<br />
										<small>Save Element to Database</small>--%>
									</span>
								</a></li>
						</ul>
						<div id="step-1">
							<h2 class="StepTitle">
								Select Type of Phenotype
								<span class="editor-help">
									<a href="${initParam['eureka-help-url']}/phenotypes.html#select-type" target="eureka-help">Help</a>
								</span>
							</h2>
								<p>Eureka! computes time intervals representing <em>when</em> your specified phenotype occurred for each patient in your dataset.
									Each time interval has a start time and a stop time. The rules for computing these intervals differ for each type
									of phenotype. Specify the type of the phenotype you are creating below:</p>
							<dl id="select_element_table">
								<%-- These values come from the DataElement.Type enum --%>
								<c:set var="types" value="CATEGORIZATION,SEQUENCE,FREQUENCY,VALUE_THRESHOLD" />
								<c:forTokens items="${types}" var="myType" delims="," varStatus="status">
									<dt class="firstCol">
									<label><input type="radio" id="type" name="type" value="${myType}" <c:if test="${not empty proposition and propositionType == myType}">checked="checked"</c:if>/><fmt:message key="dataelementtype.${myType}.displayName" /></label>
									</dt>
									<dd class="secondCol">
										<fmt:message key="dataelementtype.${myType}.description" />
									</dd>
								</c:forTokens>
							</dl>
						</div>
						<div id="step-2">
							<h2 class="StepTitle">
								<span>Specify Phenotype</span>
								<span class="editor-help">
									<a href="${initParam['eureka-help-url']}/phenotypes.html#select-elements" target="eureka-help">Help</a>
								</span>
							</h2>
							<table id="element_tree_table">
								<tr>
									<td  valign="top" width="25%">
										<div class="tabs">
											<ul class="tabNavigation">
												<li><a href="#first">System</a></li>
												<li><a href="#second">User Defined</a></li>
											</ul>
											<div id="first">
												<div id="systemTree"></div>
											</div>
											<div id="second">
												<div id="userTree"></div>
											</div>
										</div>

									</td>

									<td valign="top"><!-- step content -->
										<div style="padding-left: 20px;">
											<table>
												<tr>
													<td>
														<table id="CATEGORIZATIONdefinition" class="specify-phenotype-form" data-definition-container="true">
															<tr>
																<td class="editor-description">This category data element may be used wherever its member</br>data elements are accepted.</td>
															</tr>
															<tr>
																<td>
																	<div class="tree-drop jstree-drop" title="Drop your category's system and/or user-defined data element members in here">
																		<div class="label-info" ><center>Drop Here</center></div>
																		<ul class="sortable" data-drop-type="multiple" data-proptype="empty" style="width: 100% height: 100%">
																			<c:if test="${not empty proposition and propositionType == 'CATEGORIZATION'}">
																				<c:forEach var="child" items="${proposition.children}">
																					<li data-key="${child.dataElementKey}" data-desc="${child.dataElementDisplayName}" data-type="${child.type}" data-subtype="${child.type == 'CATEGORIZATION' ? child.categoricalType : ''}" data-space="${proposition.inSystem ? 'system' : 'user'}">
																						<span class="delete" style="cursor: pointer; background-color: lightblue;" title="Remove this data element from the category"></span>
																						<span>${child.dataElementDisplayName} (${child.dataElementKey})</span>
																					</li>
																				</c:forEach>
																			</c:if>
																		</ul>
																	</div>
																</td>
															</tr>
														</table>
														<%--
														<table id="TEMPORALdefinition"  class="specify-phenotype-form"> <!-- DEPRECATED -->
															<tr>
																<td>
																	<div class="tree-drop jstree-drop">
																		<div class="label-info" ><center>Drop Here</center></div>
																		<ul class="sortable" style="width: 100% height: 100%">
																			<c:if test="${not empty proposition and propositionType == 'TEMPORAL'}">
																			<c:forEach var="child" items="${proposition.children}">
																			<li data-key="${child.key}" data-type="${child.type}" data-space="${proposition.inSystem ? 'sysem' : 'user'}">
																				<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																				<span>${child.key} ${child.displayName}</span>
																			</li>
																			</c:forEach>
																			</c:if>
																		</ul>
																	</div>
																</td>
															</tr>
														</table>
														--%>
														<table id="SEQUENCEdefinition" class="specify-phenotype-form" data-definition-container="true">
															<tr>
																<td class="editor-description">Computes intervals with the same start and stop time as the Main data element below when</br>the temporal relationships below are satisfied.</td>
															</tr>
															<tr>
																<td>
																	<fieldset>
																		<legend>Main data element</legend>
																	<table>
																		<tr>
																			<td>
																				<table>
																					<tr>
																						<td>
																							Name:
																						</td>
																						<td>
																							<div id="mainDataElement" class="tree-drop-single jstree-drop" title="Drag a system or user-defined data element in here">
																								<div class="label-info" ><center>Drop Here</center></div>
																								<ul data-type="main" data-drop-type="single" data-count="1" class="sortable" style="width: 100% height: 100%">
																									<c:if test="${not empty proposition and propositionType == 'SEQUENCE'}">
																										<li data-key="${proposition.primaryDataElement.dataElementKey}" data-desc="${proposition.primaryDataElement.dataElementDisplayName} (${proposition.primaryDataElement.dataElementKey})" data-space="${proposition.primaryDataElement.inSystem ? 'system' : 'user'}">
																											<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																											<span class="desc">${empty proposition.primaryDataElement.dataElementDisplayName ? '' : proposition.primaryDataElement.dataElementDisplayName} (${proposition.primaryDataElement.dataElementKey})</span>
																										</li>
																									</c:if>
																								</ul>
																							</div>
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<table>
																					<tr title="Optionally specify duration constraints for this data element">
																						<td>
																							<label><input type="checkbox" value="true" name="mainDataElementSpecifyDuration" <c:if test="${propositionType == 'SEQUENCE' and proposition.primaryDataElement.hasDuration}">checked="checked"</c:if> >with duration</label>
																							</td>
																							<td>
																								<table>
																									<tr>
																										<td>
																											at least
																										</td>
																										<td>
																											<input type="number" class="durationField" name="mainDataElementMinDurationValue" value="<c:if test="${propositionType == 'SEQUENCE'}">${proposition.primaryDataElement.minDuration}</c:if>"/>
																										</td>
																										<td>
																											<select name="mainDataElementMinDurationUnits">
																											<c:forEach var="unit" items="${timeUnits}">
																												<option value="${unit.id}" <c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryDataElement.minDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																											</c:forEach>
																										</select>
																									</td>
																								</tr>
																								<tr>
																									<td>
																										at most
																									</td>
																									<td>
																										<input type="number" class="durationField" name="mainDataElementMaxDurationValue" value="<c:if test="${propositionType == 'SEQUENCE'}">${proposition.primaryDataElement.maxDuration}</c:if>"/>
																										</td>
																										<td>
																											<select name="mainDataElementMaxDurationUnits">
																											<c:forEach var="unit" items="${timeUnits}">
																												<option value="${unit.id}" <c:if test="${propositionType == 'SEQUENCE' ? unit.id == proposition.primaryDataElement.maxDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																											</c:forEach>
																										</select>
																									</td>
																								</tr>
																							</table>
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<table>
																					<tr title="Optionally specify a property value constraint for this data element">
																						<td>
																							<label><input type="checkbox" class="propertyValueConstraint" value="true" name="mainDataElementSpecifyProperty" <c:if test="${propositionType == 'SEQUENCE' and proposition.primaryDataElement.hasPropertyConstraint}">checked="checked"</c:if>
																										  <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryDataElement.dataElementKey]}">disabled="disabled"</c:if>/>with property value</label>
																						</td>
																						<td>
																							<select name="mainDataElementPropertyName" data-properties-provider="mainDataElement">
																							<c:if test="${propositionType == 'SEQUENCE'}">
																								<c:forEach var="property" items="${properties[proposition.primaryDataElement.dataElementKey]}">
																									<option value="${property}" <c:if test="${proposition.primaryDataElement.property == property}">selected="selected"</c:if>>${property}</option>
																								</c:forEach>
																							</c:if>
																							</select>
																							<input type="text" class="propertyValueField" name="mainDataElementPropertyValue" value="<c:if test="${propositionType == 'SEQUENCE' and not empty proposition.primaryDataElement.propertyValue}">${proposition.primaryDataElement.propertyValue}</c:if>"
																								   <c:if test="${propositionType == 'SEQUENCE' and empty properties[proposition.primaryDataElement.dataElementKey]}">disabled="disabled"</c:if>
																								   />
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		</table>
																	</fieldset>
																	</td>
																</tr>
																<tr>
																	<td class="sequence-relations-container">
																	<c:choose>
																		<c:when test="${not empty proposition and propositionType == 'SEQUENCE' and not empty proposition.relatedDataElements}">
																			<c:forEach var="relation" items="${proposition.relatedDataElements}" varStatus="status">
																				<fieldset class="sequence-relation">
																					<legend>Related data element <span class="count">${status.count}</span></legend>
																				<table class="drop-parent" style="margin-top: 10px">
																					<tr>
																						<td>
																							<table>
																								<tr>
																									<td>Name:</td>
																									<td colspan="5">
																										<div id="relatedDataElement${status.count}" class="tree-drop-single jstree-drop sequencedDataElement"  title="Drag another system or user-defined data element in here">
																											<div class="label-info"><center>Drop Here</center></div>
																											<ul class="sortable" data-type="related" data-drop-type="single" data-count="${status.count + 1}" style="width: 100% height: 100%">
																												<li data-key="${relation.dataElementField.dataElementKey}" data-desc="${relation.dataElementField.dataElementDisplayName} (${relation.dataElementField.dataElementKey})" data-space="${relation.dataElementField.inSystem ? 'system' : 'user'}">
																													<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																													<span class="desc">${empty relation.dataElementField.dataElementDisplayName ? '' : relation.dataElementField.dataElementDisplayName} (${relation.dataElementField.dataElementKey})</span>
																												</li>
																											</ul>
																										</div>
																									</td>
																								</tr>
																							</table>
																						</td>
																					</tr>
																					<tr>
																						<td>
																							<table>
																								<tr title="Optionally specify duration constraints for this data element">
																									<td>
																										<label><input type="checkbox" value="true" name="sequenceRelDataElementSpecifyDuration" <c:if test="${relation.dataElementField.hasDuration}">checked="checked"</c:if> />with duration</label>
																										</td>
																										<td>
																											at least
																											<input type="number" class="durationField" name="sequenceRelDataElementMinDurationValue" value="${relation.dataElementField.minDuration}"/>
																										<select name="sequenceRelDataElementMinDurationUnits">
																											<c:forEach var="unit" items="${timeUnits}">
																												<option value="${unit.id}" <c:if test="${unit.id == relation.dataElementField.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																											</c:forEach>
																										</select>
																										<br />
																										at most
																										<input type="number" class="durationField" name="sequenceRelDataElementMaxDurationValue" value="${relation.dataElementField.maxDuration}"/>
																										<select name="sequenceRelDataElementMaxDurationUnits">
																											<c:forEach var="unit" items="${timeUnits}">
																												<option value="${unit.id}" <c:if test="${unit.id == relation.dataElementField.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																											</c:forEach>
																										</select>
																									</td>
																								</tr>
																							</table>
																						</td>
																					</tr>
																					<tr>
																						<td>
																							<table>
																								<tr title="Optionally specify a property value constraint for this data element">
																									<td>
																										<input type="checkbox" class="propertyValueConstraint" value="true" name="sequenceRelDataElementSpecifyProperty" <c:if test="${relation.dataElementField.hasPropertyConstraint}">checked="checked"</c:if> 
																											   <c:if test="${propositionType == 'SEQUENCE' and empty properties[relation.dataElementField.dataElementKey]}">disabled="disabled"</c:if>/>
																											   <label>with property value</label>
																										</td>
																										<td colspan="5">
																											<select name="sequenceRelDataElementPropertyName" data-properties-provider="relatedDataElement${status.count}">
																											<c:forEach var="property" items="${properties[relation.dataElementField.dataElementKey]}">
																												<option value="${property}" <c:if test="${relation.dataElementField.property == property}">selected="selected"</c:if>>${property}</option>
																											</c:forEach>
																										</select>

																										<input type="text" class="propertyValueField" name="sequenceRelDataElementPropertyValue" value="<c:if test="${not empty relation.dataElementField.propertyValue}">${relation.dataElementField.propertyValue}</c:if>"
																											   <c:if test="${empty properties[relation.dataElementField.dataElementKey]}">disabled="disabled"</c:if>
																												   />
																										</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<table>
																									<tr title="Specify the temporal relationship that intervals of this data element must have">
																										<td>
																											<select name="sequenceRelDataElementTemporalRelation">
																												<c:forEach var="op" items="${sequentialRelationOps}">
																													<option value="${op.id}" <c:if test="${op.id == relation.relationOperator}">selected="selected"</c:if>>${op.description}</option>
																												</c:forEach>
																											</select>
																										</td>
																										<td>
																											<select name="propositionSelect" style="width: 200px" data-sourceid="${relation.sequentialDataElementSource}"></select>
																										</td>
																										<td>
																											by
																										</td>
																										<td>
																											at least
																											<input type="text" class="distanceField" name="sequenceRhsDataElementMinDistanceValue" value="${relation.relationMinCount}" />
																											<select name="sequenceRhsDataElementMinDistanceUnits">
																												<c:forEach var="unit" items="${timeUnits}">
																													<option value="${unit.id}" <c:if test="${unit.id == relation.relationMinUnits}">selected="selected"</c:if>>${unit.description}</option>
																												</c:forEach>
																											</select>
																											<br />
																											at most
																											<input type="text" class="distanceField" name="sequenceRhsDataElementMaxDistanceValue" value="${relation.relationMaxCount}" />
																											<select name="sequenceRhsDataElementMaxDistanceUnits">
																												<c:forEach var="unit" items="${timeUnits}">
																													<option value="${unit.id}" <c:if test="${unit.id == relation.relationMaxUnits}">selected="selected"</c:if>>${unit.description}</option>
																												</c:forEach>
																											</select>
																										</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																					</table>
																				</fieldset>
																			</c:forEach>
																		</c:when>
																		<c:otherwise>
																			<fieldset class="sequence-relation">
																					<legend>Related data element <span class="count">1</span></legend>
																			<table class="drop-parent" style="margin-top: 10px">
																				<%-- For creating a new data element. --%>
																				<tr>
																					<td>
																						<table>
																							<tr>
																								<td>Name:</td>
																								<td colspan="5">
																									<div id="relatedDataElement1" class="tree-drop-single jstree-drop sequencedDataElement" title="Drag another system or user-defined data element in here">
																										<div class="label-info"><center>Drop Here</center></div>
																										<ul class="sortable" data-type="related" data-drop-type="single" data-count="2" style="width: 100% height: 100%"></ul>
																									</div>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<%--<tr>
																					<td>with value</td>
																					<td colspan="5"><select name="sequenceRelDataElementValue"></select></td>
																				</tr>--%>
																				<tr>
																					<td>
																						<table>
																							<tr title="Optionally specify a duration range that intervals of this data element must have">
																								<td>
																									<label><input type="checkbox" value="true" name="sequenceRelDataElementSpecifyDuration"/>with duration</label>
																								</td>
																								<td>
																									at least
																									<input type="number" class="durationField" name="sequenceRelDataElementMinDurationValue"/>
																									<select name="sequenceRelDataElementMinDurationUnits">
																										<c:forEach var="unit" items="${timeUnits}">
																											<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																										</c:forEach>
																									</select>
																									<br />
																									at most
																									<input type="number" class="durationField" name="sequenceRelDataElementMaxDurationValue"/>
																									<select name="sequenceRelDataElementMaxDurationUnits">
																										<c:forEach var="unit" items="${timeUnits}">
																											<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																										</c:forEach>
																									</select>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						<table>
																							<tr title="Optionally specify a property value that matching intervals of this data element must have">
																								<td>
																									<input type="checkbox" value="true" name="sequenceRelDataElementSpecifyProperty"/>
																									<label>with property value</label>
																								</td>
																								<td colspan="5">
																									<select name="sequenceRelDataElementPropertyName" data-properties-provider="relatedDataElement1"></select>
																									<input type="text" class="propertyValueField" name="sequenceRelDataElementPropertyValue"/>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						<table>
																							<tr title="Specify the temporal relationship that intervals of this data element must have">
																								<td>
																									<select name="sequenceRelDataElementTemporalRelation">
																										<c:forEach var="op" items="${sequentialRelationOps}">
																											<option value="${op.id}" <c:if test="${op.id == defaultRelationOp.id}">selected="selected"</c:if>>${op.description}</option>
																										</c:forEach>
																									</select>
																									<select name="propositionSelect" style="width: 200px"></select>
																									by
																								</td>
																								<td>
																									at least
																									<input type="text" class="distanceField" name="sequenceRhsDataElementMinDistanceValue"/>
																									<select name="sequenceRhsDataElementMinDistanceUnits">
																										<c:forEach var="unit" items="${timeUnits}">
																											<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																										</c:forEach>
																									</select>
																									<br />
																									at most
																									<input type="text" class="distanceField" name="sequenceRhsDataElementMaxDistanceValue"/>
																									<select name="sequenceRhsDataElementMaxDistanceUnits">
																										<c:forEach var="unit" items="${timeUnits}">
																											<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																										</c:forEach>
																									</select>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</c:otherwise>
																	</c:choose>

																</td>
															</tr>
															<tr>
																<td colspan="6" style="text-align: center">
																	<div class="action_link">
																		<a href="#" class="create"></a>
																		<a id="add-to-sequence" href="#" style="text-decoration:none">Add to sequence</a>
																	</div>
																</td>
															</tr>
														</table>
														<table id="FREQUENCYdefinition" class="specify-phenotype-form" data-definition-container="true">
															<tr><td colspan="3" class="editor-description">Computes an interval over the temporal extent of the intervals contributing to</br>the specified frequency count below.</td></tr>
															<tr>
																<td>
																	<select id="freqTypes" name="freqTypes" title="Specify whether only the first n intervals will be matched (First) or any n intervals (At least)">
																		<c:forEach var="freqType" items="${frequencyTypes}">
																			<option value="${freqType.id}" <c:if test="${propositionType == 'FREQUENCY' ? freqType.id == proposition.frequencyType : freqType.id == defaultFrequencyType.id}">selected="selected"</c:if>>${freqType.description}</option>
																		</c:forEach>
																	</select>
																</td>
																<td>
																	<input type="number" id="frequencyCountField" name="freqAtLeastField" min="1" value="<c:choose><c:when test="${propositionType == 'FREQUENCY'}">${proposition.atLeast}</c:when><c:otherwise><c:out value="1"/></c:otherwise></c:choose>" 
																	   title="Specify the frequency count"/>
																	<label id="valueThresholdConsecutiveLabel" title="For value threshold data elements, specifies whether no intervening values are present that do not match the threshold or any duration or property constraints specified below"<c:if test="${propositionType != 'FREQUENCY' or (not empty proposition and proposition.dataElement.type != 'VALUE_THRESHOLD')}">style="display:none"</c:if>><input type="checkbox" value="true" name="freqIsConsecutive" <c:if test="${propositionType == 'FREQUENCY' and proposition.isConsecutive}">checked="checked"</c:if> />consecutive</label>
																</td>
																<td>
																	<div id="freqMainDataElement" class="tree-drop-single jstree-drop" title="Specify a system or user-defined data element">
																		<div class="label-info" ><center>Drop Here</center></div>
																		<ul data-type="main" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
																			<c:if test="${not empty proposition and propositionType == 'FREQUENCY'}">
																				<li data-key="${proposition.dataElement.dataElementKey}" data-desc="${proposition.dataElement.dataElementDisplayName}" data-space="${proposition.dataElement.inSystem ? 'system' : 'user'}">
																					<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																					<span class="desc">${empty proposition.dataElement.dataElementDisplayName ? '' : proposition.dataElement.dataElementDisplayName} (${proposition.dataElement.dataElementKey})</span>
																				</li>
																			</c:if>
																		</ul>
																	</div>
																</td>
															</tr>
															<tr>
																<td colSpan="3">
																	<table>
																		<tr title="Optionally specify a duration range that intervals of this data element must have to be involved in the frequency count">
																			<td>
																				<label><input type="checkbox" value="true" name="freqDataElementSpecifyDuration" <c:if test="${propositionType == 'FREQUENCY' and proposition.dataElement.hasDuration}">checked="checked"</c:if> >with duration</label>
																				</td>
																				<td>
																					<table>
																						<tr>
																							<td>
																								at least
																							</td>
																							<td>
																								<input type="text" class="durationField" name="freqDataElementMinDurationValue" value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.dataElement.minDuration}</c:if>" />
																							</td>
																							<td>
																								<select name="freqDataElementMinDurationUnits">
																								<c:forEach var="unit" items="${timeUnits}">
																									<option value="${unit.id}" <c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.dataElement.minDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																								</c:forEach>
																							</select>
																						</td>
																					</tr>
																					<tr>
																						<td>
																							at most
																						</td>
																						<td>
																							<input type="text" class="durationField" name="freqDataElementMaxDurationValue" value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.dataElement.maxDuration}</c:if>" />
																							</td>
																							<td>
																								<select name="freqDataElementMaxDurationUnits">
																								<c:forEach var="unit" items="${timeUnits}">
																									<option value="${unit.id}" <c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.dataElement.maxDurationUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																								</c:forEach>
																							</select>
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr title="Optionally specify a property value that matching intervals of this data element must have to be involved in the frequency count">
																			<td>
																				<label><input type="checkbox" <c:if test="${propositionType == 'FREQUENCY' and proposition.dataElement.hasPropertyConstraint}">checked="checked"</c:if> name="freqDataElementSpecifyProperty" />with property value</label>
																				</td>
																				<td>

																					<select name="freqDataElementPropertyName" data-properties-provider="freqMainDataElement">
																					<c:if test="${propositionType == 'FREQUENCY'}">
																						<c:forEach var="property" items="${properties[proposition.dataElement.dataElementKey]}">
																							<option value="${property}" <c:if test="${proposition.dataElement.property == property}">selected="selected"</c:if>>${property}</option>
																						</c:forEach>
																					</c:if>
																				</select>

																				<input type="text" class="propertyValueField" name="freqDataElementPropertyValue" value="<c:if test="${propositionType == 'FREQUENCY' and not empty proposition.dataElement.propertyValue}">${proposition.dataElement.propertyValue}</c:if>"/>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td colSpan="3">
																		<table>
																			<tr title="Optionally specify constraints on the duration of time allowed between intervals of this data element involved in the frequency count">
																				<td>
																					<label><input type="checkbox" value="true" name="freqIsWithin" <c:if test="${propositionType == 'FREQUENCY' and proposition.isWithin}">checked="checked"</c:if> >within</label>
																				</td>
																				<td>
																					at least
																					<input type="text" class="distanceField" name="freqWithinAtLeast" value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.withinAtLeast}</c:if>" />
																					<select name="freqWithinAtLeastUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																						<option value="${unit.id}" <c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.withinAtLeastUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																					</c:forEach>
																				</select>
																				<br />
																				at most
																				<input type="text" class="distanceField" name="freqWithinAtMost" value="<c:if test="${propositionType == 'FREQUENCY'}">${proposition.withinAtMost}</c:if>"/>
																					<select name="freqWithinAtMostUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																						<option value="${unit.id}" <c:if test="${propositionType == 'FREQUENCY' ? unit.id == proposition.withinAtMostUnits : unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																					</c:forEach>
																				</select>
																			</td>
																			<td>
																				of each other
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
														<table id="VALUE_THRESHOLDdefinition" class="specify-phenotype-form" data-definition-container="true">
															<tr><td colspan="2" class="editor-description">Computes intervals corresponding to when the specified thresholds below are present.</td></tr>
															<tr>
																<td style="text-align: left">
																	<div class="action_link">
																		<a href="#" class="create"></a>
																		<a id="add-threshold" href="#" style="text-decoration:none">Add threshold</a>
																	</div>
																</td>
																<td style="text-align: left" title="Compute this phenotype when any of the thresholds below are present or only if all are present">
																	Value thresholds:
																	<select name="valueThresholdType">
																		<c:forEach var="operator" items="${thresholdsOperators}">
																			<option value="${operator.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and proposition.thresholdsOperator == operator.id}">selected="true"</c:if>>${operator.description}</option>
																		</c:forEach>
																	</select>
																</td>
															</tr>
															<tr>
																<td colspan="2">
																	<table class="value-thresholds-container">
																		<c:choose>
																			<c:when test="${not empty proposition and propositionType == 'VALUE_THRESHOLD' and not empty proposition.valueThresholds}">
																				<c:forEach var="threshold" items="${proposition.valueThresholds}" varStatus="status">
																					<tr class="value-threshold">
																						<td>
																							<fieldset>
																								<legend>Threshold</legend>
																							<table>
																								<tr>
																									<td>
																										<table>
																											<tr>
																												<td>
																													<div id="thresholdedDataElement${status.count}" class="tree-drop-single jstree-drop thresholdedDataElement" title="Drag and drop the system or user-defined data element with a numerical value to be thresholded">
																														<div class="label-info" ><center>Drop Thresholded Data Element Here</center></div>
																														<ul data-type="threshold1" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
																															<li data-key="${threshold.dataElement.dataElementKey}" data-desc="${threshold.dataElement.dataElementDisplayName}" data-space="${threshold.dataElement.inSystem ? 'system' : 'user'}">
																																<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																																<span class="desc">${empty threshold.dataElement.dataElementDisplayName ? '' : threshold.dataElement.dataElementDisplayName} (${threshold.dataElement.dataElementKey})</span>
																															</li>
																														</ul>
																													</div>
																												</td>
																												<td>with</td>
																												<td title="Specify lower and/or upper bounds on the value of the data element to be thresholded">
																													<table>
																														<tr>
																															<td>
																																Lower threshold
																															</td>
																															<td>
																																<select name="thresholdLowerComp">
																																	<c:forEach var="valComp" items="${valueComparatorsLower}">
																																		<option value="${valComp.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and valComp.id == threshold.lowerComp}">selected="selected"</c:if>>${valComp.name}</option>
																																	</c:forEach>
																																</select>
																															</td>
																															<td>
																																<input type="text" name="thresholdLowerVal" class="valueField" value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.lowerValue}</c:if>"/>
																															</td>
																														</tr>
																														<tr>
																																<td>
																																	Upper threshold
																																</td>
																																<td>
																																	<select name="thresholdUpperComp">
																																	<c:forEach var="valComp" items="${valueComparatorsUpper}">
																																		<option value="${valComp.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and valComp.id == threshold.upperComp}">selected="selected"</c:if>>${valComp.name}</option>
																																	</c:forEach>
																																</select>
																															</td>
																															<td>
																																<input type="text" name="thresholdUpperVal" class="valueField" value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.upperValue}</c:if>"/>
																															</td>
																														</tr>
																														</table>
																													</td>
																												</tr>
																											</table>
																										</td>
																									</tr>
																									<tr>
																										<td>
																											<table>
																												<tr>
																													<td>and</td>
																													<td>
																														<div id="thresholdRelatedDataElements${status.count}" class="tree-drop-multiple jstree-drop thresholdedRelatedDataElements" title="Drag and drop a contextual system or user-defined data element, an interval of which must be present to match this threshold">
																															<div class="label-info" ><center>Drop Contextual Data Element Here</center></div>
																															<ul data-proptype="empty" data-drop-type="multiple" class="sortable" style="width: 100% height: 100%">
																																<c:if test="${not empty proposition and propositionType == 'VALUE_THRESHOLD'}">
																																	<c:forEach var="relatedDataElement" items="${threshold.relatedDataElements}">
																																		<li data-key="${relatedDataElement.dataElementKey}" data-desc="${relatedDataElement.dataElementDisplayName}" data-type="${relatedDataElement.type}" data-subtype="${relatedDataElement.type == 'CATEGORIZATION' ? relatedDataElement.categoricalType : ''}" data-space="${relatedDataElement.inSystem ? 'system' : 'user'}">
																																			<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																																			<span>${relatedDataElement.dataElementDisplayName} (${relatedDataElement.dataElementKey})</span>
																																		</li>
																																	</c:forEach>
																																</c:if>
																															</ul>
																														</div>
																													</td>
																													<td>interval(s)</td>
																												</tr>
																											</table>
																										</td>
																									</tr>
																									<tr>
																										<td>
																											<table>
																												<tr>
																													<td title="Optionally specify duration constraints between intervals of the data element matching the threshold and this contextual data element">
																														<table>
																															<tr>
																																<td>
																																	at least
																																</td>
																																<td>
																																	<input type="text" class="distanceField" name="thresholdWithinAtLeast" value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.withinAtLeast}</c:if>"/>
																																</td>
																																<td>
																																	<select name="thresholdWithinAtLeastUnits">
																																		<c:forEach var="unit" items="${timeUnits}">
																																			<option value="${unit.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and unit.id == threshold.withinAtLeastUnit}">selected="selected"</c:if>>${unit.description}</option>
																																		</c:forEach>
																																	</select>
																																</td>
																															</tr>
																															<tr>
																																<td>
																																	at most
																																</td>
																																<td>
																																	<input type="text" class="distanceField" name="thresholdWithinAtMost" value="<c:if test="${propositionType == 'VALUE_THRESHOLD'}">${threshold.withinAtMost}</c:if>"/>
																																</td>
																																<td>
																																	<select name="thresholdWithinAtMostUnits">
																																		<c:forEach var="unit" items="${timeUnits}">
																																			<option value="${unit.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and unit.id == threshold.withinAtMostUnit}">selected="selected"</c:if>>${unit.description}</option>
																																		</c:forEach>
																																	</select>
																																</td>
																															</tr>
																														</table>
																													</td>
																													<td title="Specify a temporal relationship constraint between intervals of the data element matching the threshold and this contextual data element">
																														<select name="thresholdDataElementTemporalRelation">
																															<c:forEach var="op" items="${contextRelationOps}">
																																<option value="${op.id}" <c:if test="${propositionType == 'VALUE_THRESHOLD' and op.id == threshold.relationOperator}">selected="selected"</c:if>>${op.description}</option>
																															</c:forEach>
																														</select>
																													</td>
																													<td>it</td>
																												</tr>
																											</table>
																										</td>
																								</table>
																							</fieldset>
																						</td>
																					</tr>
																				</c:forEach>
																			</c:when>
																			<c:otherwise>
																				<tr class="value-threshold">
																					<td>
																						<fieldset>
																							<legend>Threshold</legend>
																						<table>
																							<tr>
																								<td>
																									<table>
																										<tr>
																											<td>
																												<div id="thresholdedDataElement1" class="tree-drop-single jstree-drop thresholdedDataElement" title="Drag and drop the system or user-defined data element with a numerical value to be thresholded">
																													<div class="label-info" ><center>Drop Thresholded Data Element Here</center></div>
																													<ul data-type="threshold1" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
																													</ul>
																												</div>
																											</td>
																											<td>with</td>
																											<td title="Specify lower and/or upper bounds on the value of the data element to be thresholded">
																												<table>
																													<tr>
																														<td>
																															Lower threshold
																														</td>
																														<td>
																															<select name="thresholdLowerComp">
																																<c:forEach var="valComp" items="${valueComparatorsLower}">
																																	<option value="${valComp.id}">${valComp.name}</option>
																																</c:forEach>
																															</select>
																														</td>
																														<td>
																															<input type="text" class="valueField" name="thresholdLowerVal"/>
																														</td>
																													</tr>
																													<tr>
																														<td>
																															Upper threshold
																														</td>
																														<td>
																															<select name="thresholdUpperComp">
																																<c:forEach var="valComp" items="${valueComparatorsUpper}">
																																	<option value="${valComp.id}">${valComp.name}</option>
																																</c:forEach>
																															</select>
																														</td>
																														<td>
																															<input type="text" class="valueField" name="thresholdUpperVal"/>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																									</table>
																								</td>
																							</tr>
																							<tr>
																								<td>
																									<table>
																										<tr>
																											<td>and</td>
																											<td>
																												<div id="thresholdRelatedDataElements1" class="tree-drop-multiple jstree-drop thresholdedRelatedDataElements" title="Drag and drop a contextual system or user-defined data element, an interval of which must be present to match this threshold">
																													<div class="label-info" ><center>Drop Contextual Data Element Here</center></div>
																													<ul data-proptype="empty" data-drop-type="multiple" class="sortable" style="width: 100% height: 100%">
																													</ul>
																												</div>
																											</td>
																											<td>interval(s)</td>
																										</tr>
																									</table>
																								</td>
																							</tr>
																							<tr>
																								<td>
																									<table>
																										<tr>
																											<td title="Optionally specify duration constraints between intervals of the data element matching the threshold and this contextual data element">
																												<table>
																													<tr>
																														<td>
																															at least
																														</td>
																														<td>
																															<input type="text" class="distanceField" name="thresholdWithinAtLeast" value=""/>
																														</td>
																														<td>
																															<select name="thresholdWithinAtLeastUnits">
																																<c:forEach var="unit" items="${timeUnits}">
																																	<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																																</c:forEach>
																															</select>
																														</td>
																													</tr>
																													<tr>
																														<td>
																															at most
																														</td>
																														<td>
																															<input type="text" class="distanceField" name="thresholdWithinAtMost" value=""/>
																														</td>
																														<td>
																															<select name="thresholdWithinAtMostUnits">
																																<c:forEach var="unit" items="${timeUnits}">
																																	<option value="${unit.id}" <c:if test="${unit.id == defaultTimeUnit.id}">selected="selected"</c:if>>${unit.description}</option>
																																</c:forEach>
																															</select>
																														</td>
																													</tr>
																												</table>
																											</td>
																											<td title="Specify a temporal relationship constraint between intervals of the data element matching the threshold and this contextual data element">
																												<select name="thresholdDataElementTemporalRelation">
																													<c:forEach var="op" items="${contextRelationOps}">
																														<option value="${op.id}" <c:if test="${op.id == defaultRelationOp.id}">selected="selected"</c:if>>${op.description}</option>
																													</c:forEach>
																												</select>
																											</td>
																											<td>it</td>
																										</tr>
																									</table>
																								</td>
																							</tr>
																						</table>
																						</fieldset>
																					</td>
																				</tr>
																			</c:otherwise>
																		</c:choose>
																	</table>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</div></td><!-- end step content -->
								</tr>
							</table>


						</div>
						<div id="step-3">
							<h2 class="StepTitle">
								<c:choose>
									<c:when test="${not empty proposition}">
										<span>Update the Phenotype's Name</span>
									</c:when>
									<c:otherwise>
										<span>Select a Name for the Phenotype</span>
									</c:otherwise>
								</c:choose>
								<span class="editor-help">
									<a href="${initParam['eureka-help-url']}/phenotypes.html#select-name" target="eureka-help">Help</a>
								</span>
							</h2>
								<p>Name and optionally describe your phenotype below:</p>
							<table>
								<tr>
									<td>
										<label>Name:</label>
									</td>
									<td>
										<c:choose>
											<c:when test="${not empty proposition}">
												<input type="hidden" id="propId" value="${proposition.id}" />
												<input type="hidden" id="propType" value="${propositionType}" />
												<input type="hidden" id="propSubType" value="${propositionType == 'CATEGORIZATION' ? proposition.categoricalType : ''}" />
												<input type="text" id="propDisplayName" value="${proposition.displayName}" />
											</c:when>
											<c:otherwise>
												<input type="text" id="propDisplayName"/>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td valign="top">
										<label>Description:</label>
									</td>
									<td valign="top">
										<textarea id="propDescription" ><c:if test="${not empty proposition}">${proposition.description}</c:if></textarea>
										</td>
									</tr>

								</table>

							</div>
							<div id="step-4">
								<h2 class="StepTitle">
									<span>Save Phenotype to Database</span>
									<span class="editor-help">
										<a href="${initParam['eureka-help-url']}/phenotypes.html#save" target="eureka-help">Help</a>
								</span>
							</h2>
							<p>
								<c:choose>
									<c:when test="${not empty proposition}">
										Save your changes to this phenotype.
									</c:when>
									<c:otherwise>
										Save the new phenotype.
									</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>

					<!-- End SmartWizard Content -->

				</td></tr>
		</table>

	</div>
</template:content>
<template:content name="subcontent">


</div>
</div>
</template:content>

</template:insert>
