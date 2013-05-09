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
		<img src="${pageContext.request.contextPath}/images/medical_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>About Version ${initParam['eureka-version']}</h3>
		<p>Eureka! Clinical Analytics is a project of the
			<a href="http://cci.emory.edu" target="_blank">Center for Comprehensive Informatics</a>
			and <a href="http://bmi.emory.edu" target="_blank">Department of Biomedical Informatics</a>
			at <a href="http://www.emory.edu" target="_blank">Emory University</a>. 
			It was conceived as part of the vision of the 
			<a href="http://www.cvrgrid.org" target="_blank" rel="nofollow">CardioVascular Research Grid (CVRG)</a> 
			to create tools that enable researchers to analyze and manipulate 
			their biomedical research data in the cloud.
		</p>
		
		<h4>What does it do?</h4>
		<p>Eureka! Clinical Analytics provides you with an easy way to 
			phenotype a patient population based on their clinical and 
			administrative data, and load those phenotypes and data into your 
			own instance of <a href="http://www.i2b2.org" target="_blank" rel="nofollow">i2b2</a>.
			Once in i2b2, you and your colleagues can ask questions about the 
			data, summarize data and download subsets of your data for your 
			research. You provide your data in an Excel spreadsheet meeting 
			certain criteria, and the software takes care of the rest. While 
			its functionality is targeted to clinical datasets in 
			cardiovascular disease research, it supports loading a wide variety 
			of clinical data types into i2b2.
		</p>
		
		<c:if test="${applicationScope.webappProperties.demoMode}">
		<h4>Can I load data with identifiers in it?</h4>
		<p>No, this is a demonstration website only. Please see the End-user 
			agreement when you register for an account on the website for 
			details.
		</p>
		
		<h4>What do you mean I can't load data with identifiers on it?</h4>
		<p>If you need to load real patient data into Eureka!, you must 
			deploy your own instance of the software in a <a href="https://hipaa.emory.edu/home/" target="_blank">HIPAA</a>-compliant
			environment. See the <a href="${initParam['aiw-site-url']}/get-it.html" target="_blank">Get the Software page</a>
			on the Eureka! website for details. We provide a 
			<a href="http://aiw.sourceforge.net/download.html" target="_blank">distribution of Eureka! for local installation</a> 
			and an <a href="http://aiw.sourceforge.net/ec2.html" target="_blank">Amazon Machine Instance</a> for you to clone on the 
			<a href="http://aws.amazon.com/" target="_blank">Amazon Elastic Compute (EC2) Cloud</a>.
		</p>
		</c:if>
		
		<h4>What is going on behind the scenes?</h4>
		<p>Eureka! Clinical Analytics is a front-end user interface for the 
			<a href="${initParam['aiw-site-url']}/aiw.html" target="_blank">Analytic Information Warehouse (AIW)</a> 
			software system. The AIW is being developed at 
			<a href="http://www.emory.edu" target="_blank">Emory University</a> 
			to provide analytics infrastructure for clinical research and 
			healthcare quality assessment and performance improvement. We are 
			using it to develop predictive models of readmission within 30 days 
			and find &quot;hot spots&quot; in readmissions in Emory 
			Healthcare's patient populations. The AIW software is itself based 
			on <a href="http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1975802/" target="_blank" rel="nofollow">Protempa</a>, 
			a software system for finding temporal patterns in clinical 
			datasets. Loading data into i2b2 is just one of the AIW's features. 
			Future releases of Eureka! will, over time, add user interfaces for 
			the AIW's entire feature set.
		</p>
		
		<h4>May I see the source code?</h4>
		<p>Yes! The software is available as open source under the 
			<a href="http://www.apache.org/licenses/LICENSE-2.0.html" target="_blank" rel="nofollow">Apache 2 license</a>. 
			It is available from the 
			<a href="${initParam['eureka-dev-site-url']}" target="_blank">Eureka! Clinical Analytics Developer Resources</a> site.
		</p>
		
		<h4>Where can I get more information?</h4>
		<p>Check out the Eureka! website at 
			<a href="${initParam['aiw-site-url']}" target="_blank">${initParam['aiw-site-url']}</a>.
		</p>
	</template:content>
	<template:content name="subcontent">
	</template:content>
	<template:content name="subcontent">
		<%@ include file="common/rss.jspf" %>
	</template:content>

</template:insert>
