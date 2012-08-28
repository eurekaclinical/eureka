<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_editor.jsp">

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

        <table>
            <tr>
				<td colspan="5" class="bottom"><a href="${pageContext.request.contextPath}/editor.jsp">Create New Element</a></td>
            </tr>   
        </table>
         <table align="center" id="elements">
                         <tr>
                                 <th>Name</th><th>Description</th><th>Type</th><th>Created Date</th><th>Last Modified</th>
                         </tr>
                         <c:forEach items="${props}" var="prop">
                                 <tr>
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
