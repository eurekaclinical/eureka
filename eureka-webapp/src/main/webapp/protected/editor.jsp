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
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_editor.jsp">

    <template:content name="sidebar">
        <img
            src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>

    <template:content name="content">

        <table align="center" border="0" cellpadding="0" cellspacing="0">
            <tr><td> 
                    <!-- Tabs -->
                    <div id="dialog" title="Confirm Remove Selected Element">

                    </div>
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
                                        <small>Select Elements from Ontology</small>
                                    </span>
                                </a>
                            <li><a href="#step-3">
                                    <label class="stepNumber">3</label>
                                    <span class="stepDesc">
                                        <c:choose>
                                            <c:when test="${not empty proposition}">
                                                Update Element<br />
                                                <small>Update an Existing Derived Element</small>
                                            </c:when>
                                            <c:otherwise>
                                                Create Element<br />
                                                <small>Create a New Derived Element</small>
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
                            <table id="select_element_table" width="100%">
                                <tr>
                                    <td width="100px" style="display: inline-block">
                                        <input type="radio" id="type" name="type" value="OR" <c:if test="${not empty proposition and proposition.type == 'OR'}">CHECKED</c:if>/>Categorical</td>
                                    <td>For defining a significant category of codes or clinical
                                        events or observations.</td>
                                </tr>
                                <tr>
                                    <td width="100px" style="display: inline-block">
                                        <input type="radio" name="type" id="type" value="AND" <c:if test="${not empty proposition and proposition.type == 'AND'}">CHECKED</c:if>/>Temporal</td>
                                    <td>For defining a disease, finding or patient care process to be
                                        reflected by codes,clinical events and/or observations in a specified frequency, sequential or other temporal patterns.</td>
                                </tr>
                            </table>
                        </div>
                        <div id="step-2">
                            <h2 class="StepTitle"><span>Select Elements from Ontology Explorer</span><span style="font-size: 10px; float:right"><a href="#" id="help_select">Help</a></span></h2>	

                            <p>
                                &nbsp;
                            </p>
                            <table style="width: 650px; " id="element_tree_table">
                                <tr>
                                    <td  valign="top" width="25%">
                                        <div class="tabs" style="float: left;" >
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

                                    <td valign="top">

                                        <table>
                                            <tr>
                                                <td colspan="2">
                                                    <h3 style="width: 300px"></h3>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <div id="tree-drop" 
                                                                     class="jstree-drop">

                                                                    <ul id="sortable" style="width: 100% height: 100% >
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
                                                                    <div id="holder" style="width: 550px; height: 350px" ></div>
                                                                    <div id="label-info" class="jstree-drop" ><center>Drop Here</center></div>


                                                                </div>

                                                            </td>
                                                        </tr>
                                                    </table>

                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>            			
                            </table>


                        </div>                      
                        <div id="step-3">	
                            <h2 class="StepTitle">
                                <c:choose>
                                    <c:when test="${not empty proposition}">
                                        Update an Existing Derived Element
                                    </c:when>
                                    <c:otherwise>
                                        Create a New Derived Element
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
                                                <input type="text" id="element_name" style="width:250px" value="${proposition.abbrevDisplayName}" />
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" id="element_name" style="width:250px"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <label>Description:</label>
                                    </td>
                                    <td valign="top">
                                        <textarea id="element_description" ><c:if test="${not empty proposition}">${proposition.displayName}</c:if></textarea>
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
