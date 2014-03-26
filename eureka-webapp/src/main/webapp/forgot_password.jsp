<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template" %>


<template:insert template="/templates/eureka_main.jsp">

	<template:content name="content">
		<h3>Having trouble logging in?</h3>

		<form id="ResetPsdForm" action="#" method="post" role="form" class="form-horizontal">
			<p>
				Remember, your user name is the email address that you used when you registered.
			</p>
			<p>
				If you cannot remember your password, please enter your email address and click the Reset Password
				button below. You will receive an email with a temporary password. You will be asked to change the
				password the next time you login.
			</p>
			<div class="form-group">
				<div class="col-sm-2 control-label">
					<label for="email">Email Address</label>
				</div>
				<div class="col-sm-5">
					<input id="email" name="email" type="text" class="form-control" value=""/>
				</div>
				<div class="col-sm-5">
					<button id="submit" type="submit" class="btn btn-primary">Reset Password</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<p id="passwordresetComplete" class="default-hidden text-danger">
				</div>
			</div>
		</form>
		<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/eureka.reset${initParam['eureka-build-timestamp']}.js"></script>
		<script type="text/javascript">
			eureka.reset.setup('#ResetPsdForm', '#email');
		</script>
	</template:content>
</template:insert>
