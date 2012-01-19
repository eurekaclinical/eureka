<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">

		<div class="content">
			<h3>Account</h3>
			<div class="pad pad_top">
				<table>
					<tr class="grey">
						<td width="124">Name:</td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td>Organization:</td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td>Email:</td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td width="143">&nbsp;</td>
						<td width="189">&nbsp;</td>
						<td width="66">&nbsp;</td>
						<td width="67">&nbsp;</td>
					</tr>
					<tr>
						<td>User Name:</td>
						<td colspan="4">&nbsp;</td>
					</tr>
					<tr>
						<td>Password:</td>
						<td colspan="4">&nbsp;</td>
					</tr>

				</table>
			</div>
			<div class=" fltrt margin">
				<a href="edit_acct.html"><img src="images/edit_btn.gif"
					width="33" height="19" alt="Edit" /></a>
			</div>

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