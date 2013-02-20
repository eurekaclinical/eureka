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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>


<template:insert template="/templates/eureka_main.jsp">

    <template:content name="sidebar">
        <img
            src="${pageContext.request.contextPath}/images/bioinformatics.jpg" />
    </template:content>

    <template:content name="content">

        <h3>Upload Data</h3>


        <form id="uploadForm" name="uploadForm" class="pad_top pad"
            method="post" action="${pageContext.request.contextPath}/protected/upload"
            ENCTYPE="multipart/form-data">

            <table>
                <tr>
                    <td width="252">Job Status</td>
                    <td width="204">Status Date</td>
                    <td width="151">Warnings & Errors</td>
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
                <br /> <br /> <input type="file" name="uploadFileName" class="button" id="browseButton"
                    value="Browse" /> <input type="submit" id="button" class="button" value="Upload"
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
    </template:content>
</template:insert>

