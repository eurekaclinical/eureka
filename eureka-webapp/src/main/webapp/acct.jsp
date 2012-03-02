<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		    <img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

			<h3>Account</h3>
            <form id="userAcctForm" action="#" method="post">
			<div class="pad pad_top">
				<table id="userAcctTable">
					<tr>
						<td width="124">Name:</td>
						<td width="465" colspan="4">${user.firstName} ${user.lastName}</td>
					</tr>
					<tr>
						<td>Organization:</td>
						<td colspan="4">${user.organization}</td>
					</tr>
					<tr>
						<td>Email:</td>
						<td colspan="4">${user.email}</td>
					</tr>
					<tr>
						<td>Password:</td>
						<td colspan="4"><input type="password" value="${user.password}" disabled="disabled"/></td>
					</tr>
					<tr>
					  <td>&nbsp;</td>
					  <td colspan="4">
                                          <!--
					<a href="edit_acct.html"><img src="${pageContext.request.contextPath}/images/edit_btn.gif"
                        width="33" height="19" alt="Change Password" id="editAcctBtn"/></a>
                    -->
					<img class="fltrt" src="${pageContext.request.contextPath}/images/chg_pswd_btn.gif"
                        alt="Change Password" id="editAcctBtn"/>
                      </td>
				  </tr>
				</table>

			
				
				<table id="newPasswordTable">
				
					
					<tr>
	  					<td class="status white"></td>					
					<tr>
						<td class=" white"><label id="lemail" for="email">New Password:</label></td>
	  					<td class="field white"><input type="password" name="newPassword" id="newPassword" /></td>
	  					<td class="status white"></td>					
					</tr>
					<tr>
						<td class=" white"><label id="lemail" for="email">Re-enter New Password</label></td>
	  					<td class="field white"><input type="password" name="verifyPassword" id="verifyPassword" /></td>
	  					<td class="status white"></td>					
					</tr>
					<tr>
					  <td class=" white">&nbsp;</td>
					  <td class="field white fltrt">
					    <input type="submit" value="Save" id="saveAcctBtn" class="button" />
					</td>
					  <td class="status white">&nbsp;</td>
				  </tr>
				</table>

                <input type="hidden" name="id" id="id" value="${user.id}" />
                <input type="hidden" name="action" value="save" />
            </form>
			<div id="passwordChangeComplete">
				<h2>Your password has been successfully changed.</h2>
			</div>
			<div id="passwordChangeFailure">
				<h2>There was an error changing your password.  The old password did not match with our system.</h2>
			</div>


		</div>
	</template:content>
	<template:content name="subcontent">
		<div>
			<div id="release_notes">
				<h3>
					<img src="${pageContext.request.contextPath}/images/rss.png"
						border="0" /> Related News <a href="xml/rss_news.xml" class="rss"></a>
				</h3>
<!-- start sw-rss-feed code --> 
<script type="text/javascript"> 
<!-- 
rssfeed_url = new Array(); 
rssfeed_url[0]="http://whsc.emory.edu/home/news/releases/index.rss&num=3";  
rssfeed_title="on"; 
rssfeed_item_title_length="50"; 
rssfeed_item_date="off"; 
rssfeed_item_description="on"; 
rssfeed_item_description_length="120"; 
//--> 
</script> 


				<noscript>
					<a style="padding-left: 35px"
						href="http://feed2js.org//feed2js.php?src=http%3A%2F%2Fwhsc.emory.edu%2Fhome%2Fnews%2Freleases%2Fresearch.rss&chan=y&num=4&desc=200>1&utf=y&html=y">View
						RSS feed</a>
				</noscript>

			</div>
		</div>
	</template:content>

</template:insert>
