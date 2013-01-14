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
		<img src="${pageContext.request.contextPath}/images/infra3.jpg" />
	</template:content>


	<template:content name="content">
		<h3>What is Eureka! Clinical Analytics?</h3>
		<p>Eureka! Clinical Analytics is a web application that provides you 
			with an easy way to phenotype a patient population based on their 
			clinical and administrative data, and load those phenotypes and 
			data into your own instance of the <a href="http://www.i2b2.org" target="_blank" rel="nofollow">i2b2 data warehouse system</a>.
		</p>

		<p>This website is a fully functional online demo. Take a look at our 
			<a href="http://aiw.sourceforge.net" target="_blank">Sourceforge website</a> for how to deploy your own copy 
			of Eureka!
		</p>
		
		<p><strong>NOTE: This demonstration website is NOT suitable for use 
				with sensitive data including patient data that contains 
				identifiers.</strong>
		</p>
		
        <p class="small_text">&nbsp;</p>
        <p class="small_text">The software powering this site has been 
			supported in part by <a href="http://www.emoryhealthcare.org" target="_blank" rel="nofollow">Emory Healthcare</a>; 
			<a href="http://winshipcancer.emory.edu" target="_blank" rel="nofollow">Emory Winship Cancer Institute</a>; 
			NHLBI grant R24 HL085343; PHS Grant UL1 RR025008, KL2 RR025009 and 
			TL1 RR025010 from the CTSA program, NIH, NCRR; NIH/ARRA grant 
			325011.300001.80022; and M01 RR-00039 from the GCRC program, NIH, 
			NCRR.
		</p>
	</template:content>
	<template:content name="subcontent">
	</template:content>
	<template:content name="subcontent">
		<h3>Release Notes</h3>
		<div class="release_notes sub_width" style="position: absolute; top: 570px; left: 10px; background-color:#fff;">
			<p>10.11.2012 : Eureka! now has support for user-defined derived data elements.  Click the &quot;Editor&quot; link after logging in.</p>
			<p>04.05.2012 : Eureka! Release</p>
		</div>
		<%--
				<div class="release_notes sub_width" style="position: absolute; top: 570px; left: 280px; background-color:#fff;">
					<p></p>
				</div>
				<div class="release_notes sub_width" style="position: absolute; top: 570px; left: 600px; background-color:#fff;">
					<p></p>
				</div>
				<div class="release_notes sub_width" style="position: absolute; top: 570px; left:920px; background-color:#fff;">
					<p></p>
				</div>
		--%>
	</template:content>

</template:insert>
