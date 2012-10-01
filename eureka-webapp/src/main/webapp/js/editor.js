$("#sortable").sortable();
$("#sortable").disableSelection();

var numItems = 0;

var el;
var dropBoxMaxTextWidth = 275;

$(document).ready(function(){
	
	$('#wizard').smartWizard({'labelFinish': 'Save', onLeaveStep:leaveAStepCallback, onFinish:onFinishCallback});

    var $dialog = $('<div></div>')
        .html('<p>' +
'For Categorical:<br/>' +
'Drag and Drop an element from the System or User-Defined element explorer' +
'to the canvas to make it a member of the category.' +
               '</p>' +
                '<p>' +
'For Temporal:<br/>' +
'Drag and Drop an element from the System or User-Defined element explorer' +
'to the canvas to make it a part of the temporal pattern. Current behavior' +
'is to infer the pattern when all of the listed elements are present in any' +
'temporal order.' +
                '</p>' 
)
        .dialog({
            autoOpen: false,
            title: 'Help Instructions'
        });

    $('#help_select').click( function() {
        $dialog.dialog('open');
    });
    
    $('#sortable').show();
    $('#holder').hide();
    $('#expand_div').hide();
    $('#collapse_div').hide();

    initDropTargetList();

    $('#dialog').hide();

    function leaveAStepCallback(obj){
      var step_num= obj.attr('rel');
      return validateSteps(step_num);
    }

    var type = $("input:radio[name='type']:checked").val();
    
    function onFinishCallback(){
     //if(validateAllSteps()){
    	var propositions = [];
    	var type = $("input:radio[name='type']:checked").val();
    	var element_name = $('#element_name').val();
    	var element_desc = $('#element_description').val();
    	var propId = $('#propId').val();
       $('#sortable li').each( function () {
            console.log(this.id);
            var namespaceStr = '';//this.textContent.split("-")[0];
            if ($('#systemTree [id="'+this.id + '"]').length > 0) {
            	namespaceStr = 'system';
            	
            } else {
            	namespaceStr = 'user';
            }
            var obj = {id: this.id, type: namespaceStr};
            propositions.push(obj);
        });
       $.ajax({
           type: "POST",
           url: "/protected/saveprop?id="+propId +
                    "&name="+element_name+
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
                  window.location.href="/protected/editorhome";       
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
                var type = $("input:radio[name='type']:checked").val();
              if (type == undefined || type == "") {
			    isStepValid = false; 
			    $('#wizard').smartWizard('showMessage','Please select a type of element to create.');
			    $('#wizard').smartWizard('setError',{stepnum:step,iserror:true});         
              }
              else{
                $('#wizard').smartWizard('hideMessage','');
                $('#wizard').smartWizard('setError',{stepnum:step,iserror:false});
              }

    		
    	} else if (step == 3) {
              if ($('#sortable').children().length == 0) {
			    isStepValid = false; 
			    $('#wizard').smartWizard('showMessage','Please select an element from the ontology explorer.');
			    $('#wizard').smartWizard('setError',{stepnum:step,iserror:true});         
              }
              else{
                $('#wizard').smartWizard('hideMessage','');
                $('#wizard').smartWizard('setError',{stepnum:step,iserror:false});
              }

        } else if (step == 4) {


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
		    "ajax" : { 
                "url" : "/systemlist" ,
                "data": function(n) {
                    return {
                        id : n.attr ? n.attr("id") : "root" 
                    };
                }

             }
		},
		"dnd" : {
			"drop_finish" : function(data) {
                if (idIsNotInList(data.o[0].id)  &&
                        data.e.currentTarget.id == 'tree-drop' || data.e.currentTarget.id == 'label-info') {

                    $('#label-info').hide();
                    var target = $('#tree-drop')[0]; 
                    target.style["background"] = "lightblue";
                    $('#label-info')[0].hidden = false;
				    var type = $('#type').val();
                    var X = $("<span/>", { class: "delete" });
                    var txt = $("<span/>", { text : data.o[0].children[1].childNodes[1].textContent });
                    
                    $('<li/>', { id: data.o[0].id}).append(X, txt).appendTo('#sortable');
                
                    var txt = $('#sortable li:last').text();
    
                    var width = txt.length * 7;
                    if (width > dropBoxMaxTextWidth) {
                        dropBoxMaxTextWidth = width;
                        $('#sortable').width(dropBoxMaxTextWidth);
                    }

                    $('#sortable li:last').mouseover(function () {

                            $(this.children[0]).css({cursor:"pointer", backgroundColor:"#24497A"});
                    });     
                    $('#sortable li:last').mouseout(function () {

                            $(this.children[0]).css({cursor:"pointer", backgroundColor:"lightblue"});
                    });     

                    // function to enable removal of list item once it is clicked
                    $($('#sortable li:last')[0].children[0]).click(function onItemClick() {
                        $(this.parent).animate( { backgroundColor: "#CCC", color: "#333" }, 500);
                            $("#dialog").dialog({
                                buttons : {
                                    "Confirm" : function() {
                                        $($('#sortable li:last')[0].children[0].parentNode).remove();
                                        $(this).dialog("close");
                                        if ($('#sortable li').length == 0) {
                                            $('#sortable').width(275);
                                            dropBoxMaxTextWidth = 275;
                                        }
                                    },
                                    "Cancel" : function() {
                                        $(this).dialog("close");
                                    }
                                }
                            });
                        $("#dialog").html(this.parentNode.children[1].innerHTML);
                        $("#dialog").dialog("open");
                        
                        //alert("Removed Selection: "+this.parentNode.children[1].innerHTML);

                    });
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
    }).bind("open_node.jstree", function(e, data)
	{
	    if(data.rslt.obj[0].id !== undefined)
	    {
	    	//loadChild(data.rslt.obj[0].id);

	    	//if ($("[id='"+data.rslt.obj[0].id+ "']")[0].children.length < 3) {
	    	//	loadChild(data.rslt.obj[0].id);
	    	//}
	    }
	    else
	    {
	        alert("No href defined for this element");
	    }

	});


	function loadChild(id) {
	    $.ajax({
	           type: "POST",
	           url: "/syspropchildren?propId="+id,
	           contentType: "application/json; charset=utf-8",
	           //data: "{'id':" + id + "}",
	           dataType: "json",
	           success: function(data) {
	               var childData = data;
                   console.log(childData);
                   for (var i = 0; i < childData.length; i++) {
                	   addNode("systemTree", id, childData[i].data, childData[i]["attr"].id, false);    
                	   
                   }
	           }
	       });
	
	}

	
	function loadUserDefinedProps(id) {
	    $.ajax({
	           type: "POST",
	           url: "/userpropchildren?propId="+id,
	           contentType: "application/json; charset=utf-8",
	           //data: "{'id':" + id + "}",
	           dataType: "json",
	           success: function(data) {
	               var childData = data;
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
	
	        $("#" +rootNodeId).jstree("create_node", $("[id='"+localParentId+"']"), "inside",  { "data" : nodeName ,"state" : "closed","attr" : { "id" : nodeId,"rel":"children"}},null, true);
	
	
	    } else {
	
	        // Nodes with no trees cannot be opened and
	        // have a different image
	
	        $("#" + rootNodeId).jstree("create_node", $("[id='"+localParentId+ "']"), "inside",  { "data" : nodeName ,"state" : "leaf","attr" : { "id" : nodeId,"rel":"noChildren"}},null, true);
	
	    }

	}
	
	
	
	
	$("#userTree").jstree({
		"json_data" : {
		    "ajax" : { "url" : "/userproplist?id=root" }
		},
		"dnd" : {
			"drop_finish" : function(data) {
                if (idIsNotInList(data.o[0].id)  &&
                        data.e.currentTarget.id == 'tree-drop' || data.e.currentTarget.id == 'label-info') {
                      
                    $('#label-info').hide();
                    
                    var target = $('#tree-drop')[0]; 
                    target.style["background"] = "lightblue";

				    var type = $('#type').val();
				    
                    var X = $("<span/>", { class: "delete" });
                    var txt = $("<span/>", { text : data.o[0].children[1].childNodes[1].textContent });
                    
                    $('<li/>', { id: data.o[0].id}).append(X, txt).appendTo('#sortable');
                

                    var txt = $('#sortable li:last').text();
   
                    var width = txt.length * 7;
                    if (width > dropBoxMaxTextWidth) {
                        dropBoxMaxTextWidth = width;
                        $('#sortable').width(dropBoxMaxTextWidth);
                    }


                    $('#sortable li:last').mouseover(function () {

                            $(this.children[0]).css({cursor:"pointer", backgroundColor:"#24497A"});
                    });     
                    $('#sortable li:last').mouseout(function () {

                            $(this.children[0]).css({cursor:"pointer", backgroundColor:"lightblue"});
                    });     

                    // function to enable removal of list item once it is clicked
                    $($('#sortable li:last')[0].children[0]).click(function onItemClick() {
                        $(this.parent).animate( { backgroundColor: "#CCC", color: "#333" }, 500);
                            $("#dialog").dialog({
                                buttons : {
                                    "Confirm" : function() {
                                        $($('#sortable li:last')[0].children[0].parentNode).remove();
                                        $(this).dialog("close");
                                        if ($('#sortable li').length == 0) {
                                            $('#sortable').width(275);
                                            dropBoxMaxTextWidth = 275;
                                        }
                                    },
                                    "Cancel" : function() {
                                        $(this).dialog("close");
                                    }
                                }
                            });
                        $("#dialog").html(this.parentNode.children[1].innerHTML);
                        $("#dialog").dialog("open");
                        
                        //alert("Removed Selection: "+this.parentNode.children[1].innerHTML);

                    });
		
				numItems++;
            }
	
			
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
	    	if ($("[id='"+data.rslt.obj[0].id+ "']")[0].children.length < 3) {
	    		loadUserDefinedProps(data.rslt.obj[0].id);
	    	}
	    }
	    else
	    {
	        alert("No href defined for this element");
	    }


	});


	        
}

function showHelp() {

}
function idIsNotInList(id) {

    
    var retVal = true;
    $('#sortable li').each( function() {
        if (id == this.id) {
            retVal = false;
        }

    });
    return retVal;
    


}

function initDropTargetList() {

    $('#sortable li').each( function () {

          $(this).mouseover(function () {

                  $(this.children[0]).css({cursor:"pointer", backgroundColor:"#24497A"});
          });     
          $(this).mouseout(function () {

                  $(this.children[0]).css({cursor:"pointer", backgroundColor:"lightblue"});
          });     

          // function to enable removal of list item once it is clicked
          $($(this)[0].children[0]).click(function onItemClick() {
              $(this.parent).animate( { backgroundColor: "#CCC", color: "#333" }, 500);
                  $("#dialog").dialog({
                      buttons : {
                          "Confirm" : function() {
                              $($('#sortable li:last')[0].children[0].parentNode).remove();
                              $(this).dialog("close");
                              if ($('#sortable li').length == 0) {
                                  $('#sortable').width(275);
                                  dropBoxMaxTextWidth = 275;
                              }
                          },
                          "Cancel" : function() {
                              $(this).dialog("close");
                          }
                      }
                  });
              $("#dialog").html(this.parentNode.children[1].innerHTML);
              $("#dialog").dialog("open");
              
              //alert("Removed Selection: "+this.parentNode.children[1].innerHTML);

          });

    });
}
