<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>

<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/infra3.jpg" />
	</template:content>


	<template:content name="content">
		<h3>Overview</h3>
      <p style="font-size:11px;">Welcome to the Eureka! Clinical Analytics demonstration website. This   site provides you with an easy way to load clinical datasets into your   own instance of <a href="http://www.i2b2.org" target="_blank" rel="nofollow">i2b2</a>, a clinical database query tool, on the cloud. This site is a project of the <a href="http://cci.emory.edu" target="_blank" rel="nofollow">Emory Center for Comprehensive Informatics (CCI)</a> and is sponsored by the <a href="http://www.cvrgrid.org" target="_blank" rel="nofollow">CardioVascular Research Grid (CVRG)</a>, an <a href="http://www.nhlbi.nih.gov/" target="_blank" rel="nofollow">NHLBI</a> grant for creating informatics infrastructure in support of cardiovascular research.</p>
      <p style="font-size:11px;">To get started, click on the Register link. You will soon receive an   account and can begin uploading data. Click on the Help link to learn   more.</p>
        <p><strong>NOTE: This is a demonstration website and is NOT suitable for use   with sensitive data including patient data that contains identifiers. We   will soon make available a version of this web application for cloud   deployment that is suitable for use with identified patient data. Stay   tuned!</strong></p>
      <p style="font-size:11px;">The website's source code is available as open source under the Apache 2   license. Please contact the developers through the email address on the   site's Contact page for a copy.</p>
        <p class="small_text">&nbsp;</p>
        <p class="small_text">The software powering this site has been supported in part by <a href="http://www.emoryhealthcare.org" target="_blank" rel="nofollow">Emory Healthcare</a>; <a href="http://winshipcancer.emory.edu" target="_blank" rel="nofollow">Emory Winship Cancer Institute</a>;   NHLBI grant R24 HL085343; PHS Grant UL1 RR025008, KL2 RR025009 and TL1   RR025010 from the CTSA program, NIH, NCRR; NIH/ARRA grant   325011.300001.80022; and M01 RR-00039 from the GCRC program, NIH, NCRR.</p>
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
