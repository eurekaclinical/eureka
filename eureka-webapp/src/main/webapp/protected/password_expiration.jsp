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

			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />

	</template:content>
	<template:content name="content">

		<h3>Your password has expired. Enter a new one below:</h3>
		<form id="passwordExpirationfrm" action="#" method="post">
		<table id="newPasswordTableForExpiration">


					<tr>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white"><label id="lemail" for="email">Old

								Password:</label></td>

						<td class="field white"><input type="password"

							name="oldExpPassword" id="oldExpPassword" /></td>

						<td class="status white"></td>

					</tr>



					<tr>

						<td class=" white"><label id="lemail" for="email">New

								Password:</label></td>

						<td class="field white"><input type="password"

							name="newExpPassword" id="newExpPassword" /></td>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white"><label id="lemail" for="email">Re-enter

								New Password</label></td>

						<td class="field white"><input type="password"

							name="verifyExpPassword" id="verifyExpPassword" /></td>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white">&nbsp;</td>

						<td class="field white fltrt"><input type="submit"

							value="Save" id="saveAcctBtnExp" class="button" /></td>

						<td class="status white">&nbsp;</td>

					</tr>

				</table>
				<input type="hidden" name="targetURL" id="targetURL" value="<%= request.getParameter("redirectURL") %> "/>

		<div id="passwordChangeComplete" class = "pw_reset left_padding">

		</div>
		</form>
	</template:content>
</template:insert>




