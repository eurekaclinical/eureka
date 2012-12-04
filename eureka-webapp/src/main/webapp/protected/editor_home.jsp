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


<template:insert template="/templates/eureka_main.jsp">

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
		<div id="dialog" title="Confirm Remove Selected Element"></div>
		<div class="action_link">   
			<a href="${pageContext.request.contextPath}/protected/editprop" class="create"></a>
			<a href="${pageContext.request.contextPath}/protected/editprop" style="text-decoration:none">Create New Element</a>
		</div>
		<table align="center" id="elements" style="width: 98%">
			<tr class="bold" >
				<th>Action</th><th>Name</th><th>Description</th><th>Type</th><th>Created Date</th><th>Last Modified</th>
			</tr>
			<c:forEach items="${props}" var="prop">
			<tr>
				<td style="width:60px">
					<a href="#" onclick="showPopup(event, '${prop.attr['key']}')" class="view" title="View"></a>
					<a href="${pageContext.request.contextPath}/protected/editprop?key=${prop.attr['key']}" class="edit" title="Edit"></a>
					<a href="#" onclick="deleteElement(event, '${prop.attr['key']}')" class="delete" title="Delete"></a>
				</td>
				<td style="width: 100px">${prop.attr['abbrevDisplay']}</td>
				<td>${prop.attr['displayName']}</td>
				<td>${prop.attr['type']}</td>
				<td>${prop.attr['created']}</td>
				<td>${prop.attr['lastModified']}</td>
			</tr>
			</c:forEach>
		</table>
	</template:content>

	<template:content name="subcontent">
			</div>
		</div>
	</template:content>

</template:insert>
