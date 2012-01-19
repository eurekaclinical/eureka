<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/medical_research.jpg" />
	</template:content>

	<template:content name="content">

		<div class="content">
			<h3>About</h3>
			<p>Eureka! is a product of the Center for Comprehensive
				Informatics (CCI) at Emory University in collaboration with John
				Hopkins University. The tool is a collaborative team-science project
				between software system researchers and scientific research groups.
				The purpose of this project is to employ complementary types of
				information sources and large volumes of data to perform detailed
				studies of mechanisms underlying physical and biological functions
				and processes that will enable clinical researchers to use existing
				clinical data for discovery research.</p>
			<p>
				Eureka! is the Data Analysis Tool for biomedical informatics and
				data. This application provides a method to extract, transform and
				load a clinical dataset and conforms it to a defined structure that
				loads into an instance of i2b2. <br />
			</p>
			<p>This tool enhances  medical research via technology by making
				it possible to collect, weed through, and analyze widespread data on
				patient treatments and outcomes. It involves the collection,
				management, analysis, and integration of data in biomedicine and can
				be used for research and healthcare delivery.</p>
			<p>
				The application has an Administration tab where virtual machines can
				be assigned and configured to a clinical database. Currently, the
				method for uploading data is to use an excel spreadsheet.<br /> <br />
				Begin your research by clicking on the <em>Upload Data</em> link.
			</p>
		</div>
	</template:content>
	<template:content name="subcontent">
		<div class="sub-content">
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
		</div>
	</template:content>

</template:insert>