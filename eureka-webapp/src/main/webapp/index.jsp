<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>

<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/infra3.jpg" />
	</template:content>


	<template:content name="content">
		<h3>Overview</h3>
      <p style="font-size:11px;">Welcome to the Eureka! Clinical Analytics demonstration website. This   site provides you with an easy way to load clinical datasets into your   own instance of <a href="http://www.i2b2.org" rel="nofollow">i2b2<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a>, a clinical database query tool, on the cloud. This site is a project of the <a href="http://cci.emory.edu" rel="nofollow">Emory Center for Comprehensive Informatics (CCI)<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a> and is sponsored by the <a href="http://www.cvrgrid.org" rel="nofollow">CardioVascular Research Grid (CVRG)<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a>, an <a href="http://www.nhlbi.nih.gov/" rel="nofollow">NHLBI<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a> grant for creating informatics infrastructure in support of cardiovascular research.</p>
      <p style="font-size:11px;">To get started, click on the Register link. You will soon receive an   account and can begin uploading data. Click on the Help link to learn   more.</p>
        <p><strong>NOTE: This is a demonstration website and is NOT suitable for use   with sensitive data including patient data that contains identifiers. We   will soon make available a version of this web application for cloud   deployment that is suitable for use with identified patient data. Stay   tuned!</strong></p>
      <p style="font-size:11px;">The website's source code is available as open source under the Apache 2   license. Please contact the developers through the email address on the   site's Contact page for a copy.</p>
        <p class="small_text">&nbsp;</p>
        <p class="small_text">The software powering this site has been supported in part by <a href="http://www.emoryhealthcare.org" rel="nofollow">Emory Healthcare<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a>; <a href="http://winshipcancer.emory.edu" rel="nofollow">Emory Winship Cancer Institute<img src="https://web.cci.emory.edu/confluence/images/icons/linkext7.gif" alt="" height="7" border="0" width="7" align="absmiddle" /></a>;   NHLBI grant R24 HL085343; PHS Grant UL1 RR025008, KL2 RR025009 and TL1   RR025010 from the CTSA program, NIH, NCRR; NIH/ARRA grant   325011.300001.80022; and M01 RR-00039 from the GCRC program, NIH, NCRR.</p>
	</template:content>
	<template:content name="subcontent">
	  </template:content>
	<template:content name="subcontent">
	  <h3>Release Notes</h3>
		<div class="release_notes sub_width" style="position: absolute; top: 560px; left: 10px;">
			<p>01.05.2012 : eureka.cci.emory.edu established</p>
			<p>01.09.2012 : Front-end/Back-end combined</p>
			<p>01.20.2012 : Login Procedure in place</p>
			<p>01.27.2012 : Admin/Edit Users Implemented</p>
            <p>01.29.2012 : Backend Installations on server</p>
			<p>02.03.2012 : Upload Data Functionality</p>
		</div>
		<div class="release_notes sub_width" style="position: absolute; top: 560px; left: 355px;">
			<p>12.05.2011 : Login Process Underway</p>
			<p>12.02.2011 : Core UI Design Prototype Created</p>
			<p>11.29.2011 : Technology Recommendations</p>
			<p>11.28.2011 : High level Requirements Published</p>
			<p>11.18.2011 : Business Requirements Published</p>
			<p>11.15.2011 : Data Analysis Tool Named Eureka!</p>
		</div>
		<div class="release_notes sub_width" style="position: absolute; top: 560px; left: 715px;">
			<p>11.14.2011 : Data Analysis Tool Lands a New Name</p>
			<p>11.01.2011 : Data Analysis Tool Demo VM Launched</p>
			<p>11.01.2011 : Data Architecture Defined</p>
			<p>11.01.2011 : CVRG Data Dctionary Defined</p>
			<p>11.01.2011 : User Stories</p>
			<p>10.20.2011 : Demo VM Available for testing</p>
		</div>
	</template:content>

</template:insert>