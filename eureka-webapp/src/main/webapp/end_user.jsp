<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2015 Emory University
  %%
  This program is dual licensed under the Apache 2 and GPLv3 licenses.
  
  Apache License, Version 2.0:
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
  GNU General Public License version 3:
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_sidebar.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/informatics.jpg" />
	</template:content>
	
	<template:content name="content">

    <h3 id="registerHeading">End User Agreement</h3>
<div class="help">
  <h5>This is a demonstration website. It is not suitable for use with sensitive data including patient data that contains identifiers. Do not under any circumstances load sensitive data to this site.</h5>
  <p>This is beta-quality software. There exists the risk that data that   you upload to this site could accidentally be exposed, deleted or   corrupted. You agree to accept the risk that this web site and the   software on it may delete, corrupt or publicly disclose any data you   upload to this site and agree to hold Emory University and the   individual contributors to this web site harmless from any negative   consequence of you uploading data to this site.</p>
  <p>By uploading data to this web site, you warrant that you have the   legal right to do so. You also agree that if you upload data to this web   set that you are not legally entitled to upload, you will hold Emory   University and all other parties connected with this web site harmless   from the consequences of your action.</p>
  <p>This web site, the software on the web site and any other  intellectual property on the web site are provided &quot;as is&quot; without  warranty of any kind.  Emory University and the individual contributors   to this web site, its software and content disclaim all warranties,   express and implied, including and without limitation, any implied   warranties of merchantability, fitness for a particular purpose or   noninfringement.</p>
  <p>In no event shall Emory University or the individual contributors to   this web site, its software or other intellectual property be liable for   any indirect, incidental, punitive or consequential damages, or damages   for loss of profits, revenue, data or data use, incurred by you or any   third party, whether in an action in contract or tort, even if Emory   University or any of the individual contributors to this web site, its   software or other intellectual property have been advised of the   possibility of such damages.</p>
  <p>In no event shall the entire liability of Emory University or any of   the individual contributors to this web site exceed one thousand dollars   ($1,000).</p>
  <p>This agreement is governed by the substantive and procedural laws of the State of Georgia. You, Emory University and the individual contributors   to this web site agree to submit to the exclusive jurisdiction of, and   venue in, the courts of Dekalb or Fulton counties in the State of  Georgia in any dispute arising out of or relating to this agreement. </p>
  <p>If any provision of this Agreement is held to be unenforceable, this Agreement will remain in effect with the provision omitted.</p>
  <p>Please do provide feedback to the developers using the email address on the site's Contact page if you discover these or any other bugs in the software.</p>
  </div>
  <p class="fltlft">
    <input type="button" onClick="parent.location='register.jsp'" value='Back' id="submit">
    
  </p>

	</template:content>
</template:insert>
