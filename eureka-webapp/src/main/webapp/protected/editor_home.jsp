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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_sidebar.jsp">

	<template:content name="sidebar">
         <div class="tooltip" id="tooltip" style="text-align: left">
                <div id="tree" style="background-color: #ffffff;
                                        height: 255px;
                                        overflow: auto;
                                        width: 380px">
                </div>
        </div>
	</template:content>

	<template:content name="content">
		<h3>Phenotype Editor</h3>
		<div id="dialog" title="Delete Data Element"></div>
		<div class="action_link">   
			<a href="${pageContext.request.contextPath}/protected/editprop" class="create"></a>
			<a href="${pageContext.request.contextPath}/protected/editprop" style="text-decoration:none">Create New Element</a>
		</div>
		<table align="center" id="elements">
			<tr class="bold" >
				<th>Action</th><th>Name</th><th>Description</th><th>Type</th><th>Created Date</th><th>Last Modified</th>
			</tr>
			<c:forEach items="${props}" var="prop">
				<c:url value="/protected/editprop" var="editUrl">
					<c:param name="key" value="${prop.attr['key']}"/>
				</c:url>
				<tr class="editor-home-data-element" data-key="${prop.attr['key']}" data-display-name="${prop.attr['displayName']}">
					<td style="width:60px">
						<a href="#" class="view" title="View"></a>
						<a href="${editUrl}" class="edit" title="Edit"></a>
						<a href="#" class="delete" title="Delete"></a>
					</td>
					<td style="width: 100px">${prop.attr['displayName']}</td>
					<td>${prop.attr['description']}</td>
					<td>${prop.attr['type']}</td>
					<td>${prop.attr['created']}</td>
					<td>${prop.attr['lastModified']}</td>
				</tr>
			</c:forEach>
		</table>
	</template:content>

</template:insert>
