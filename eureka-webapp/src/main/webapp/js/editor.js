$("#sortable").sortable();
$("#sortable").disableSelection();

$("#sortable2").sortable();
$("#sortable2").disableSelection();
var numItems = 0;

Raphael.fn.connection = function (obj1, obj2, line, bg) {
    if (obj1.line && obj1.from && obj1.to) {
        line = obj1;
        obj1 = line.from;
        obj2 = line.to;
    }
    var bb1 = obj1.getBBox(),
        bb2 = obj2.getBBox(),
        p = [{x: bb1.x + bb1.width / 2, y: bb1.y - 1},
        {x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1},
        {x: bb1.x - 1, y: bb1.y + bb1.height / 2},
        {x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2},
        {x: bb2.x + bb2.width / 2, y: bb2.y - 1},
        {x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1},
        {x: bb2.x - 1, y: bb2.y + bb2.height / 2},
        {x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2}],
        d = {}, dis = [];
    for (var i = 0; i < 4; i++) {
        for (var j = 4; j < 8; j++) {
            var dx = Math.abs(p[i].x - p[j].x),
                dy = Math.abs(p[i].y - p[j].y);
            if ((i == j - 4) || (((i != 3 && j != 6) || p[i].x < p[j].x) && ((i != 2 && j != 7) || p[i].x > p[j].x) && ((i != 0 && j != 5) || p[i].y > p[j].y) && ((i != 1 && j != 4) || p[i].y < p[j].y))) {
                dis.push(dx + dy);
                d[dis[dis.length - 1]] = [i, j];
            }
        }
    }
    if (dis.length == 0) {
        var res = [0, 4];
    } else {
        res = d[Math.min.apply(Math, dis)];
    }
    var x1 = p[res[0]].x,
        y1 = p[res[0]].y,
        x4 = p[res[1]].x,
        y4 = p[res[1]].y;
    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
        y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
        x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
        y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3)].join(",");
    if (line && line.line) {
        line.bg && line.bg.attr({path: path});
        line.line.attr({path: path});
    } else {
        var color = typeof line == "string" ? line : "#000";
        return {
            bg: bg && bg.split && this.path(path).attr({stroke: bg.split("|")[0], fill: "none", "stroke-width": bg.split("|")[1] || 3}),
            line: this.path(path).attr({stroke: color, fill: "none", "arrow-end": "classic-wide-long", "stroke-width": 3}),
            from: obj1,
            to: obj2
        };
    }
};

var el;

