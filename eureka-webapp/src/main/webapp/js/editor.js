$("#sortable").sortable();
$("#sortable").disableSelection();

$("#sortable2").sortable();
$("#sortable2").disableSelection();
var numItems = 0;

var el;

$(document).ready(function(){
	
	//$('#wizard').smartWizard({transitionEffect:'slide', 'labelFinish': 'Save'});
	//$('#wizard').smartWizard({'labelFinish': 'Save', onLeaveStep:leaveAStepCallback,onFinish:onFinishCallback,enableFinishButton:true});
	$('#wizard').smartWizard({'labelFinish': 'Save', onLeaveStep:leaveAStepCallback, onFinish:onFinishCallback});
    
    $('#sortable').show();
    $('#holder').hide();
    $('#expand_div').hide();
    $('#collapse_div').hide();
    $('#dialog').hide();

    function leaveAStepCallback(obj){
      var step_num= obj.attr('rel');
      return validateSteps(step_num);
    }

    $('.stepContainer').height('462px');

    $('#expand').click( function() {
        $('#steps').hide();
        $('#expand_div').hide();
        $('#collapse_div').show();
        $('.stepContainer').width('1180px');
        $('.stepContainer').height('800px');
        $('.msg_content').width('1100px');
        $('.msg_content').height('800px');
        $('#holder').width('900px');
        $('#holder').height('700px');
        $('#content').height('900px');
    });
    $('#collapse').click( function() {
        $('#steps').show();
        $('#expand_div').show();
        $('#collapse_div').hide();
        $('.stepContainer').height('462px');
        $('.stepContainer').width('732px');
        $('.msg_content').width('720px');
        $('.msg_content').height('450px');
        $('#holder').width('550px');
        $('#holder').height('350px');
        //$('#content').height('900px');
    });

    var type = $('#type').val();
    if (type != undefined && type.length > 0) {
        gettypedescription();

    }
    $('#type').change(function() {
        gettypedescription();
     });
    
    function gettypedescription() {
        var type = $('#type').val();

        if (type == 'Category') {
			  $("#type_description").html("Concept is composed of 1 or more elements");
            
        } else if (type == 'Temporal'){
			  $("#type_description").html("Concept is composed of all elements");


        } else if (type == 'Workflow') { 
			  $("#type_description").html("New Workflow based on System and User-defined types.");

        } else {

			  $("#type_description").html("");

        }

    }
    function onFinishCallback(){
     //if(validateAllSteps()){
    	var propositions = [];
    	var type = $('#type').val();
    	var element_name = $('#element_name').val();
    	var element_desc = $('#element_description').val();
       $('#sortable li').each( function () {
            console.log(this.id);
            var namespaceStr = '';//this.textContent.split("-")[0];
            if ($('#systemTree #'+this.id).length > 0) {
            	namespaceStr = 'system';
            	
            } else {
            	namespaceStr = 'user';
            }
            var obj = {id: this.id, type: namespaceStr};
            propositions.push(obj);
        });
       $.ajax({
           type: "POST",
           url: "http://localhost:8080/eureka-webapp/saveprop?name="+element_name+
           			"&description="+element_desc +
           			"&type="+type+
           			"&proposition="+JSON.stringify(propositions),
           contentType: "application/json; charset=utf-8",
           data: JSON.stringify(propositions),
           dataType: "json",
           beforeSend: function(x) {
               if (x && x.overrideMimeType) {
                   x.overrideMimeType("application/j-son;charset=UTF-8");
               }                                     
            },
           success: function(data) {
                  window.location.href="http://localhost:8080/eureka-webapp/editor.jsp";       
           }
       });

      //$('form').submit();
     //}
    }
    

    function validateSteps(step){
    	var isStepValid = true;
    	// validate step 1
    	if(step == 1){
		  var element_name = $('#element_name').val();
		  var element_desc = $('#element_description').val();
		  
		  if((!element_name && element_name.length <= 0) ||
				  (!element_desc && element_desc.length <= 0)){
			  isStepValid = false; 
			  $('#wizard').smartWizard('showMessage','Please enter an element name and description and click next.');
			  $('#wizard').smartWizard('setError',{stepnum:step,iserror:true});         
		  }
		  else{
			  $('#wizard').smartWizard('hideMessage','');
			  $('#wizard').smartWizard('setError',{stepnum:step,iserror:false});
		  }
    	} else if (step == 2) {
			  var str = $('#type').val();
			  //$("h3:last").html(str);
              if (str == 'Workflow') {
                    $('#sortable').hide();
                    $('#holder').show();
                    $('#expand_div').show();
              } else {
                    $('#sortable').show();
                    $('#holder').hide();
                    $('#expand_div').hide();


              }
    		
    	} else if (step == 3) {

        } else if (step == 5) {


        }
    	
    	return isStepValid;
    }
  
	
	var tabContainers = $('div.tabs > div');
	tabContainers.hide().filter(':first').show();
    
	$('div.tabs ul.tabNavigation a').click(function () {
		tabContainers.hide();
		tabContainers.filter(this.hash).show();
		$('div.tabs ul.tabNavigation a').removeClass('selected');
		$(this).addClass('selected');
		return false;
	}).filter(':first').click();
		    
    initTrees();


});

