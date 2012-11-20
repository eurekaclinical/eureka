/* Eureka WebApp. Copyright (C) 2012 Emory University. Licensed under http://www.apache.org/licenses/LICENSE-2.0. */

$("ul.sortable").sortable();
$("ul.sortable").disableSelection();

var dropBoxMaxTextWidth = 275;
var possiblePropositions = new Object();
var saveFuncs = {
	'sequence': saveSequence,
	'categorical': saveCategorical
};

function postProposition (postUrl, postData, successFunc) {
	$.ajax({
		type: "POST",
		url: postUrl,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(postData),
		dataType: "json",
		success: successFunc
	});
}


function collectSequenceDataElement (elem) {
	// var type = $mainProposition.data('type');
	var $mainProposition = $(elem).find('ul[data-type="main"]').find('li').first();
	return {
		'dataElementKey': $mainProposition.data('key'),
		'withValue': $(elem).find('select[name="mainDataElementValue"]').val(),
		'hasDuration': $(elem).find('input[name="mainDataElementSpecifyDuration"]').val(),
		'minDuration': $(elem).find('input[name="mainDataElementMinDurationValue"]').val(),
		'minDurationUnits': $(elem).find('select[name="mainDataElementMinDurationUnits"]').val(),
		'maxDuration': $(elem).find('input[name="mainDataElementMaxDurationValue"]').val(),
		'maxDurationUnits': $(elem).find('select[name="mainDataElementMaxDurationUnits"]').val(),
		'hasPropertyConstraint': $(elem).find('input[name="mainDataElementSpecifyProperty"]').val(),
		'property': $(elem).find('select[name="mainDataElementPropertyName"]').val(),
		'propertyValue': $(elem).find('input[name="mainDataElementPropertyValue"]').val()
	}
}

function collectSequenceRelations ($relationElems) {
	//relation['propositionType'] = $proposition.data('type');
	var relations = new Array();
	$relationElems.each(function (i,r) {
		var $proposition = $(r).find('ul.sortable').find('li').first();
		relations.push({
			'dataElementField': {
				'dataElementKey': $proposition.data('key'),
				'withValue': $(r).find('select[name="sequenceRelDataElementValue"]').val(),
				'hasDuration': $(r).find('input[name="sequenceRelDataElementSpecifyDuration"]').val(),
				'minDuration': $(r).find('input[name="sequenceRelDataElementMinDurationValue"]').val(),
				'minDurationUnits':  $(r).find('select[name="sequenceRelDataElementMinDurationUnits"]').val(),
				'maxDuration': $(r).find('input[name="sequenceRelDataElementMaxDurationValue"]').val(),
				'maxDurationUnits':  $(r).find('select[name="sequenceRelDataElementMaxDurationUnits"]').val(),
				'hasPropertyConstraint': $(r).find('input[name="sequenceRelDataElementSpecifyProperty"]').val(),
				'property': $(r).find('select[name="sequenceRelDataElementPropertyName"]').val(),
				'propertyValue': $(r).find('input[name="sequenceRelDataElementPropertyValue"]').val()
			},
			'relationOperator': $(r).find('select[name="sequenceRelDataElementTemporalRelation"]').val(),
			'sequentialDataElement': $(r).find('select[name="propositionSelect"]').val(),
			'relationMinCount': $(r).find('input[name="sequenceRhsDataElementMinDistanceValue"]').val(),
			'relationMinUnits': $(r).find('select[name="sequenceRhsDataElementMinDistanceUnits"]').val(),
			'relationMaxCount': $(r).find('input[name="sequenceRhsDataElementMaxDistanceValue"]').val(),
			'relationMaxUnits': $(r).find('select[name="sequenceRhsDataElementMaxDistanceUnits"]').val()
		});
	});
	return relations;
}

function saveSequence (elem) {
	var sequence = new Object();
	var $relationElems = $(elem).find('.sequence-relations-container').find('.sequence-relation');

	sequence['@class'] = "edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence";
	sequence.abbrevDisplayName = $('input#propAbbrevDisplayName').val();
	sequence.displayName = $('textarea#propDisplayName').val();
	sequence.primaryDataElement = collectSequenceDataElement(elem);
	sequence.relatedDataElements = collectSequenceRelations($relationElems);

	postProposition('savesequence', sequence, function (data) {window.location.href = 'editorhome'});
}

