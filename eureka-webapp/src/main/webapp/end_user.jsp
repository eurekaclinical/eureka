<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/informatics.jpg" />
	</template:content>
	
	<template:content name="content">

    <h3 id="registerHeading">End User Agreement</h3>

  <p>This is a demonstration website. It is not suitable for use with   sensitive data including patient data that contains identifiers. Do not   under any circumstances load sensitive data to this site. </p>
  <p>You agree to   absolve us of all responsibility legal or otherwise if federal, state or   local laws, your institution's policies, or applicable data use   agreements are violated by your loading data onto this site.</p>
  <p>This is beta-quality software. There exists the possibility that data   that you upload to this site could accidentally be exposed, deleted or   corrupted. You agree to absolve us of any responsibility legal or   otherwise for damage caused by the exposure, deletion or corruption of   data that you upload to this site. </p>
  <p>Please do provide feedback to the   developers using the email address on the site's Contact page if you   discover these or any other bugs in the software.</p>
  <p class="fltlft">
    <input type="button" onClick="parent.location='register.jsp'" value='Back' id="submit">

    </p>

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