<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
         <div class="tooltip" id="tooltip" style="text-align: left">
                <div id="tree" style="height: 255px;
                                        overflow: auto;
                                        width: 380px">
                </div>
        </div>
	</template:content>

	<template:content name="content">

        <div id="dialog" title="Confirm Remove Selected Element"></div>
		 <div class="action_link">   
            <a href="${pageContext.request.contextPath}/protected/editor.jsp" class="create"></a>
            <a href="${pageContext.request.contextPath}/protected/editor.jsp" style="text-decoration:none">Create New Element</a>
         </div>


                <table align="center" id="elements1" style="width: 98%">
                         <tr class="bold" >
                                 <th>Action</th><th>Name</th><th>Description</th><th>Type</th><th>Created Date</th><th>Last Modified</th>
                         </tr>
                         <c:forEach items="${props}" var="prop">
                
                                <tr>
                                         <td>
                                            <a href="#" onclick="showPopup(event, ${prop.attr['id']})" class="view"></a>
                                            <a href="${pageContext.request.contextPath}/protected/editprop?id=${prop.attr['id']}" class="edit"></a>
                                            <a href="#" onclick="deleteElement(event, ${prop.attr['id']})" class="delete"></a>
                                        </td>
                                         <td>${prop.attr['abbrevDisplay']}</td>
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
