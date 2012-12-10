<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 Emory University
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
										Select Type<br />
										<small>Select Type of Element</small>
									</span>
								</a></li>
							<li><a href="#step-2">
									<label class="stepNumber">2</label>
									<span class="stepDesc">
										Select Elements<br />
										<small>Select Elements<br />from
										Ontology</small>
									</span>
								</a>
							<li><a href="#step-3">
									<label class="stepNumber">3</label>
									<span class="stepDesc">
										<c:choose>
											<c:when test="${not empty proposition}">
												Update Name<br />
												<small>Update the
												Element&apos;s Name</small>
											</c:when>
											<c:otherwise>
												Select Name<br />
												<small>Select a Name<br />for
												the Element</small>
											</c:otherwise>
										</c:choose>
									</span>
								</a></li>
							<li><a href="#step-4">
									<label class="stepNumber">4</label>
									<span class="stepDesc">
										Save<br />
										<small>Save Element to Database</small>
									</span>
								</a></li>
						</ul>
						<div id="step-1">
							<h2 class="StepTitle">Select Type of Element</h2>
							<p><br/></p>
							<table id="select_element_table">
								<%-- These values come from the DataElement.Type enum --%>
								<c:set var="types" value="categorization,sequence,frequency,value_threshold" />
								<c:forTokens items="${types}" var="myType" delims="," varStatus="status">
									<tr>
										<td class="firstCol">
											<input type="radio" id="type" name="type" value="${myType}" <c:if test="${not empty proposition and propositionType == myType}">checked="checked"</c:if>/>
											<fmt:message key="dataelementtype.${myType}.displayName" />
										</td>
										<td class="secondCol"><fmt:message key="dataelementtype.${myType}.description" /></td>
									</tr>
								</c:forTokens>
							</table>
						</div>
						<div id="step-2">
							<h2 class="StepTitle">
								<span>Select Elements from Ontology Explorer</span>
								<span style="font-size: 10px; float:right">
									<a href="#" id="help_select">Help</a>
								</span>
							</h2>
							<p>
								&nbsp;
							</p>
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
									<div style="height: 290px; overflow:auto; padding-left: 20px;">
										<table>
											<tr>
												<td>
													<table id="categorizationdefinition" data-definition-container="true">
														<tr>
															<td>
																<div class="tree-drop jstree-drop">
																	<div class="label-info" ><center>Drop Here</center></div>
																	<ul class="sortable" data-drop-type="multiple" data-proptype="empty" style="width: 100% height: 100%">
																		<c:if test="${not empty proposition and propositionType == 'categorization'}">
																		<c:forEach var="child" items="${proposition.children}">
																		<li data-key="${child.key}" data-desc="${child.abbrevDisplayName}" data-type="${child.type}" data-space="${proposition.inSystem ? 'sysem' : 'user'}">
																			<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																			<span>${child.displayName} ${child.abbrevDisplayName} (${child.key})</span>
																		</li>
																		</c:forEach>
																		</c:if>
																	</ul>
																</div>
															</td>
														</tr>
													</table>
													<%--
													<table id="temporaldefinition"> <!-- DEPRECATED -->
														<tr>
															<td>
																<div class="tree-drop jstree-drop">
																	<div class="label-info" ><center>Drop Here</center></div>
																	<ul class="sortable" style="width: 100% height: 100%">
																		<c:if test="${not empty proposition and propositionType == 'temporal'}">
																		<c:forEach var="child" items="${proposition.children}">
																		<li data-key="${child.key}" data-type="${child.type}" data-space="${proposition.inSystem ? 'sysem' : 'user'}">
																			<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																			<span>${child.key} ${child.displayName} ${child.abbrevDisplayName}</span>
																		</li>
																		</c:forEach>
																		</c:if>
																	</ul>
																</div>
															</td>
														</tr>
													</table>
													--%>
													<table id="sequencedefinition" data-definition-container="true">
														<tr>
															<td>
																<table>
																	<tr>
																		<td>
																			Data Element:
																		</td>
																		<td>
																			<div class="tree-drop-single jstree-drop">
																				<div class="label-info" ><center>Drop Here</center></div>
																				<ul data-type="main" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
																					<c:if test="${not empty proposition and propositionType == 'sequence'}">
																					<li data-key="${proposition.primaryDataElement.dataElementKey}" data-desc="${proposition.primaryDataElement.dataElementAbbrevDisplayName}" data-space="user">
																						<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																						<span>${proposition.primaryDataElement.dataElementKey} ${proposition.primaryDataElement.dataElementAbbrevDisplayName}</span>
																					</li>
																					</c:if>
																				</ul>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			with value
																		</td>
																		<td>
																			<select name="mainDataElementValue"></select>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<label><input type="checkbox" value="true" name="mainDataElementSpecifyDuration" <c:if test="${propositionType == 'sequence' and not empty proposition.primaryDataElement.hasDuration}">checked="checked"</c:if> >with duration</label>
																		</td>
																		<td>
																			<table>
																				<tr>
																					<td>
																						at least
																					</td>
																					<td>
																						<input type="text" class="durationField" name="mainDataElementMinDurationValue" value="<c:if test="${propositionType == 'sequence'}">${proposition.primaryDataElement.minDuration}</c:if>" />
																					</td>
																					<td>
																						<select name="mainDataElementMinDurationUnits">
																							<c:forEach var="unit" items="${timeUnits}">
																							<option value="${unit.id}" <c:if test="${propositionType == 'sequence' and unit.id == proposition.primaryDataElement.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																							</c:forEach>
																						</select>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						at most
																					</td>
																					<td>
																						<input type="text" class="durationField" name="mainDataElementMaxDurationValue" value="<c:if test="${propositionType == 'sequence'}">${proposition.primaryDataElement.maxDuration}</c:if>" />
																					</td>
																					<td>
																						<select name="mainDataElementMaxDurationUnits">
																							<c:forEach var="unit" items="${timeUnits}">
																							<option value="${unit.id}" <c:if test="${propositionType == 'sequence' and unit.id == proposition.primaryDataElement.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																							</c:forEach>
																						</select>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<label><input type="checkbox" value="true" name="mainDataElementSpecifyProperty" />with property value</label>
																		</td>
																		<td>
																		  <select name="mainDataElementPropertyName"></select>
																		  <input type="text" class="propertyValueField" name="mainDataElementPropertyValue"/>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td class="sequence-relations-container">
																<table class="sequence-relation drop-parent" style="margin-top: 10px">
																	<c:choose>
																	<c:when test="${not empty proposition and propositionType == 'sequence' and not empty proposition.relatedDataElements}">
																	<c:forEach var="relation" items="${proposition.relatedDataElements}" varStatus="status">
																	<tr>
																		<td>Related Data Element <span class="count">${status.count}</span>:</td>
																		<td colspan="5">
																			<div class="tree-drop-single jstree-drop">
																				<div class="label-info"><center>Drop Here</center></div>
																				<ul class="sortable" data-type="related" data-drop-type="single" style="width: 100% height: 100%">
																					<li data-key="${relation.dataElementField.dataElementKey}" data-desc="${relation.dataElementField.dataElementAbbrevDisplayName}" data-space="user">
																						<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																						<span>${relation.dataElementField.dataElementKey}</span>
																					</li>
																				</ul>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td>with value</td>
																		<td colspan="5"><select name="sequenceRelDataElementValue"></select></td>
																	</tr>
																	<tr>
																		<td>
																			<label><input type="checkbox" value="true" name="sequenceRelDataElementSpecifyDuration" <c:if test="${relation.dataElementField.hasDuration}">checked="checked"</c:if> />with duration</label>
																		</td>
																		<td>
																			at least
																			<input type="text" class="durationField" name="sequenceRelDataElementMinDurationValue" value="${relation.dataElementField.minDuration}" />
																			<select name="sequenceRelDataElementMinDurationUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}" <c:if test="${unit.id == relation.dataElementField.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																					</c:forEach>
																			</select>
																			<br />
																			at most
																			<input type="text" class="durationField" name="sequenceRelDataElementMaxDurationValue" value="${relation.dataElementField.maxDuration}" />
																			<select name="sequenceRelDataElementMaxDurationUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}" <c:if test="${unit.id == relation.dataElementField.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																					</c:forEach>
																			</select>
																		</td>
																		<td>
																			<select name="sequenceRelDataElementTemporalRelation">
																				<c:forEach var="op" items="${operators}">
																				<option value="${op.id}" <c:if test="${op.id == relation.relationOperator}">selected="selected"</c:if>>${op.description}</option>
																				</c:forEach>
																			</select>
																		</td>
																		<td>
																			<select name="propositionSelect" style="width: 200px"></select>
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
																	<tr>
																		<td>
																			<input type="checkbox" value="true" name="sequenceRelDataElementSpecifyProperty"/>
																			<label>with property value</label>
																		</td>
																		<td colspan="5">
																			<select name="sequenceRelDataElementPropertyName"></select>
																			<input type="text" class="propertyValueField" name="sequenceRelDataElementPropertyValue"/>
																		</td>
																	</tr>
																	</c:forEach>
																	</c:when>
																	<c:otherwise>
																	<%-- For creating a new data element. --%>
																	<tr>
																		<td>Related Data Element <span class="count">1</span>:</td>
																		<td colspan="5">
																			<div class="tree-drop-single jstree-drop">
																				<div class="label-info"><center>Drop Here</center></div>
																				<ul class="sortable" data-type="related" data-drop-type="single" style="width: 100% height: 100%"></ul>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td>with value</td>
																		<td colspan="5"><select name="sequenceRelDataElementValue"></select></td>
																	</tr>
																	<tr>
																		<td>
																			<label><input type="checkbox" value="true" name="sequenceRelDataElementSpecifyDuration"/>with duration</label>
																		</td>
																		<td>
																			at least
																			<input type="text" class="durationField" name="sequenceRelDataElementMinDurationValue"/>
																			<select name="sequenceRelDataElementMinDurationUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}">${unit.description}</option>
																					</c:forEach>
																			</select>
																			<br />
																			at most
																			<input type="text" class="durationField" name="sequenceRelDataElementMaxDurationValue"/>
																			<select name="sequenceRelDataElementMaxDurationUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}">${unit.description}</option>
																					</c:forEach>
																			</select>
																		</td>
																		<td>
																			<select name="sequenceRelDataElementTemporalRelation">
																				<c:forEach var="op" items="${operators}">
																				<option value="${op.id}">${op.description}</option>
																				</c:forEach>
																			</select>
																		</td>
																		<td>
																			<select name="propositionSelect" style="width: 200px"></select>
																		</td>
																		<td>
																			by
																		</td>
																		<td>
																			at least
																			<input type="text" class="distanceField" name="sequenceRhsDataElementMinDistanceValue"/>
																			<select name="sequenceRhsDataElementMinDistanceUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}">${unit.description}</option>
																					</c:forEach>
																			</select>
																			<br />
																			at most
																			<input type="text" class="distanceField" name="sequenceRhsDataElementMaxDistanceValue"/>
																			<select name="sequenceRhsDataElementMaxDistanceUnits">
																					<c:forEach var="unit" items="${timeUnits}">
																					<option value="${unit.id}">${unit.description}</option>
																					</c:forEach>
																			</select>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<input type="checkbox" value="true" name="sequenceRelDataElementSpecifyProperty"/>
																			<label>with property value</label>
																		</td>
																		<td colspan="5">
																			<select name="sequenceRelDataElementPropertyName"></select>
																			<input type="text" class="propertyValueField" name="sequenceRelDataElementPropertyValue"/>
																		</td>
																	</tr>
																	</c:otherwise>
																	</c:choose>
																</table>
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
													<table id="frequencydefinition" data-definition-container="true">
														<tr>
															<td>
																<table>
																	<tr>
																		<td>
																			At least
																		</td>
																		<td>
																			<input type="number" class="frequencyCountField" name="freqAtLeastField" value="<c:if test="${propositionType == 'frequency'}">${proposition.atLeast}</c:if>" />
																		</td>
																		<td>
																			<label><input type="checkbox" value="true" name="freqIsConsecutive" <c:if test="${propositionType == 'frequency' and proposition.isConsecutive}">checked="checked"</c:if> />consecutive</label>
																		</td>
																		<td>
																			<table>
																				<tr>
																					<td>
																						<div class="tree-drop-single jstree-drop">
																							<div class="label-info" ><center>Drop Here</center></div>
																							<ul data-type="main" data-drop-type="single" class="sortable" style="width: 100% height: 100%">
																								<c:if test="${not empty proposition and propositionType == 'frequency'}">
																								<li data-key="${proposition.dataElement.dataElementKey}" data-desc="${proposition.dataElement.dataElementAbbrevDisplayName}" data-space="user">
																									<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																									<span>${proposition.dataElement.dataElementKey} ${proposition.dataElement.dataElementAbbrevDisplayName}</span>
																								</li>
																								</c:if>
																							</ul>
																						</div>
																					</td>
																					
																				</tr>
																				<tr>
																					<td>
																						<table>
																							<tr>
																								<td>
																									with value
																								</td>
																								<td>
																									<select name="freqDataElementValue"></select>
																								</td>
																							</tr>
																							<tr>
																								<td>
																									<label><input type="checkbox" value="true" name="freqDataElementSpecifyDuration" <c:if test="${propositionType == 'frequency' and not empty proposition.dataElement.hasDuration}">checked="checked"</c:if> >with duration</label>
																								</td>
																								<td>
																									<table>
																										<tr>
																											<td>
																												at least
																											</td>
																											<td>
																												<input type="text" class="durationField" name="freqDataElementMinDurationValue" value="<c:if test="${propositionType == 'frequency'}">${proposition.dataElement.minDuration}</c:if>" />
																											</td>
																											<td>
																												<select name="freqDataElementMinDurationUnits">
																													<c:forEach var="unit" items="${timeUnits}">
																													<option value="${unit.id}" <c:if test="${propositionType == 'frequency' and unit.id == proposition.dataElement.minDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																													</c:forEach>
																												</select>
																											</td>
																										</tr>
																										<tr>
																											<td>
																												at most
																											</td>
																											<td>
																												<input type="text" class="durationField" name="freqDataElementMaxDurationValue" value="<c:if test="${propositionType == 'frequency'}">${proposition.dataElement.maxDuration}</c:if>" />
																											</td>
																											<td>
																												<select name="freqDataElementMaxDurationUnits">
																													<c:forEach var="unit" items="${timeUnits}">
																													<option value="${unit.id}" <c:if test="${propositionType == 'frequency' and unit.id == proposition.dataElement.maxDurationUnits}">selected="selected"</c:if>>${unit.description}</option>
																													</c:forEach>
																												</select>
																											</td>
																										</tr>
																									</table>
																								</td>
																							</tr>
																							<tr>
																								<td>
																									<label><input type="checkbox" value="true" name="freqDataElementSpecifyProperty" />with property value</label>
																								</td>
																								<td>
																								  <select name="freqDataElementPropertyName"></select>
																								  <input type="text" class="propertyValueField" name="freqDataElementPropertyValue"/>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																		<td>
																			<table>
																				<tr>
																					<td>
																						<label><input type="checkbox" value="true" name="freqIsWithin" <c:if test="${propositionType == 'frequency' and proposition.isWithin}">checked="checked"</c:if> >within</label>
																					</td>
																					<td>
																						at least
																						<input type="text" class="distanceField" name="freqWithinAtLeast" value="<c:if test="${propositionType == 'frequency'}">${proposition.dataElement.withinAtLeast}</c:if>" />
																						<select name="freqWithinAtLeastUnits">
																								<c:forEach var="unit" items="${timeUnits}">
																									<option value="${unit.id}" <c:if test="${propositionType == 'frequency' and unit.id == proposition.dataElement.withinAtLeastUnits}">selected="selected"</c:if>>${unit.description}</option>
																								</c:forEach>
																						</select>
																						<br />
																						at most
																						<input type="text" class="distanceField" name="freqWithinAtMost" value="<c:if test="${propositionType == 'frequency'}">${proposition.dataElement.withinAtMost}</c:if>"/>
																						<select name="freqWithinAtMostUnits">
																								<c:forEach var="unit" items="${timeUnits}">
																								<option value="${unit.id}" <c:if test="${propositionType == 'frequency' and unit.id == proposition.dataElement.withinAtMostUnits}">selected="selected"</c:if>>${unit.description}</option>
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
																
																
															</td>
														</tr>
													</table>
													<table id="valuethresholddefinition" data-definition-container="true">
														<tr>
															<td>
																<div>
																	<p>Value Threshold Definition</p>
																</div>
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
										Update the Derived Element's Name
									</c:when>
									<c:otherwise>
										Select a Name for the Derived Element
									</c:otherwise>
								</c:choose>
							</h2>
							<table>
								<tr>
									<td>
										<label>Name:</label>
									</td>
									<td>
										<c:choose>
											<c:when test="${not empty proposition}">
												<input type="hidden" id="propId" value="${proposition.id}" />
												<input type="text" id="propAbbrevDisplayName" style="width:250px" value="${proposition.abbrevDisplayName}" />
											</c:when>
											<c:otherwise>
												<input type="text" id="propAbbrevDisplayName" style="width:250px"/>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
								<tr>
									<td valign="top">
										<label>Description:</label>
									</td>
									<td valign="top">
										<textarea id="propDisplayName" ><c:if test="${not empty proposition}">${proposition.displayName}</c:if></textarea>
										</td>
									</tr>

								</table>

							</div>
							<div id="step-4">
								<h2 class="StepTitle">Save Element to Database</h2>
								<p>
									Save the new element to the Database as a User Defined Element.
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
