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
		<img
			src="${pageContext.request.contextPath}/images/clinical_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>Having trouble logging in?</h3>
		<form id="ResetPsdForm" action="#" method="post">
			<div class="pad_btm fltlft">
				<p>
					Remember that your user name is your email address you used when you registered.<br />
					If you cannot remember your password, please enter your email address.   </p>         

				<div ><table class="white">
						<tr>
							<td class=" white"><label id="lemail" for="email">Email Address:</label></td>
							<td class="field white"><input id="email" name="email" type="text"  class="email" value="" /></td>
							<td class="status white shift_left error"></td>
						</tr>
					</table>
					&nbsp;&nbsp;&nbsp;
					<button id="submit" type="submit" class="btn btn-primary submit left_padding">Reset Password</button></div>

				<br />
				<br />
				<div id="passwordresetComplete" class = "pw_reset left_padding"></div>
			</div>


		</form>
	</template:content>
	<template:content name="subcontent">
		<%@ include file="common/rss.jspf" %>
	</template:content>
</template:insert>