<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>

<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/scientific_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>Login</h3>
		
		<form method="POST" action="j_security_check" style="width: 80%; text-align: right">
                        <div>
                            The e-mail address and password you entered does not match our records.
                        </div>
			<p>
				Enter your e-mail address and password.
			</p>
			<span style="text-align: right">
				E-mail Address:
				<input type="text" name="j_username" class="login_field required" tabindex="1" accesskey="e" size="25" autocomplete="false"/>
				<br>
				<br>
				Password:
				<input type="password" name="j_password" class="login_field required" tabindex="2" accesskey="p" size="25" autocomplete="false"/>
				<br>
				<br>
			</span>
			*Please note that loading real patient data into the system is strictly prohibited!
			<br>
			<br>
			<div class="row btn-row">
				<input name="submit" id="submit" class="submit" accesskey="l" value="Login" tabindex="3" type="submit" />
				<input name="reset" id="reset" class="btn-reset" accesskey="c" value="Clear" tabindex="4" type="reset" />
				<br>
				<br>
				<span class="sub_text">
					<a href="${pageContext.request.contextPath}/forgot_password.jsp">Login Help</a>
				</span>
			</div>
		</form>
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
