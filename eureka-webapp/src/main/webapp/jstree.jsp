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
                        /*
                        "data":[
                            {
                                "data" : "Search engines",
                                "children" :[
                                             {"data":"Yahoo", "metadata":{"href":"http://www.yahoo.com"}},
                                             {"data":"Bing", "metadata":{"href":"http://www.bing.com"}},
                                             {"data":"Google", "children":[{"data":"Youtube", "metadata":{"href":"http://youtube.com"}},{"data":"Gmail", "metadata":{"href":"http://www.gmail.com"}},{"data":"Orkut","metadata":{"href":"http://www.orkut.com"}}], "metadata" : {"href":"http://youtube.com"}}
                                            ]
                            },
                            {
                                "data" : "Networking sites",
                                "children" :[
                                    {"data":"Facebook", "metadata":{"href":"http://www.fb.com"}},
                                    {"data":"Twitter", "metadata":{"href":"http://twitter.com"}}
                                ]
                            }
                        ]
                        */
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
