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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


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
								<c:set var="types" value="categorical,temporal,sequence,frequency,valuethreshold" />
								<c:forTokens items="${types}" var="myType" delims="," varStatus="status">
									<tr>
										<td class="firstCol">
											<input type="radio" id="type" name="type" value="${myType}" <c:if test="${not empty proposition and proposition.type == '${myType}'}">CHECKED</c:if>/>
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
													<table id="categoricaldefinition" data-definition-container="true">
														<tr>
															<td>
																<div id="tree-drop-categorical" class="tree-drop jstree-drop">
																	<div class="label-info" ><center>Drop Here</center></div>
																	<ul class="sortable" data-drop-type="multiple" data-proptype="empty" style="width: 100% height: 100%">
																		<c:if test="${not empty proposition}">
																			<c:forEach var="child" items="${proposition.children}">
																				<c:choose>
																					<c:when test="${empty child.key}">
																						<li id="${child.id}" data-type="user">
																							<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																							<span>${child.abbrevDisplayName}</span>
																						</li>
																					</c:when>
																					<c:otherwise>
																						<li id="${child.key}" data-type="system">
																							<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																							<span>${child.key} ${child.displayName} ${child.abbrevDisplayName}</span>
																						</li>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																		</c:if>
																	</ul>
																</div>
															</td>
														</tr>
													</table>
													<table id="temporaldefinition"> <!-- DEPRECATED -->
														<tr>
															<td>
																<div id="tree-drop-temporal" class="tree-drop jstree-drop">
																	<div class="label-info" ><center>Drop Here</center></div>
																	<ul class="sortable" style="width: 100% height: 100%">
																		<c:if test="${not empty proposition}">
																			<c:forEach var="child" items="${proposition.children}">
																				<c:choose>
																					<c:when test="${empty child.key}">
																						<li id="${child.id}" data-type="user">
																						<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																						<span>${child.abbrevDisplayName}</span>
																						</li>
																					</c:when>
																					<c:otherwise>
																						<li id="${child.key}" data-type="system">
																							<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																							<span>${child.key} ${child.displayName} ${child.abbrevDisplayName}</span>
																						</li>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																		</c:if>
																	</ul>
																</div>
															</td>
														</tr>
													</table>
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
																				<c:if test="${not empty proposition}">
																				<c:forEach var="child" items="${proposition.children}">
																					<c:choose>
																						<c:when test="${empty child.key}">
																							<li data-id="${child.id}" data-type="user">
																								<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																								<span>${child.abbrevDisplayName}</span>
																							</li>
																						</c:when>
																						<c:otherwise>
																							<li data-id="${child.key}" data-type="system">
																								<span class="delete" style="cursor: pointer; background-color: lightblue;"></span>
																								<span>${child.key} ${child.displayName} ${child.abbrevDisplayName}</span>
																							</li>
																						</c:otherwise>
																					</c:choose>
																				</c:forEach>
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
																			<label><input type="checkbox" value="true" name="mainDataElementSpecifyDuration">with duration</label>
																		</td>
																		<td>
																			<table>
																				<tr>
																					<td>
																						at least
																					</td>
																					<td>
																						<input type="text" class="durationField" name="mainDataElementMinDurationValue"/>
																					</td>
																					<td>
																						<select name="mainDataElementMinDurationUnits">
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
																						</select>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						at most
																					</td>
																					<td>
																						<input type="text" class="durationField" name="mainDataElementMaxDurationValue"/>
																					</td>
																					<td>
																						<select name="mainDataElementMaxDurationUnits">
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
																						</select>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<label><input type="checkbox" value="true" name="mainDataElementSpecifyProperty"/>with property value</label>
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
																		<c:when test="${false}">
																			<%-- Populate the first related data element, if editing an existing derived element with a first related data element. --%>
																		</c:when>
																		<c:otherwise>
																			<%-- For creating a new data element. --%>
																			<tr>
																				<td>Related Data Element <span class="count">1</span>:</td>
																				<td colspan="5">
																					<div class="tree-drop-single jstree-drop">
																						<div class="label-info"><center>Drop Here</center></div>
																						<ul class="sortable" style="width: 100% height: 100%"></ul>
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
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
																					</select>
																					<br />
																					at most
																					<input type="text" class="durationField" name="sequenceRelDataElementMaxDurationValue"/>
																					<select name="sequenceRelDataElementMaxDurationUnits">
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
																					</select>
																				</td>
																				<td>
																					<select name="sequenceRelDataElementTemporalRelation">
																						<option value="BEFORE">Before</option>
																						<option value="AFTER">After</option>
																					</select>
																				</td>
																				<td>
																					<select name="propositionSelect"></select>
																				</td>
																				<td>
																					by
																				</td>
																				<td>
																					at least
																					<input type="text" class="distanceField" name="sequenceRhsDataElementMinDistanceValue"/>
																					<select name="sequenceRhsDataElementMinDistanceUnits">
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
																					</select>
																					<br />
																					at most
																					<input type="text" class="distanceField" name="sequenceRhsDataElementMaxDistanceValue"/>
																					<select name="sequenceRhsDataElementMaxDistanceUnits">
																							<option value="minutes">Minutes</option>
																							<option value="hours">Hours</option>
																							<option value="days" selected="selected">Days</option>
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
																		<%-- print second, third, etc. related data element. --%>
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
													<table id="frequencydefinition">
														<tr>
															<td>
																<div>
																	<p>Frequency Definition</p>
																</div>
															</td>
														</tr>
													</table>
													<table id="valuethresholddefinition">
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
