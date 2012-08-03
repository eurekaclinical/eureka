<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_editor.jsp">

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">
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
                                                    <div id="tab2"></div>
										        </div>							        
								    </div>
										
            					</td>
            					
							</tr>            			
            			</table>
								   


	</template:content>
	<template:content name="subcontent">
		

		
	</template:content>

</template:insert>







			

	