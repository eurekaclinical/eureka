<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

		 <div class="action_link">   
            <a href="${pageContext.request.contextPath}/protected/editor.jsp">Create New Element</a>
         </div>

         <div class="tooltip" id="tooltip"><div id="tree"></div></div>

         <table align="center" id="elements1" style="width: 100%">
                         <tr class="bold">
                                 <th>Name</th><th>Description</th><th>Type</th><th>Created Date</th><th>Last Modified</th>
                         </tr>
                         <c:forEach items="${props}" var="prop">
                                 <tr>
                                         <td><a href="#" onmouseover="showPopup(event)" id="${prop.attr['id']}">${prop.attr['abbrevDisplay']}</a></td>
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
