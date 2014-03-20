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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<c:choose>
			<c:when test="${requestScope.error}">
				${error}
			</c:when>
			<c:otherwise>
				<h3>
					Your registration is now verified. Your account will be activated within the next 3 business days.
					You will be notified by e-mail when activation has occurred. If you do not receive any e-mail,
					please contact us at <a href="mailto:aiwhelp@emory.edu">aiwhelp@emory.edu</a>.
				</h3>
			</c:otherwise>
		</c:choose>
	</template:content>
</template:insert>


