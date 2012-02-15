<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_upload.jsp">
	    <script type="text/javascript"> 
		/*    
	    (function poll(){
		        setTimeout(function(){
		            $.ajax({ url: "jobpoll", success: function(data){
		            //Update your dashboard gauge
		            alert(data);
		            //Setup the next poll recursively
		            poll();
		        }, dataType: "text"});
		       }, 5000);
		    })();
		*/
		</script> 
<!--  
	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
	</template:content>
-->
	<template:content name="content">

			<h3>Upload Data</h3>

      <!--  
			<form id="form" name="form1" class="pad_top" method="post" action="${pageContext.request.contextPath}/upload" ENCTYPE="multipart/form-data">
			
                <table>
					<tr class="grey">
						<td width="231">Document Name</td>
						<td width="89">Date</td>
						<td width="152">Successfully Uploaded</td>
						<td width="104">Upload Time</td>
						<td width="51">Errors</td>
					</tr>
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
				</table>
      
				<div class="pad_top" align="right">
          			<br/>
                    <br/>
					<input type="file" name="uploadFileName" id="button" value="Browse" />
					<input type="submit" id="button" value="Upload">
                    <br/>
                    <br/>
                    <a href="docs/sample.xlsx">Download Sample Spreadsheet</a>
				</div>
			</form>
		-->
   <form id="fileupload" action="${pageContext.request.contextPath}/upload" method="POST" enctype="multipart/form-data">
        <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->        <div class="row fileupload-buttonbar">
            <div class="span7">
                <!-- The fileinput-button span is used to style the file input field as button -->
                <span class="btn btn-success fileinput-button">
                    <span><i class="icon-plus icon-white"></i> Add file...</span>
                    <input type="file" name="files[]" multiple>
                </span>
                <button type="submit" class="btn btn-primary start">
                    <i class="icon-upload icon-white"></i> Start upload
                </button>
                <button type="reset" class="btn btn-warning cancel">
                    <i class="icon-ban-circle icon-white"></i> Cancel upload
                </button>
                <button type="button" class="btn btn-danger delete">
                    <i class="icon-trash icon-white"></i> Delete
                </button>
                <input type="checkbox" class="toggle">
            </div>
            <div class="span5">
                <!-- The global progress bar -->
                <div class="progress progress-success progress-striped active fade">
                    <div class="bar" style="width:0%;"></div>
                </div>
            </div>
        </div>
        <!-- The loading indicator is shown during image processing -->
        <div class="fileupload-loading"></div>
        <br>
        <!-- The table listing the files available for upload/download -->
        <table class="table table-striped"><tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody></table>
    </form>

<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, files=o.files, l=files.length, file=files[0]; i<l; file=files[++i]) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name">{%=file.name%}</td>
        <td class="size">{%=o.formatFileSize(file.size)%}</td>
        {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=fileUploadErrors[file.error] || file.error%}</td>
        {% } else if (o.files.valid && !i) { %}
            <td>
                <div class="progress progress-success progress-striped active"><div class="bar" style="width:0%;"></div></div>
            </td>
            <td class="start">{% if (!o.options.autoUpload) { %}
                <button class="btn btn-primary">
                    <i class="icon-upload icon-white"></i> Start
                </button>
            {% } %}</td>
        {% } else { %}
            <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button class="btn btn-warning">
                <i class="icon-ban-circle icon-white"></i> Cancel
            </button>
        {% } %}</td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, files=o.files, l=files.length, file=files[0]; i<l; file=files[++i]) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
            <td></td>
            <td class="name">{%=file.name%}</td>
            <td class="size">{%=o.formatFileSize(file.size)%}</td>
            <td class="error" colspan="2"><span class="label label-important">Error</span> {%=fileUploadErrors[file.error] || file.error%}</td>
        {% } else { %}
            <td class="preview">{% if (file.thumbnail_url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}"></a>
            {% } %}</td>
            <td class="name">
                <a href="{%=file.url%}" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name%}</a>
            </td>
            <td class="size">{%=o.formatFileSize(file.size)%}</td>
            <td colspan="2"></td>
        {% } %}
        <td class="delete">
            <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <i class="icon-trash icon-white"></i> Delete
            </button>
            <input type="checkbox" name="delete" value="1">
        </td>
    </tr>
{% } %}
</script>
       
  </template:content>
	<template:content name="subcontent">

		<div>
			<h3>Please wait while your project is being uploaded.....</h3>
			<img src="${pageContext.request.contextPath}/images/e-ani.gif"
				hspace="450" align="middle" />
		</div>
	</template:content>
</template:insert>