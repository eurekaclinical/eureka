<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">
	<script type="text/javascript">
		(function poll() {
			setTimeout(function() {
				$.ajax({
					url : "${pageContext.request.contextPath}/jobpoll",
					success : function(data) {

						if (data['currentStep'] != undefined) {
							if (data['currentStep'] < data['totalSteps']) {
								$('#status').text(
										data['currentStep'] + " out of "
												+ data['totalSteps']);
							} else {
								$('#status').text('Complete');

							}

							var d = new Date(data['uploadTime']);
							var dateStr = d.getMonth() + "/" + d.getDate()
									+ "/" + d.getFullYear() + " "
									+ d.toLocaleTimeString();

							$('#statusDate').text(dateStr);
							if (data['messages'][0] == null) {
								$('#messages').text('No errors reported');
							} else {
								$('#messages').text(data['messages'][0]);
							}
						} else {
							$('#status').text('No jobs have been submitted.');
							$('#statusDate').empty();
							$('#messages').empty();
						}

						//Setup the next poll recursively
						poll();
					},
					dataType : "json"
				});
			}, 5000);
		})();
	</script>

	<template:content name="sidebar">
		<img
			src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

		<h3>Upload Data</h3>


		<form id="uploadForm" name="uploadForm" class="pad_top fltlft"
			method="post" action="${pageContext.request.contextPath}/upload"
			ENCTYPE="multipart/form-data">

			<table>
				<tr>
					<td width="152">Job Status</td>
					<td width="104">Status Date</td>
					<td width="51">Errors</td>
				</tr>
				<tr>
					<td>
						<div id="status"></div>
					</td>
					<td>
						<div id="statusDate"></div>
					</td>
					<td>
						<div id="messages"></div>
					</td>
				</tr>
			</table>



			<div class="pad_rt pad_top" align="right">
				<br /> <br /> <input type="file" name="uploadFileName" id="button"
					value="Browse" /> <input type="submit" id="button" value="Upload"
					disabled> <br /> <br /> <a
					href="${pageContext.request.contextPath}/docs/sample.xlsx">Download
					Sample Spreadsheet</a>
			</div>
		</form>


	</template:content>
	<template:content name="subcontent">

		<div id="jobUpload">
			<h3>Please wait while your project is being uploaded.....</h3>
			<img src="${pageContext.request.contextPath}/images/e-ani.gif"
				hspace="450" align="middle" />
		</div>
		<div id="uploadOutput"></div>
	</template:content>
</template:insert>