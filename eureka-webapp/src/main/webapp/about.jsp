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
		`<img src="${pageContext.request.contextPath}/images/medical_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>About</h3>
		<div>
        <p>Eureka! Clinical Analytics is a project of the <a href="http://cci.emory.edu" target="_blank" rel="nofollow">Emory Center for Comprehensive Informatics</a>. It was conceived as part of the vision of the <a href="http://www.cvrgrid.org" target="_blank" rel="nofollow">CardioVascular Research Grid (CVRG)</a> to create tools that enable researchers to analyze and manipulate their biomedical research data in the cloud.</p>
        <h5>What does it do?</h5>
        <p>It can load your research project's clinical data into your own cloud-based instance of <a href="http://www.i2b2.org" target="_blank" rel="nofollow">i2b2</a>,   where you and your colleagues can ask questions about the data,   summarize data and download subsets of their data for your research. You   provide an Excel spreadsheet that meets certain criteria, and the   software takes care of the rest. While its functionality is targeted to   clinical datasets in cardiovascular disease research, it supports   loading a wide variety of clinical data types into i2b2.</p>
        <h5>Can I load data with identifiers in it?</h5>
        <p>No, this is a demonstration website only. Please see the End-user   agreement when you register for an account on the website for details.</p>
        <h5>What do you mean I can't load data with identifiers on it?</h5>
        <p>We are just getting started. We plan to leverage a cloud-based service like <a href="http://aws.amazon.com/ec2/" target="_blank" rel="nofollow">Amazon EC2</a> to enable you to create your own running instance of the website for   your lab or research project. There, within the terms of use of the   cloud service and your institution's policies, you will be able to load a   wider variety of research datasets onto your own instance of the   website. We also are creating powerful tools for you to connect the   software to existing research databases and data warehouses at your   institution. Of course, you also will be able to install the software   locally on your or your institution's own infrastructure.</p>
        <h5>What is going on behind the scenes?</h5>
      <p>Eureka! Clinical Analytics is a front-end user interface for the <a href="http://cci.emory.edu/cms/projects/aiw.html" target="_blank" rel="nofollow">Analytic Information Warehouse (AIW)</a> software system. The AIW is being developed at <a href="http://www.emory.edu" target="_blank" rel="nofollow">Emory University</a> to provide analytics infrastructure for clinical research and   healthcare quality assessment and performance improvement. We are using   it to develop predictive models of readmission within 30 days and find   &quot;hot spots&quot; in readmissions in Emory Healthcare's patient populations.   The AIW software is itself based on <a href="http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1975802/" target="_blank" rel="nofollow">Protempa</a>,   a software system for finding temporal patterns in clinical datasets.   Loading data into i2b2 is just one of the AIW's features. Future   releases of Eureka! will, over time,  add user interfaces for the   AIW's entire feature set.</p>
        <h5>May I see the source code?</h5>
        <p>Yes! The software is available as open source under the <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank" rel="nofollow">Apache 2 license</a>. Send an e-mail to the e-mail address on the Contact page to request a copy.</p>
        <h5>Where can I get more information?</h5>
        <p>Check out the CVRG wiki page for PROTEMPA/Analytic Information Warehouse/i2b2 at <a href="http://www.cvrgrid.org/cvrg-tool-demonstrations" target="_blank" rel="nofollow">http://www.cvrgrid.org/cvrg-tool-demonstrations</a>.</p>
        <h5>The Eureka! Clinical Analytics Team</h5>
        <p>Andrew Post, MD, PhD - Eureka! lead and CVRG co-investigator<br />
          Joel Saltz, MD, PhD - CVRG Emory site PI<br />
          Tahsin Kurc, PhD - CVRG co-investigator<br />
          Ashish Sharma, PhD - CVRG co-investigator<br />
          Richard Willard - Eureka! project manager<br />
          Himanshu Rathod - Lead software engineer<br />
          Sanjay Agravat, MS - Software engineer<br />
          Geoff Milton, MS - Software engineer<br />
          Suzanne Sturm - Web designer	</p>
          </div>
	</template:content>
	<template:content name="subcontent">
	  </template:content>
	<template:content name="subcontent">
	  <div id="release_notes">
			<h3>
				<img src="${pageContext.request.contextPath}/images/rss.png"
					border="0" /> Related News <a href="xml/rss_news.xml" class="rss"></a>
			</h3>
			<script language="JavaScript"
				src="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=1&utf=y"
				charset="UTF-8" type="text/javascript"></script>

			<noscript>
				<a style="padding-left: 35px"
					href="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=200>1&utf=y&html=y">View
					RSS feed</a>
			</noscript>



		</div>
	</template:content>

</template:insert>
