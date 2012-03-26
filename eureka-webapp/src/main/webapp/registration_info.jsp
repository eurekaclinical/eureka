<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		`<img src="${pageContext.request.contextPath}/images/medical_research.jpg" />
	</template:content>

	<template:content name="content">
		<c:choose>
			<c:when test="${requestScope.error}">
				${error}			
			</c:when>
			<c:otherwise>
				<h3>Your registration is now verified. Your account will be activated within the next 3 business days. You will be notified by e-mail when activation has occurred. If you do not receive any e-mail, please contact us at <a href="mailto:aiwhelp@emory.edu">aiwhelp@emory.edu</a>.</h3>
			
			</c:otherwise>
		</c:choose>
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


