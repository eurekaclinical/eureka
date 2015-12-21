<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
	<div class="help">
		<h3>Help</h3>
		<p>The links on this page will open in a separate window in the <a href="${initParam['eureka-help-url']}/index.html" target="eureka-help">Eureka! website's help pages</a>.
		<div class="row">
			<div class="col-sm-3">
				<span class="thumbnail text-center">
					<a href="${initParam['eureka-help-url']}/getting-started.html" class="imageAndCaption" target="eureka-help">
						<img alt="Getting Started" src="${pageContext.request.contextPath}/assets/images/100px-Help-getting-started.png"/>
						<span class="caption">
							Getting Started
						</span>
					</a>
				</span>
			</div>
			<div class="col-sm-3">
				<span class="thumbnail text-center">
					<a href="${initParam['aiw-site-url']}/user-faq.html" class="imageAndCaption" target="eureka-help">
						<img alt="Frequently Asked Questions" src="${pageContext.request.contextPath}/assets/images/100px-Help-FAQ.png"/>
						<span class="caption">
							Frequently Asked Questions
						</span>
					</a>
				</span>
			</div>
		</div>
		<h4>Topics:</h4>
		<ul>
			<li>
				<a href="${initParam['eureka-help-url']}/spreadsheets.html" target="eureka-help">Spreadsheet Data Upload</a>
			</li>
			<li>
				<a href="${initParam['eureka-help-url']}/phenotypes.html" target="eureka-help">Working with Phenotypes</a>
			</li>
		</ul>
		<c:if test="${applicationScope.webappProperties.demoMode}">
		<p>
			NOTE: For privacy, data retention and other policies regarding this
			site, click on Frequently Asked Questions above.
		</p>
		</c:if>
	</div>
	</template:content>
</template:insert>
