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
		<h3>Contact Information</h3>

        <div class="pad_btm fltlft">
			<address>
				Center for Comprehensive Informatics<br/>
				Department of Biomedical Informatics<br/>
				Emory University<br/>
				Psychology and Interdisciplinary Sciences Building, Suite 566<br/>
				36 Eagle Row<br/>
				Atlanta, GA 30322<br/>
			</address>
			<address>
				<script type="text/javascript">insertMailToTag('aiwhelp', 'emory.edu');</script>
			</address>
          </div>
          <div class="fltlft margin">
           <iframe width="300" height="300" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=Psychology+Building,+Emory+University,+Atlanta,+GA+30322&amp;aq=&amp;sll=33.772352,-84.395771&amp;sspn=0.010666,0.01369&amp;ie=UTF8&amp;hq=&amp;hnear=Psychology+Bldg,+Druid+Hills,+DeKalb,+Georgia+30307&amp;t=m&amp;ll=33.794948,-84.327407&amp;spn=0.010699,0.012875&amp;z=15&amp;iwloc=A&amp;output=embed"></iframe><br /><small><a href="https://maps.google.com/maps?f=q&amp;source=embed&amp;hl=en&amp;geocode=&amp;q=Psychology+Building,+Emory+University,+Atlanta,+GA+30322&amp;aq=&amp;sll=33.772352,-84.395771&amp;sspn=0.010666,0.01369&amp;ie=UTF8&amp;hq=&amp;hnear=Psychology+Bldg,+Druid+Hills,+DeKalb,+Georgia+30307&amp;t=m&amp;ll=33.794948,-84.327407&amp;spn=0.010699,0.012875&amp;z=15&amp;iwloc=A" style="color:#0000FF;text-align:left">View Larger Map</a></small>
		  </div>

	</template:content>
	<template:content name="subcontent">
		<%@ include file="common/rss.jspf" %>
	</template:content>
</template:insert>