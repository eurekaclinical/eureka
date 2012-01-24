<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    
    <form id="form" name="form1" method="post" action="">
      <table>
      <tr class="bold">
        <td width="162">User Name</td>
        <td width="83">Login</td>
        <td width="121">Role</td>
        <td width="217">Email</td>
        <td width="179">Organization</td>
        <td width="100">Status</td>
      </tr>
      <c:forEach items="${users}" var="user">
      	<tr>
      		<td>${user.email}</td><td>&nbsp;</td>
      		<td>
      			<select>
				    <c:forEach var="role" items="${roles}">
				     <option>${role.role}</option>
				    </c:forEach>
				</select>
      		
      		</td>
      		
      		<td>&nbsp;</td><td>&nbsp;</td>
      		<td>&nbsp;</td>
      	</tr>
      </c:forEach>
    </table>
    <div class="pad_top" align="right"><input type="reset" name="Edit User" id="button" class="margin" value="Edit User"/></div>
 </form>

	</template:content>

<template:content name="subcontent">
		<div class="sub-content">
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