function saveCategorical (elem) {
	console.log('SAVING CATEGORIZATION');
	var categorization = new Object();
	var childElements = new Array();
	var $propositions = $(elem).find('ul.sortable').find('li');

	categorization['@class'] = "edu.emory.cci.aiw.cvrg.eureka.common.comm.Categorization";
	categorization.abbrevDisplayName = $('input#propAbbrevDisplayName').val();
	categorization.displayName = $('textarea#propDisplayName').val();
	$propositions.each(function (i, p) {
		var child = {
		};
		childElements.push(child);
	});
	categorization.children = childElements;
	categorization.categoricalType = $(childElements[0]).data("subtype");
	postProposition('savecategorical', categorization, function (data) {window.location.href = 'editorhome'});
	postProposition("savecategorization", categorization, function (data) {window.location.href = 'editorhome'});
}


function setPropositionSelects () {
	var selects = $('select').filter('[name="propositionSelect"]');
	$(selects).each( function (i, sel) {
		var items = $(sel).closest('.drop-parent').find('li');
		$(sel).empty();
		$.each(possiblePropositions, function (key, val) {
			var add = true;
			$(items).each( function (i, item) {
				if ($(item).data('id') == key) {
					add = false;
					return false;
				} else {
					return true;
				}
			});
			if (add) {
				$(sel).append($('<option></option>').attr('value', key).text(val));
			}
		});
	});
}

$(document).ready(function(){

	$('#wizard').smartWizard({
		'labelFinish': 'Save',
		onLeaveStep:leaveAStepCallback,
		onFinish:onFinishCallback
	});

	var dialog = $('<div></div>')
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
		dialog.dialog('open');
	});

	$('#expand_div').hide();
	$('#collapse_div').hide();

	$('#categoricaldefinition').hide();
	$('#temporaldefinition').hide();
	$('#sequencedefinition').hide();
	$('#frequencydefinition').hide();
	$('#valuethresholddefinition').hide();

	function leaveAStepCallback(from, to){
		var result;
		var step_num_from = from.attr('rel');
		var step_num_to = to.attr('rel');

		if (step_num_to < step_num_from) {
			result = true;
		} else {
			result = validateSteps(step_num_from);
		}

		if (result) {
			if (step_num_to == 2) {
				var type = $("input:radio[name='type']:checked").val();
				$('#' + type + 'definition').show();
			} else {
				$('#categoricaldefinition').hide();
				$('#temporaldefinition').hide();
				$('#sequencedefinition').hide();
				$('#frequencydefinition').hide();
				$('#valuethresholddefinition').hide();
			}
		}

		return result;
	}

	function onFinishCallback(){
		//if(validateAllSteps()){
		var propositions = [];
		var type = $("input:radio[name='type']:checked").val();
		var abbrevDisplayName = $('#propAbbrevDisplayName').val();
		var displayName = $('#propDisplayName').val();
		var propId = $('#propId').val();

		propId = (typeof(propId) === 'undefined' ? "" : propId);
		saveFuncs[type]($('#' + type + 'definition'));
	}


	function validateSteps(step){
		var isStepValid = true;
		// validate step 1
		if (step == 1) {
			var type = $("input:radio[name='type']:checked").val();
			if (type == undefined || type == "") {
				isStepValid = false;
				$('#wizard').smartWizard('showMessage','Please select a type of element to create.');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:true
				});
			}
			else{
				$('#wizard').smartWizard('hideMessage','');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:false
				});
			}


		} else if (step == 2) {
			var type = $("input:radio[name='type']:checked").val();
			if ($('#' + type + 'definition ul.sortable').children().length == 0) {
				isStepValid = false;
				$('#wizard').smartWizard('showMessage','Please select an element from the ontology explorer.');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:true
				});
			}
			else{
				$('#wizard').smartWizard('hideMessage','');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:false
				});
			}

		} else if(step == 3) {
			var propAbbrevDisplayName = $('#propAbbrevDisplayName').val();
			var propDisplayName= $('#propDisplayName').val();

			if((!propAbbrevDisplayName && propAbbrevDisplayName.length <= 0) ||
				(!propDisplayName && propDisplayName.length <= 0)){
				isStepValid = false;
				$('#wizard').smartWizard('showMessage','Please enter an element name and description and click next.');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:true
				});
			}
			else{
				$('#wizard').smartWizard('hideMessage','');
				$('#wizard').smartWizard('setError',{
					stepnum:step,
					iserror:false
				});
			}
		}
		else if (step == 4) {
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

	$('a#add-to-sequence').click(function (e) {
		var total = $('table.sequence-relation').length;
		var data = $('table.sequence-relation').filter(':last').clone();
		var appendTo = $('td.sequence-relations-container');
		data.find('ul.sortable').empty();
		data.find('span.count').text(total + 1);
		appendTo.append(data);
		setPropositionSelects();
	});

});

