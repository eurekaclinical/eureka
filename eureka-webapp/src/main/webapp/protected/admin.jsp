<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
	<script type="text/javascript">

	$(document).ready(function() {
	
	});

</script>

    <h3>Administration </h3>

    
    <form id="form" name="form1" method="post" action="">
      <table>
      <tr class="grey">
        <td width="170">User Name</td>
        <td width="94">Last Login</td>
        <td width="199">Role</td>
        <td width="61">Jobs</td>
        <td width="60">Errors</td>
      </tr>
      <c:forEach items="${users}" var="user">
      	<tr>
      		<td>${user.email}</td><td>1</td>
      		<td>
      			<select>
				    <c:forEach var="role" items="${roles}">
				     <option>${role.role}</option>
				    </c:forEach>
				</select>
      		
      		</td>
      		
      		<td>4</td><td>5</td>
      	</tr>
      </c:forEach>
    </table>
    <div align="right"><input type="reset" name="Edit User" id="button" class="margin" value="Edit User"/></div>
 </form>

	</template:content>
</template:insert>
