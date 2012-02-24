<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">
	    <script type="text/javascript"> 
		
	    (function poll(){
		        setTimeout(function(){
		            $.ajax({ url: "${pageContext.request.contextPath}/jobpoll", success: function(data){
		            //Update your dashboard gauge
		            if (data['currentStep'] != undefined) {
                    	
		            	$('#status').text(data['currentStep'] + " out of " + data['totalSteps']);
		            	$('#uploadTime').text(data['uploadTime']);

                    } else {
                            
                    }

		            //Setup the next poll recursively
		            poll();
		        }, dataType: "json"});
		       }, 5000);
		    })();
		
		</script> 
  
	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>

	<template:content name="content">

			<h3>Upload Data</h3>

			<form id="uploadForm" name="uploadForm" class="pad_top" method="post" action="${pageContext.request.contextPath}/upload" ENCTYPE="multipart/form-data">
			
                <table>
					<tr class="grey">
						<td width="231">Document Name</td>
						<td width="89">Date</td>
						<td width="152">Status</td>
						<td width="104">Upload Time</td>
						<td width="51">Errors</td>
					</tr>
					<tr>
			      		<td>&nbsp;
				  			
				  		</td>
			      		<td>&nbsp;
				  			
				  		</td>
			      		<td>
				  			<div id="status"></div>
				  		</td>
			      		<td>&nbsp;
				  			
				  		</td>
			      		<td>&nbsp;
				  			
				  		</td>
				  	</tr>
					
					<!--  
			       <c:forEach items="${jobs}" var="jobs">
			      	<tr>
			      		<td>
				  			${job.configurationId}
				  		</td>
			      		<td>
				  			&nbsp;
				  		</td>
			      		<td>
				  			&nbsp;
				  		</td>
			      		<td>
				  			&nbsp;
				  		</td>
			      		<td>
				  			&nbsp;
				  		</td>
				  	</tr>
				  	</c:forEach>
				  	-->
				</table>
      
				<div class="pad_rt pad_top" align="right">
          			<br/>
                    <br/>
					<input type="file" name="uploadFileName" id="button" value="Browse" />
					<input type="submit" id="button" value="Upload" disabled>
                    <br/>
                    <br/>
                    <a href="${pageContext.request.contextPath}/docs/sample.xlsx">Download Sample Spreadsheet</a>
				</div>
			</form>

       
  </template:content>
	<template:content name="subcontent">

		<div>
			<h3>Please wait while your project is being uploaded.....</h3>
			<img src="${pageContext.request.contextPath}/images/e-ani.gif"
				hspace="450" align="middle" />
		</div>
	</template:content>
</template:insert>