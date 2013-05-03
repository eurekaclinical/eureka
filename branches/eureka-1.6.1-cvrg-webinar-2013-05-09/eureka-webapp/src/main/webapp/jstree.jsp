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
<html>
    <head>
        <title>Use jsTree</title>
        <script src="js/jquery-1.7.1.min.js">
        </script>
        <script src="js/jquery.jstree.js">
        </script>
        <link href="${pageContext.request.contextPath}/themes/default/style.css"
            rel="stylesheet" type="text/css" />

        <script>
            $(document).ready(function(){
                $("#treeViewDiv").jstree({
                    "json_data" : {
                        "ajax" : { "url" : "http://localhost:8080/eureka-webapp/json?id=root" }
                    },
                    "plugins" : [ "themes", "json_data", "ui" ]
                }).bind("select_node.jstree", function(e, data)
                    {
	                    if(data.rslt.obj[0].id !== undefined)
	                    {
	                        var childData = loadChild(data.rslt.obj[0].id);
	                        addNode(data.rslt.obj[0].id, childData.data, childData["attr"].id, false);    
	                    }
	                    else
	                    {
	                        alert("No href defined for this element");
	                    }

                    });
                
                
	                function loadChild(id) {
                        var result = {};
                        $.ajax({
                               type: "POST",
                               url: "http://localhost:8080/eureka-webapp/json?id="+id,
                               contentType: "application/json; charset=utf-8",
                               //data: "{'id':" + id + "}",
                               dataType: "json",
                               success: function(data) {
                                   result = data[0];
                               }
                           });
                           console.log(result);
                           return result;

	                }
	                
	                function addNode(localParentId,nodeName,nodeId,hasChildren) {
	
	                    ////////////////////////////////////////////
	                    //
	                    // Create a new node on the tree
	                    //
	                    ////////////////////////////////////////////
	                    if(hasChildren) {
	
		                    /// If it has children we load the node a little differently
		                    /// give it a different image, theme and make it clickable
		
		                    $("#treeViewDiv").jstree("create_node", $("#"+localParentId), "inside",  { "data" : nodeName ,"state" : "closed","attr" : { "id" : nodeId,"rel":"children"}},null, true);
	
	
		                } else {
		
		                    // Nodes with no trees cannot be opened and
		                    // have a different image
		
		                    $("#treeViewDiv").jstree("create_node", $("#"+localParentId), "inside",  { "data" : nodeName ,"state" : "leaf","attr" : { "id" : nodeId,"rel":"noChildren"}},null, true);
		
		                }
		
	                }

            });
        </script>
    </head>
    <body>
        <div id="treeViewDiv">
        </div>
    </body>
</html>