function initTrees() {

	$("#systemTree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "systemlist" ,
				"data": function(n) {
					return {
						id : n.attr ? n.attr("id") : "root"
					};
				}

			}
		},
		"dnd" : {
			"drop_finish" : function(data) {
				var target = data.e.currentTarget;
				if (idIsNotInList(target, data.o[0].id)) {

					var propositionId = data.o[0].id;
					var propositionDesc = data.o[0].children[1].childNodes[1].textContent;

					var infoLabel = $(target).find('div.label-info');
					infoLabel.hide();

					var sortable = $(target).find('ul.sortable');
					var newItem = $('<li></li>', {
						"data-space": $(data.o[0]).data("space"),
						"data-type": $(data.o[0]).data("type"),
						"data-key": $(data.o[0]).data("proposition")
					});

					// check that all types in the categorization are the same
					if ($(sortable).data("proptype") != "empty") {
						if ($(sortable).data("proptype") != $(newitem).data("type")) {
							alert("All elements in a categorization must be of the same type");
						}
					} else {
						$(sortable).data("proptype", $(newitem).data("type"));
					}

					var X = $("<span/>", {
						class: "delete",
						click: function () {
							var toRemove = $(this).parent()[0];
							var dialog = $('<div></div>');
							$(dialog).dialog({
								title: 'Confirm removal of selected element',
								buttons : {
									"Confirm": function() {
										$(toRemove).remove();
										delete possiblePropositions[$(toRemove).data('id')];
										setPropositionSelects();
										if ($(sortable).find('li').length == 0) {
											infoLabel.show();
										}
										$(this).dialog("close");
										$(this).remove();
									},
									"Cancel": function() {
										$(this).dialog("close");
										$(this).remove();
									}
								}
							});
							$(dialog).html(this.parentNode.children[1].innerHTML);
							$(dialog).dialog("open");
						}
					});

					var txt = $("<span/>", {
						text : propositionDesc
					});

					newItem.append(X);
					newItem.append(txt);
					sortable.append(newItem);

					possiblePropositions[propositionId] = propositionDesc;
					setPropositionSelects();
				}
			},
			"drop_check": function (data) {
				var target = data.r;
				var sortable = $(target).find('ul.sortable');
				var datatype = $(sortable).data("proptype");
				
				if (datatype == "empty" || datatype == $(data.o).data("type")) {
					return true;
				} else {
					return false;
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
	}).bind("open_node.jstree", function(e, data)
	{
		if(data.rslt.obj[0].id == undefined) {
			alert("No href defined for this element");
		}
	});

	$("#userTree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "userproplist?id=root"
			}
		},
		"dnd" : {
			"drop_finish" : function(data) {
				var target = data.e.currentTarget;
				if (idIsNotInList(data.o[0].id)) {

					$('#label-info').hide();

					var target = $('#tree-drop')[0];
					target.style["background"] = "lightblue";

					var type = $('#type').val();

					var X = $("<span/>", {
						class: "delete"
					});
					var txt = $("<span/>", {
						text : data.o[0].children[1].childNodes[1].textContent
					});

					$('<li/>', {
						id: data.o[0].id,
						"data-type": $(data.o[0]).data("type"),
						"data-subtype": $(data.o[0]).data("subtype")
					}).append(X, txt).appendTo('#sortable');


					var txt = $('#sortable li:last').text();

					var width = txt.length * 7;
					if (width > dropBoxMaxTextWidth) {
						dropBoxMaxTextWidth = width;
						$('#sortable').width(dropBoxMaxTextWidth);
					}


					$('#sortable li:last').mouseover(function () {

						$(this.children[0]).css({
							cursor:"pointer",
							backgroundColor:"#24497A"
						});
					});
					$('#sortable li:last').mouseout(function () {

						$(this.children[0]).css({
							cursor:"pointer",
							backgroundColor:"lightblue"
						});
					});

					// function to enable removal of list item once it is clicked
					$($('#sortable li:last')[0].children[0]).click(function onItemClick() {
						$(this.parent).animate( {
							backgroundColor: "#CCC",
							color: "#333"
						}, 500);
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
function idIsNotInList(target, id) {
	var retVal = true;
	$(target).find('ul.sortable').find('li').each( function() {
		if (id == this.id) {
			retVal = false;
		}

	});
	return retVal;
}