$(document).ready(function(){
	
	//$('#wizard').smartWizard({transitionEffect:'slide', 'labelFinish': 'Save'});
	//$('#wizard').smartWizard({'labelFinish': 'Save', onLeaveStep:leaveAStepCallback,onFinish:onFinishCallback,enableFinishButton:true});
	$('#wizard').smartWizard({'labelFinish': 'Save', onLeaveStep:leaveAStepCallback});
    
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
     if(validateAllSteps()){
      $('form').submit();
     }
    }
    

    function validateSteps(step){
    	var isStepValid = true;
    	// validate step 1
    	if(step == 1){
		  var element_name = $('#element_name').val();
		  
		  if(!element_name && element_name.length <= 0){
			  isStepValid = false; 
			  $('#wizard').smartWizard('showMessage','Please enter an element name and click next.');
			  $('#wizard').smartWizard('setError',{stepnum:step,iserror:true});         
		  }else{
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

        }
    	
    	return isStepValid;
    }
  
	
	

	
	var tabContainers = $('div.tabs > div');
	tabContainers.hide().filter(':first').show();
	var isoutside = true;
	var groups = [];     //just an array of the sets we create.
    
	$('div.tabs ul.tabNavigation a').click(function () {
		tabContainers.hide();
		tabContainers.filter(this.hash).show();
		$('div.tabs ul.tabNavigation a').removeClass('selected');
		$(this).addClass('selected');
		return false;
	}).filter(':first').click();
	
	var r, tempS, tempT, shapes = [], texts = [];
	
    var color, i, ii, line,
    dragger = function () {
        var g = null;
        if (!isNaN(this.idx)) {
            //find the set (if possible)
            var g = groups[this.idx];
        }
        if (g) {
            var i;
            //store the starting point for each item in the set
            for(i=0; i < g.items.length; i++) {
                //g.items[i].ox = g.items[i].attr("x");
                //g.items[i].oy = g.items[i].attr("y");
                var obj = g.items[i];   //shorthand
                //obj.attr({ x: obj.ox + dx, y: obj.oy + dy });

                //optional:  We can do a check here to see what property
                //           we should be changing.
                // i.e. (haven't fully tested this yet):
                switch (obj.type) {
                     case "rect":
                    	 obj.ox = obj.attr("x");
                    	 obj.oy = obj.attr("y");
                    	 break;
                     case "text":
                    	 obj.ox = obj.attr("x");
                    	 obj.oy = obj.attr("y");
                         break;
                     case "circle":
                    	 obj.ox = obj.attr("cx");
                    	 obj.oy = obj.attr("cy");
                    	 break;
                }
            	
            }
        }


    },
    move = function (dx, dy) {
            // Move main element
    	
        if (!isNaN(this.idx)) {
            var g = groups[this.idx];
        }

        if (g) {
            var x;
            //reposition the objects relative to their start position
            for(x = 0; x < g.items.length; x++) {
                var obj = g.items[x];   //shorthand
                obj.attr({ x: obj.ox + dx, y: obj.oy + dy });

                //optional:  We can do a check here to see what property
                //           we should be changing.
                // i.e. (haven't fully tested this yet):
                switch (obj.type) {
                	case "rect":
                	case "text":
                		obj.attr({ x: obj.ox + dx, y: obj.oy + dy });
                		break;
                	case "circle":
                		obj.attr({ cx: obj.ox + dx, cy: obj.oy + dy });
                }

            }
        }

    },
    up = function () {
        var g = null;
        if (!isNaN(this.idx)) {
            //find the set (if possible)
            var g = groups[this.idx];
        }
        if (g) {
            var i;
            //store the starting point for each item in the set
            for(i=0; i < g.items.length; i++) {
                //g.items[i].ox = g.items[i].attr("x");
                //g.items[i].oy = g.items[i].attr("y");
                var obj = g.items[i];   //shorthand
                //obj.attr({ x: obj.ox + dx, y: obj.oy + dy });

                //optional:  We can do a check here to see what property
                //           we should be changing.
                // i.e. (haven't fully tested this yet):
                switch (obj.type) {
                     case "rect":
                    	 obj.ox = obj.attr("x");
                    	 obj.oy = obj.attr("y");
                    	 break;
                     case "text":
                    	 obj.ox = obj.attr("x");
                    	 obj.oy = obj.attr("y");
                         break;
                     case "circle":
                    	 obj.ox = obj.attr("cx");
                    	 obj.oy = obj.attr("cy");
                    	 break;
                }
            }
        }        
    },

    r = Raphael("holder", 950, 480),
    connections = [],
    addText = function (theText, x, y) {
    	var width = 100;
    	var height = 90;
    	var roundness = 10;
    	var radius = 5;
    	var halfsquaresize = radius/2;
    	
        var rect = r.rect(x, y, width, height, roundness);
    	var bbox = rect.getBBox();
        
    	var botrect 	= newdragnode(r, 
    								bbox.x+bbox.width/2 - halfsquaresize, 
        							bbox.y+bbox.height + radius, 
        							radius);
        
    	var toprect 	= newdragnode(r, 
    								bbox.x+bbox.width/2 - halfsquaresize,
        							bbox.y-radius, 
        							radius);
    	
        var rightrect 	= newdragnode(r, 
        							bbox.x+bbox.width + radius,
        							bbox.y+bbox.height/2 - halfsquaresize, 
        							radius);
        
        var leftrect 	= newdragnode(r, 
        							bbox.x-radius,
        							bbox.y+bbox.height/2 - halfsquaresize, 
        							radius);
       
        
        var text =  r.text(x+40, y+20, theText);
        color = Raphael.getColor();
        rect = rect.attr({fill: color, stroke: color, "fill-opacity": 0, "stroke-width": 2, cursor: "move"});
        text = text.attr({fill: color, stroke: "none", "font-size": 12, cursor: "move"});


        //Create a set so we can move the text and rectangle at the same time
        var g = r.set(rect, leftrect, rightrect, toprect, botrect, text);
        rect.idx = groups.length;   //index in our groups array,
                                    //so we can easily find the set later
        groups.push(g);

        //set up drag/drop
        // - This could be applied to the set as well, but the "dragged"
        //   object ends up being the rect anyways.
        rect.drag(move, dragger, up);
        

    };
   
    function newdragnode(raphael, x, y, radius) {
    	
    	var node = raphael.circle(x, y, radius);
        node.attr("fill", "rgb(25,25,112)");
    	//var node = raphael.rect(x, y, radius, radius);

    	return node;
    }
   
    var obj1, obj2;
    $("#holder").mousedown(

    		function(e) {
    			var isoutside = false;
    			
    			for (var i=0; i <  $('svg circle').length; i++) {
    				var bb = $('svg circle')[i].getBBox();

    	                var width = 100;
    	                var height = 90;
                        var xPos = e.clientX - e.currentTarget.clientWidth + width - 5;
                        var yPos = e.clientY - e.currentTarget.clientHeight + height + 5;
                        //r.circle(xPos, yPos, 1);
    				//if ((( e.clientX >= bb.x && e.clientX < bb.x + bb.width) && (e.clientY >= bb.y && e.clientY < bb.y + bb.height))) {
    				if ((( xPos >= bb.x && xPos < bb.x + bb.width) && (yPos >= bb.y && yPos < bb.y + bb.height))) {
    					isoutside = true;
    					break;
    				}
    				
    			}
    		
    			if (isoutside) {
    				//x = e.clientX;
    				//y = e.clientY;
    	                var width = 100;
    	                var height = 90;
                        var x = e.clientX - e.currentTarget.clientWidth + width - 5;
                        var y = e.clientY - e.currentTarget.clientHeight + height + 5;
    				line = Line(x, y, x, y, r);
                    obj1 = r.ellipse(x,y,1,1);
    				$("#holder").bind('mousemove', function(e) {
    	                var width = 100;
    	                var height = 90;
                        var x = e.clientX - e.currentTarget.clientWidth + width - 5;
                        var y = e.clientY - e.currentTarget.clientHeight + height + 5;
    					//x = e.clientX;
    					//y = e.clientY;
    					line.updateEnd(x, y);
    				});    				
    			}

    		}
    		
    );

    $("#holder").mouseup(

    		
    		function(e) {
    			var isoutside = false;

    			for (var i=0; i <  $('svg circle').length; i++) {
    				var bb = $('svg circle')[i].getBBox();
    	                var width = 100;
    	                var height = 90;
                        var x = e.clientX - e.currentTarget.clientWidth + width - 5;
                        var y = e.clientY - e.currentTarget.clientHeight + height + 5;
	    			if ((( x >= bb.x && x < bb.x + bb.width) && (y >= bb.y && y < bb.y + bb.height))) {
	    				isoutside = true;	    				
                        obj2 = r.ellipse(x,y,1,1);
		    			break;
	    			}
    			}
    			
    			if (!isoutside) {
    				//alert("inside");
    				$('svg path:last').remove();
    			} else {
    				$('svg path:last').remove();
                    r.connection(obj1,obj2,"#8b8989");
    				$('svg ellipse').remove();

                }
    			$("#holder").unbind('mousemove');
    		}
    );
   
    $("#tab1").treeview({
        url: "http://localhost:8080/eureka-webapp/json"
    });

    /*
	$("#tab1").jstree({
        
        "json_data" : {
            "ajax" : {
                "url" : "http://localhost:8080/eureka-webapp/json",
                "data" : function (n) {
                    return { id : n.attr ? n.attr("id") : 0 };
                }
            }
        },

		"dnd" : {
			"drop_finish" : function(data) {
//				addText(data.o[0].textContent.split("\n")[0], data.e.layerX-data.e.currentTarget.clientWidth, data.e.layerY-data.e.currentTarget.clientHeight);
				if (data.e.currentTarget.id == 'tree-drop') {
				    var type = $('#type').val();
                    if (type == 'Workflow') {
    	                var width = 100;
    	                var height = 90;
                        var xPos = data.e.clientX - data.e.currentTarget.clientWidth + width;
                        var yPos = data.e.clientY - data.e.currentTarget.clientHeight + height;

                        addText(data.o[0].textContent.split("\n")[0], xPos, yPos);

                    } else {
					    $('<li/>', {
						    id: numItems,
						    text: data.o[0].textContent.split("\n")[0]		    
					    }).appendTo('#sortable');
				    } 

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
		"plugins" : [ "themes", "html_data", "dnd", "sortable","json_data" ]
	});
    */
	
	$("#tab2").jstree({
		"dnd" : {
			"drop_finish" : function(data) {
				//addText(data.o[0].textContent.split("\n")[0], data.e.clientX, data.e.clientY);
				if (data.e.currentTarget.id == 'tree-drop') {
				    var type = $('#type').val();
                    if (type == 'Workflow') {

                    } else {
					    $('<li/>', {
						    id: numItems,
						    text: data.o[0].textContent.split("\n")[0]		    
					    }).appendTo('#sortable');
				    } 
					
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
		"plugins" : [ "themes", "html_data", "dnd", "sortable" ]
	});


//connections.push(r.connection(shapes[0], shapes[1], "#fff"));
//connections.push(r.connection(shapes[1], shapes[2], "#fff", "#fff|5"));
//connections.push(r.connection(shapes[1], shapes[3], "#000", "#fff"));
	

	function Line(startX, startY, endX, endY, raphael) {
	    var start = {
	        x: startX,
	        y: startY
	    };
	    var end = {
	        x: endX,
	        y: endY
	    };
	    var getPath = function() {
	        return ("M" + start.x + " " + start.y + " L" + end.x + " " + end.y);
            //p.attr("stroke-width", "2");
            //return p;
	    };
	    var redraw = function() {
	        node.attr("path", getPath());
	        node.attr("id", "line");
            node.attr("stroke-width", "2");
            node.attr("arrow-end", "classic-wide-long");
            node.attr("stroke", "#8b8989");
	    }
	
	    var node = raphael.path(getPath());
        node.attr("stroke-width", "2");
        node.attr("arrow-end", "classic-wide-long");
        node.attr("stroke", "#8b8989");
	    return {
	        updateStart: function(x, y) {
	            start.x = x;
	            start.y = y;
	            redraw();
	            return this;
	        },
	        updateEnd: function(x, y) {
	            end.x = x;
	            end.y = y;
	            redraw();
	            
	            return this;
	        }
	    };
	};

});
   function initTrees() {
        $("#tab1").treeview({
            url: "http://localhost:8080/eureka-webapp/json"
        })
        /*
        $("#mixed").treeview({
            url: "source.php",
            // add some additional, dynamic data and request with POST
            ajax: {
                data: {
                    "additional": function() {
                        return "yeah: " + new Date;
                    }
                },
                type: "post"
            }
        });
        */
    }

