<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_editor.jsp">

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

         <table align="center" border="0" cellpadding="0" cellspacing="0">
                         <tr>
                                 <td>id</td><td>abbrev. display name</td><td>display name</td><td>created</td><td>last modified</td>
                         </tr>
                         <c:forEach items="${props}" var="prop">
                                 <tr>
                                         <td>${prop.id}</td>
                                         <td>${prop.attr['abbrevDisplay']}</td>
                                         <td>${prop.attr['displayName']}</td>
                                         <td>${prop.attr['created']}</td>
                                         <td>${prop.attr['lastModified']}</td>
                                 </tr>

                         </c:forEach>
         </table>
		
		<a href="${pageContext.request.contextPath}/editor.jsp">Create New Element</a>

	</template:content>
	<template:content name="subcontent">
		

			</div>
		</div>
	</template:content>

</template:insert>
