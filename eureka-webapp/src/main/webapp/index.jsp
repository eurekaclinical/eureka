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
		<img src="${pageContext.request.contextPath}/images/infra3.jpg" />
	</template:content>


	<template:content name="content">
		<h3>What is Eureka! Clinical Analytics?</h3>
		<p>Eureka! Clinical Analytics is a tool for creating research 
            databases containing electronic health record and other data in 
            support of cohort discovery and analysis.</p>
        <p>Eureka! populates an <a href="http://www.i2b2.org">i2b2</a> database 
            with your data, either imported from a spreadsheet or located in
            a data warehouse at your healthcare institution. It also populates 
            the database with cohort descriptions of your patients, called 
            clinical phenotypes, that you specify as temporal patterns in 
			diagnoses, procedures, lab test results and other clinical and 
			administrative data. While researchers today typically perform 
			chart abstraction to assign patients to cohorts, Eureka! does it 
			automatically. I2b2 lets you extract data and computed phenotypes 
			about selected patients into standard statistical analysis tools.
        </p>

		<c:if test="${applicationScope.webappProperties.demoMode}">
			<p>This website is a fully functional online demo. Take a look at our 
				<a href="http://aiw.sourceforge.net" target="_blank">Sourceforge website</a> for how to deploy your own copy 
				of Eureka!
			</p>

			<p><strong>NOTE: This demonstration website is NOT suitable for use 
					with sensitive data including patient data that contains 
					identifiers.</strong>
			</p>
		</c:if>
		<c:if test="${not applicationScope.webappProperties.demoMode and applicationScope.webappProperties.ephiProhibited}">
			<p><strong>NOTE: Loading real patient data into the system is strictly prohibited.</strong>
			</p>
		</c:if>
		
        <p class="small_text">&nbsp;</p>
        <p class="small_text">The software powering this site has been 
			supported in part by <a href="http://www.emoryhealthcare.org" target="_blank" rel="nofollow">Emory Healthcare</a>; 
			<a href="http://winshipcancer.emory.edu" target="_blank" rel="nofollow">Emory Winship Cancer Institute</a>; 
			NHLBI grant R24 HL085343; PHS Grant UL1 RR025008, KL2 RR025009 and TL1 RR025010 from the CTSA program, NIH, NCRR; and NCMHD grant RC4MD005964.
		</p>
	</template:content>
	<template:content name="subcontent">
	</template:content>
	<template:content name="subcontent">
		<h3>Release Notes</h3>
		<div class="release_notes sub_width">
			<p>07.01.2013: Version 1.7 is out! It provides enhancements to the data element editor and integration with jasig CAS (Central Authentication Service).</p>
			<p>02.15.2013: Version 1.6.1 is out! It fixes bugs in registration verification and editing data elements with property constraints.</p>
			<p>01.28.2013: Version 1.6 is out! The user-defined data element editor now supports four kinds of derived data elements for specifying various temporal patterns.</p>
			<p>10.11.2012: Version 1.5 is out! We now have preliminary user-defined derived data elements.  Click the &quot;Editor&quot; link after logging in.</p>
			<p>04.05.2012: First Eureka! Public Release (Version 1.4)</p>
		</div>
	</template:content>

</template:insert>
