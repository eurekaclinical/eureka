<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2015 Emory University
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>

<template:insert template="/templates/eureka_main.jsp">
	<template:content name="content">
		<h3>Browse Population</h3>
		<div id="msg" class="alert alert-danger" role="alert">
			<p><strong>No data!</strong> This job did not output any data.</p>
		</div>
		<div id="loading"><p>Please wait while treemap is loading...</p></div>
		<div id="infovis" style="width: 900px; height: 500px;"></div>
		<!--[if IE]><script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/assets/infoviz-2.0.1/Extras/excanvas.js"></script><![endif]-->
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/infoviz-2.0.1/jit-yc.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/eureka.stats${initParam['eureka-build-timestamp']}.js"></script>
		<script type="text/javascript">init(${param['jobId']});</script>
	</template:content>
</template:insert>