<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/analytics.jpg" />
	</template:content>


	<template:content name="content">
		<h3>Overview</h3>
		<p class="pad_top">
			Welcome to Eureka! The Clinical Analysis Tool for biomedical
			informatics and data. This application provides a method to extract,
			transform and load a clinical dataset and conforms it to a defined
			structure that loads into an instance of i2b2 that will enable
			clinical researchers to use existing clinical data for discovery
			research.<br />
		</p>
		<p>This tool enhances  medical research via technology by making
			it possible to collect, weed through, and analyze widespread data on
			patient treatments and outcomes. It involves the collection,
			management, analysis, and integration of data in biomedicine and can
			be used for research and healthcare delivery.</p>
		<p>
			The application has an Administration tab where virtual machines can
			be assigned and configured to a clinical database. <br />This
			mechanism is available to administrators only.
		</p>
		<p>
			Currently, the method for uploading data is to use an excel
			spreadsheet.<br /> <br /> Begin your research by clicking on the <em>Upload
				Data</em> link.
		</p>

	</template:content>
	<template:content name="subcontent">

		<h3>Release Notes</h3>
		<div class="release_notes sub_width">
			<p>12.05.2011 : Login Process Underway</p>
			<p>12.02.2011 : Core UI Design Prototype Created</p>
			<p>11.29.2011 : Technology Recommendations</p>
			<p>11.28.2011 : High level Requirements Published</p>
			<p>11.18.2011 : Business Requirements Published</p>
			<p>11.15.2011 : Data Analysis Tool Named Eureka!</p>
		</div>
		<div class="release_notes sub_width">
			<p>11.14.2011 : CVRG Data Analysis Tool Lands a New Name</p>
			<p>11.01.2011 : CVRG Data Analysis Tool Demo VM Launched</p>
			<p>11.01.2011 : Data Architecture Defined</p>
			<p>11.01.2011 : CVRG Data Dctionary Defined</p>
			<p>11.01.2011 : User Stories</p>
			<p>10.20.2011 : Demo VM Available for testing</p>
		</div>
	</template:content>

</template:insert>
