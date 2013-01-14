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

		<h3>Account</h3>

	<div style="font-size: 10px;font-weight: 900;"><%=request.getAttribute("passwordExpiration")%></div>		

		<form id="userAcctForm" action="#" method="post">

		

			<div class="pad pad_top">

				<table id="userAcctTable">

					<tr>

						<td width="124">Name:</td>

						<td width="465" colspan="3">${user.firstName}

							${user.lastName}</td>

					</tr>

					<tr>

						<td>Organization:</td>

						<td colspan="3">${user.organization}</td>

					</tr>

					<tr>

						<td>Email:</td>

						<td colspan="3">${user.email}</td>

					</tr>

					<!--  

					<tr>

						<td>Password:</td>

						<td colspan="4"><input type="password"

							value="${user.password}" disabled="disabled" /></td>

					</tr>

					-->

					<tr>

						<td>&nbsp;</td>

						<td colspan="3">

							

				
				<!--
					<a href="edit_acct.html"><img src="${pageContext.request.contextPath}/images/edit_btn.gif"
                        width="33" height="19" alt="Change Password" id="editAcctBtn"/></a>
                    -->
                         

                 	   <a href=""><img

							src="${pageContext.request.contextPath}/images/chg_pswd_btn.gif"

							alt="Change Password" id="ChangePasswordbtn" /></a> 

					

						</td>

					</tr>

				</table>







				<table id="newPasswordTable">





					<tr>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white"><label id="lemail" for="email">Old

								Password:</label></td>

						<td class="field white"><input type="password"

							name="oldPassword" id="oldPassword" /></td>

						<td class="status white"></td>

					</tr>



					<tr>

						<td class=" white"><label id="lemail" for="email">New

								Password:</label></td>

						<td class="field white"><input type="password"

							name="newPassword" id="newPassword" /></td>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white"><label id="lemail" for="email">Re-enter

								New Password</label></td>

						<td class="field white"><input type="password"

							name="verifyPassword" id="verifyPassword" /></td>

						<td class="status white"></td>

					</tr>

					<tr>

						<td class=" white">&nbsp;</td>

						<td class="field white fltrt"><input type="submit"

							value="Save" id="saveAcctBtn" class="button" /></td>

						<td class="status white">&nbsp;</td>

					</tr>

				</table>



				<input

					type="hidden" name="action" value="save" />

		</form>

		<div id="passwordChangeComplete">

		</div>
		</div>

	</template:content>

	<template:content name="subcontent">
		<%@ include file="../common/rss.jspf" %>
	</template:content>



</template:insert>

