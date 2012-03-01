<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		    <img src="${pageContext.request.contextPath}/images/data_analysis.jpg" />
	</template:content>

	<template:content name="content">
	<script type="text/javascript">

	$(document).ready(function() {
	
	});

</script>

    <h3>Administration </h3>

    
    <form id="form" name="form1" method="post" action="" class="pad_top pad">
      <table>
      <tr class="bold">
        <td width="350">User Name</td>
        <td width="100">Last Login</td>
        <td width="300">Role</td>
        <td width="217">Email</td>
        <td width="179">Organization</td>
        <td width="100">Status</td>
      </tr>
       <c:forEach items="${users}" var="user">
      	<tr>
      		<td>
      			<c:set var="is_admin" value="false" />
      			<c:set var="is_inactive" value="false" />
      			<c:forEach var="role" items="${user.roles}">
      				<c:if test="${role.name == 'admin'}">
				      	<c:set var="is_admin" value="true" />      				
      				</c:if>      				
      			</c:forEach>      		
      			<c:if test="${user.active == false}">
	      			<c:set var="is_inactive" value="true" />      				
      			</c:if>	
      			<c:choose>
      				<c:when test="${is_inactive == true}">
					      	<img src="${pageContext.request.contextPath}/images/New_User.gif"/>      				
      				</c:when>
      				
      				<c:otherwise>
	      			  <c:choose>
					      <c:when test="${is_admin == true}">
					      	<img src="${pageContext.request.contextPath}/images/Role_Admin.gif"/>
					      </c:when> 
	      			  	  <c:otherwise>
					      	<img src="${pageContext.request.contextPath}/images/Role_Researcher.gif"/>      			  	  	
	      			  	  </c:otherwise>
      			  </c:choose>
      				
      				</c:otherwise>
      			</c:choose>
      			
      			<a href="${pageContext.request.contextPath}/protected/admin?id=${user.id}&action=edit">${user.firstName} ${user.lastName}</a></td>
      		<td>
      			<fmt:formatDate value="${user.lastLogin}" type="both" dateStyle="short" timeStyle="short" /> 
      		</td>
      		<td>
			    <c:forEach var="role" items="${user.roles}">
			    ${role.name} 
			    </c:forEach>      		
      		</td>
      		<td>${user.email}</td>
      		<td>${user.organization}</td>
      		<td>
      		<c:choose>
   			<c:when test="${user.active == true}">
   				Active
   			</c:when>
   			<c:otherwise>
   				Inactive
   			</c:otherwise>
   			</c:choose>
   			</td>
      	</tr>
      </c:forEach>
    </table>
<!--     
 We aren't using the edit button
 <div class="pad_top" align="right"><input type="reset" name="Edit User" id="button" class="margin" value="Edit User"/></div>
-->
 </form>

	</template:content>

<template:content name="subcontent">
		<div>
			<div id="release_notes">
				<h3>
					<img src="${pageContext.request.contextPath}/images/rss.png"
						border="0" /> Related News <a href="xml/rss_news.xml" class="rss"></a>
				</h3>
				<script language="JavaScript"
					src="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=1&utf=y"
					charset="UTF-8" type="text/javascript"></script>

				<noscript>
					<a style="padding-left: 35px"
						href="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=200>1&utf=y&html=y">View
						RSS feed</a>
				</noscript>

			</div>
		</div>
	</template:content>

</template:insert>