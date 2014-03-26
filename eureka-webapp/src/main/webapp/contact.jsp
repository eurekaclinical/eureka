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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="webappProperties" class="edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties" scope="page"/>

<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
		<h3>Contact Information</h3>

		<div class="row">
			<div class="col-sm-4">
				<address>
					Department of Biomedical Informatics<br/>
					Emory University<br/>
					Psychology and Interdisciplinary Sciences Building, Suite
					566<br/>
					36 Eagle Row<br/>
					Atlanta, GA 30322<br/>
				</address>
				<address id="contactEmail"></address>
			</div>
			<div class="col-sm-4">
				<iframe width="500" height="400" frameborder="0"
						scrolling="no" marginheight="0" marginwidth="0"
						src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=Psychology+Building,+Emory+University,+Atlanta,+GA+30322&amp;aq=&amp;sll=33.772352,-84.395771&amp;sspn=0.010666,0.01369&amp;ie=UTF8&amp;hq=&amp;hnear=Psychology+Bldg,+Druid+Hills,+DeKalb,+Georgia+30307&amp;t=m&amp;ll=33.794948,-84.327407&amp;spn=0.010699,0.012875&amp;z=15&amp;iwloc=A&amp;output=embed"></iframe>
				<br/>
				<small><a
						href="https://maps.google.com/maps?f=q&amp;source=embed&amp;hl=en&amp;geocode=&amp;q=Psychology+Building,+Emory+University,+Atlanta,+GA+30322&amp;aq=&amp;sll=33.772352,-84.395771&amp;sspn=0.010666,0.01369&amp;ie=UTF8&amp;hq=&amp;hnear=Psychology+Bldg,+Druid+Hills,+DeKalb,+Georgia+30307&amp;t=m&amp;ll=33.794948,-84.327407&amp;spn=0.010699,0.012875&amp;z=15&amp;iwloc=A"
						style="color:#0000FF;text-align:left">View Larger
					Map</a></small>
			</div>
		</div>
		<script type="text/javascript"
				src="${pageContext.request.contextPath}/assets/js/eureka.util${initParam['eureka-build-timestamp']}.js"></script>
		<c:set var="contactEmail" value="${webappProperties.contactEmail}"/>
		<c:if test="${not empty contactEmail}">
			<c:set var="emailParts" value="${fn:split(contactEmail, '@')}"/>
			<c:if test="${fn:length(emailParts) == 2}">
				<script type="text/javascript">
					$(document).ready(function () {
						eureka.util.insertMailToTag("#contactEmail", "${emailParts[0]}", "${emailParts[1]}");
					});
				</script>
			</c:if>
		</c:if>
	</template:content>
</template:insert>