function initTrees() {
	
   $("#systemTree").jstree({
		"json_data" : {
		    "ajax" : { "url" : "http://localhost:8080/eureka-webapp/systemlist?id=root" }
		},
		"dnd" : {
			"drop_finish" : function(data) {
				if (data.e.currentTarget.id == 'tree-drop') {
				    var type = $('#type').val();
				    $('<li/>', {
					    id: data.o[0].id,
					   // text: data.o[0].parentNode.parentNode.id + "-" + data.o[0].children[1].childNodes[1].textContent
						text:  data.o[0].children[1].childNodes[1].textContent
				    }).appendTo('#sortable');
		
				}
		
				numItems++;
	
			
			},
			"drag_check" : function(data) {
				if (data.r.attr("id") == "phtml_1") {
					return false;
				}
				return {
					after : false,
					before : false,
					inside : true
				};
			},
			"drag_finish" : function(data) {
				alert("DRAG OK");
			}
	},
	"plugins" : [ "themes", "json_data", "ui", "dnd" ]
	}).bind("select_node.jstree", function(e, data)
	{
	    if(data.rslt.obj[0].id !== undefined)
	    {
	        loadChild(data.rslt.obj[0].id);
	    }
	    else
	    {
	        alert("No href defined for this element");
	    }

	});


	function loadChild(parentNodeId, id) {
	    $.ajax({
	           type: "POST",
	           url: "http://localhost:8080/eureka-webapp/propdetail?propId="+id,
	           contentType: "application/json; charset=utf-8",
	           //data: "{'id':" + id + "}",
	           dataType: "json",
	           success: function(data) {
	               var childData = data;
                   console.log(childData)
	               //addNode(parentNodeId, id, childData.data, childData["attr"].id, false);    
	           }
	       });
	
	}

	function addNode(rootNodeId, localParentId,nodeName,nodeId,hasChildren) {

	    ////////////////////////////////////////////
	    //
	    // Create a new node on the tree
	    //
	    ////////////////////////////////////////////
	    if(hasChildren) {
	
	        /// If it has children we load the node a little differently
	        /// give it a different image, theme and make it clickable
	
	        $("#" +rootNodeId).jstree("create_node", $("#"+localParentId), "inside",  { "data" : nodeName ,"state" : "closed","attr" : { "id" : nodeId,"rel":"children"}},null, true);
	
	
	    } else {
	
	        // Nodes with no trees cannot be opened and
	        // have a different image
	
	        $("#" + rootNodeId).jstree("create_node", $("#"+localParentId), "inside",  { "data" : nodeName ,"state" : "leaf","attr" : { "id" : nodeId,"rel":"noChildren"}},null, true);
	
	    }

	}
	
	
	
	
	$("#tab2").jstree({
		"json_data" : {
		    "ajax" : { "url" : "http://localhost:8080/eureka-webapp/userproplist?id=root" }
		},
		"dnd" : {
			"drop_finish" : function(data) {
				if (data.e.currentTarget.id == 'tree-drop') {
				    var type = $('#type').val();
				    
				    $('<li/>', {
					    id: data.o[0].id,
					    text: data.o[0].parentNode.parentNode.id + "-" + data.o[0].children[1].childNodes[1].textContent
				    }).appendTo('#sortable');
		
				}
		
				numItems++;
	
			
			},
			"drag_check" : function(data) {
				if (data.r.attr("id") == "phtml_1") {
					return false;
				}
				return {
					after : false,
					before : false,
					inside : true
				};
			},
			"drag_finish" : function(data) {
				alert("DRAG OK");
			}
	},
	"plugins" : [ "themes", "json_data", "ui", "dnd" ]
	}).bind("select_node.jstree", function(e, data)
	{
	    if(data.rslt.obj[0].id !== undefined)
	    {
	        loadChild("tab2", data.rslt.obj[0].id);
	    }
	    else
	    {
	        alert("No href defined for this element");
	    }


	});


	        
}

