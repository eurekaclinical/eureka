<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">
<div>
	<template:content name="sidebar">
		    <img src="${pageContext.request.contextPath}/images/clinical_analytics.jpg" />
	</template:content>
	
	<template:content name="content">
		<h3>Help</h3>
		<p class="pad_top">
			<strong>Limitations</strong><br /> The application currently has
			little in the way of security built-in. It only supports one user at
			a time. In order to make the VM runnable on a wide range of
			computers, we have limited the default amount of RAM and disk space
			that is allocated to the VM. Instability can occur if your computer
			is hibernated while the VM is running, thus it is recommended that
			you shut down the VM (using VMWare Player, Workstation or Fusion's
			menu commands) beforehand.
		</p>
		<p>
			<strong>Troubleshooting</strong><br /> The Administration tab exists
			to provide some measure of control to restart components of the
			workflow that are not functioning normally. Behind the scenes, the
			workflow uses JBoss to host i2b2 and a web services interface to the
			workflow, and it uses Oracle-XE 11g to host your uploaded data once
			it is loaded into i2b2. The Administration tab contains two boxes for
			restarting the Webservice (includes i2b2) and Oracle instances. For
			each box, click the restart button in the lower right corner.
			Depending on the capabilities of your computer, restarting either
			service may take some time.
		</p>
		<p>
			<strong>Feedback and Bug Reports</strong><br /> As this is a
			technology preview, it is possible that something will go wrong.
			Please contact the developer team with feedback and issues.
			<a href="mailto:admin@emory.edu">admin@emory.edu</a>
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
</div>