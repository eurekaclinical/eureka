<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 Emory University
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="sidebar">
		    <img src="${pageContext.request.contextPath}/images/clinical_analytics.jpg" />
	</template:content>
	
	<template:content name="content">
    <div class="help">
        <h3>Help</h3>
		<p>The links on this page will open up a new window in the <a href="${initParam['aiw-site-url']}/help.html" target="_blank">Eureka! website's help pages</a>.
        <table id="help_getting_started">
            <tr>
                <td>
                    <a href="${initParam['aiw-site-url']}/getting-started.html" class="imageAndCaption" target="_blank">
                        <img alt="Getting Started"
                             src="images/100px-Help-getting-started.png"/>
                        <div>
                            Getting Started
                        </div>
                    </a>
                </td>
                <td>
                    <a href="${initParam['aiw-site-url']}/user-faq.html" class="imageAndCaption" target="_blank">
                        <img alt="Frequently Asked Questions"
                             src="images/100px-Help-FAQ.png"/>
                        <div>
                            Frequently Asked Questions
                        </div>
                    </a>
                </td>
            </tr>
		</table>
		<table id="help_topics">
			<caption>Topics:</caption>
			<tr>
				<td>
					<a href="${initParam['aiw-site-url']}/spreadsheets.html" target="_blank">Spreadsheet Data Upload</a>
				</td>
				<td>
					<a href="${initParam['aiw-site-url']}/phenotypes.html" target="_blank">Working with Phenotypes</a>
				</td>
			</tr>
        </table>
		<p>NOTE: For privacy, data retention and other policies regarding this 
			site, click on Frequently Asked Questions above.</p>
        <p class="pad_top">&nbsp;</p>
        </div>
	</template:content>
	<template:content name="subcontent">
		<%@ include file="common/rss.jspf" %>
	</template:content>
</template:insert>