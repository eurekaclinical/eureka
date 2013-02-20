<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
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
		<c:choose>
			<c:when test="${param.firstLogin}">
				<h3>Welcome! Please replace the default password with your own below:</h3>
			</c:when>
			<c:otherwise>
				<h3>Your password has expired. Enter a new one below:</h3>
			</c:otherwise>
		</c:choose>

		<form id="passwordExpirationfrm" action="#" method="post">
			<table id="newPasswordTableForExpiration">
				<tr>
					<td class="status white"></td>
				</tr>
				<tr>
					<td class=" white">
						<label>Old Password:</label>
					</td>
					<td class="field white">
						<input type="password" name="oldExpPassword" id="oldExpPassword" />
					</td>
					<td class="status white"></td>
				</tr>
				<tr>
					<td class=" white"><label>New Password:</label></td>
					<td class="field white">
						<input type="password" name="newExpPassword" id="newExpPassword" />
					</td>
					<td class="status white"></td>
				</tr>
				<tr>
					<td class=" white">
						<label>Re-enter New Password:</label>
					</td>
					<td class="field white">
						<input type="password" name="verifyExpPassword" id="verifyExpPassword" />
					</td>
					<td class="status white"></td>
				</tr>
				<tr>
					<td class=" white">&nbsp;</td>
					<td class="field white fltrt">
						<input type="submit" value="Save" id="saveAcctBtnExp" class="button" />
					</td>
					<td class="status white">&nbsp;</td>
				</tr>
			</table>
			<input type="hidden" name="targetURL" id="targetURL" value="${param.redirectURL}"/>

			<div id="passwordChangeComplete" class = "pw_reset left_padding">
			</div>
		</form>
	</template:content>
</template:insert>




