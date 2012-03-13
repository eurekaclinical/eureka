<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tlds/function.tld" prefix="myfn" %>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="informatics, i2b2, biomedical, clinical research, research, de-identification, clinical data analysis, analytics, medical research, data analysis tool, clinical database, eureka!, eureka, scientific research, temporal patterns, bioinformatics, ontology, ontologies, ontology editor, data mining, etl, cvrg, CardioVascular Research Grid" />

<title>Eureka! Clinical Analytics</title>

<!--[if lte IE 7]>
<link href="${pageContext.request.contextPath}/css/ie7.css" rel="stylesheet" type="text/css">
<style>
.container { width:1024px; margin: 0 auto;}
ul.nav a { zoom: 1; }
#submit { border:none;}
</style>
<![endif]-->



<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.validate.js"></script>
<script src="${pageContext.request.contextPath}/js/eureka.js"></script>


<link rel="SHORTCUT ICON" href="favicon.ico">
<meta name="Description" content="A Clinical Analysis Tool for Biomedical Informatics and Data" />


</head>

<body>

<div class="container">
<div>
<!--<div id="login">
	   <label>User Name
	   <input id="login_field" type="text" name="textfield" />
	   </label>
	   	   <label>Password
	   <input id="login_field" type="text" name="textfield" />
	   </label>
 	       <input id="submit" type="submit" name="Submit" value="Log In" />
 <br />
 	<span class="sub_text"><a href="forgot_password.html">Login Help</a></span>
<br />
    </div>-->
<div class="header">
    <span><a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/tag_line.gif" alt="Data Analysis Tool" width="238" align="absmiddle" /></a></span>
</div>

 <div>    
  <ul class="nav">
      <li><a href="${pageContext.request.contextPath}/about.jsp"><img src="${pageContext.request.contextPath}/images/about_icon.gif" alt="About" width="30" height="30" align="absmiddle" />About</a></li>
      <c:choose>
        <c:when test="${pageContext.request.remoteUser == null}">
          <img src="${pageContext.request.contextPath}/images/reg_icon.gif" alt="Register" width="30" height="30" align="absmiddle" />
          <li><a href="${pageContext.request.contextPath}/register.jsp">Register</a></li>
        </c:when>
      </c:choose>
      <c:choose>
        <c:when test="${pageContext.request.remoteUser != null}">
          <img src="${pageContext.request.contextPath}/images/acct_icon.gif" alt="Account" width="30" height="30" align="absmiddle" />
          <li><a href="${pageContext.request.contextPath}/protected/user_acct?action=list">Account</a></li>
        </c:when>
      </c:choose>
     
      <img src="${pageContext.request.contextPath}/images/contact_icon.gif" alt="Contact" width="30" height="30" align="absmiddle" />
      <li><a href="${pageContext.request.contextPath}/contact.jsp">Contact</a></li>
      <img src="${pageContext.request.contextPath}/images/help_icon.gif" alt="Help" width="30" height="30" align="absmiddle" />
      <li><a href="${pageContext.request.contextPath}/help.jsp">Help</a></li>
      
 	<c:if test="${pageContext.request.remoteUser != null}">
 	
 		<c:if test="${myfn:contains(pageContext.request.roles, 'ROLE_ADMIN')}">
                  <img src="${pageContext.request.contextPath}/images/admin_icon.gif" alt="Administration" width="30" height="30" align="absmiddle" />
                  <li><a href="${pageContext.request.contextPath}/protected/admin?action=list">Administration</a></li>    
        </c:if>
 	  </c:if>
	  <c:choose>
	  	
	  	<c:when test="${pageContext.request.remoteUser != null}">
<div style="float:right;" class="fltrt">
	  	  <li>Welcome ${pageContext.request.remoteUser} | <a href="${pageContext.request.contextPath}/logout">Logout</a></li>
      	  <img src="${pageContext.request.contextPath}/images/i2b2_icon.gif" alt="i2b2" width="30" height="30" align="absmiddle" />
	      <li><a href="https://eureka.cci.emory.edu/i2b2/" target="_blank">i2b2</a></li>
	      <img src="${pageContext.request.contextPath}/images/rsch_icon.gif" alt="Upload Data" width="30" height="30" align="absmiddle" />
	      <li><a href="${pageContext.request.contextPath}/protected/jobs">Upload Data</a></li>
</div>
	  	</c:when>
	  	
	  	<c:otherwise>
<div class="fltrt">
	      <img src="${pageContext.request.contextPath}/images/login_icon.gif" alt="Login" width="30" height="30" align="absmiddle" />
	      <li><a href="${pageContext.request.contextPath}/protected/login">Login</a></li>   </div>
	  	
	  	</c:otherwise>
	  </c:choose>
    </ul>
  </div>

  <div class="sidebar1">
	<template:get name="sidebar" />
  </div>

  <div class="content">
		<template:get name="content" />
  </div>
  
   <div class="sub-content">
		<template:get name="subcontent" />
        <!--[if lte IE 9]>

<div class="release_notes sub_width" style="position: absolute; top: 580px; left: 10px;">
			<p>01.05.2012 : eureka.cci.emory.edu established</p>
			<p>01.09.2012 : Front-end/Back-end Combined</p>
			<p>01.20.2012 : Login Procedure in Place</p>
			<p>01.27.2012 : Admin/Edit Users Implemented</p>
            <p>01.29.2012 : Backend Installations on Server</p>
			<p>02.03.2012 : Upload Data Functionality</p>
		</div>
		<div class="release_notes sub_width" style="position: absolute; top: 580px; left: 355px;">
			<p>12.05.2011 : Login Process Underway</p>
			<p>12.02.2011 : Core UI Design Prototype Created</p>
			<p>11.29.2011 : Technology Recommendations</p>
			<p>11.28.2011 : High level Requirements Published</p>
			<p>11.18.2011 : Business Requirements Published</p>
			<p>11.15.2011 : Data Analysis Tool Named Eureka!</p>
		</div>
		<div class="release_notes sub_width" style="position: absolute; top: 580px; left: 715px;">
			<p>11.14.2011 : Data Analysis Tool Lands a New Name</p>
			<p>11.01.2011 : Data Analysis Tool Demo VM Launched</p>
			<p>11.01.2011 : Data Architecture Defined</p>
			<p>11.01.2011 : CVRG Data Dictionary Defined</p>
			<p>11.01.2011 : User Stories</p>
			<p>10.20.2011 : Demo VM Available for Testing</p>
		</div>
<![endif]-->
  </div>
  
  <div class="footer">
    <p>Copyright &copy; 2012 Emory University -- All  Rights Reserved</p>
  </div>
  <!-- end .container --></div>
</body>
</html>
