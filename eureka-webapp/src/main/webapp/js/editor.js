/* Eureka WebApp. Copyright (C) 2012 Emory University. Licensed under http://www.apache.org/licenses/LICENSE-2.0. */

$("ul.sortable").sortable();
$("ul.sortable").disableSelection();

var dropBoxMaxTextWidth = 275;
var possiblePropositions = new Object();
var saveFuncs = {
	'sequence': saveSequence,
	'categorization': saveCategorization,
	'frequency': saveFrequency,
	'valuethreshold': saveValueThreshold
};

function postProposition (postData, successFunc) {
	$.ajax({
		type: "POST",
		url: 'saveprop',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(postData),
		dataType: "json",
		success: function (data) {
			window.location.href = 'editorhome'
		}
	});
}


function collectSequenceDataElement (elem) {
	// var type = $mainProposition.data('type');
	var $mainProposition = $(elem).find('ul[data-type="main"]').find('li').first();
	return {
		'dataElementKey': $mainProposition.data('key'),
		'withValue': $(elem).find('select[name="mainDataElementValue"]').val(),
		'hasDuration': $(elem).find('input[name="mainDataElementSpecifyDuration"]').is(':checked'),
		'minDuration': $(elem).find('input[name="mainDataElementMinDurationValue"]').val(),
		'minDurationUnits': $(elem).find('select[name="mainDataElementMinDurationUnits"]').val(),
		'maxDuration': $(elem).find('input[name="mainDataElementMaxDurationValue"]').val(),
		'maxDurationUnits': $(elem).find('select[name="mainDataElementMaxDurationUnits"]').val(),
		'hasPropertyConstraint': $(elem).find('input[name="mainDataElementSpecifyProperty"]').is(':checked'),
		'property': $(elem).find('select[name="mainDataElementPropertyName"]').val(),
		'propertyValue': $(elem).find('input[name="mainDataElementPropertyValue"]').val()
	}
}

function collectSequenceRelations ($relationElems) {
	var relations = new Array();

	$relationElems.each(function (i,r) {
		var $proposition = $(r).find('ul.sortable').find('li').first();
		var key = $proposition.data('key');

		if (key) {
			relations.push({
				'dataElementField': {
					'dataElementKey': $proposition.data('key'),
					'withValue': $(r).find('select[name="sequenceRelDataElementValue"]').val(),
					'hasDuration': $(r).find('input[name="sequenceRelDataElementSpecifyDuration"]').is(':checked'),
					'minDuration': $(r).find('input[name="sequenceRelDataElementMinDurationValue"]').val(),
					'minDurationUnits':  $(r).find('select[name="sequenceRelDataElementMinDurationUnits"]').val(),
					'maxDuration': $(r).find('input[name="sequenceRelDataElementMaxDurationValue"]').val(),
					'maxDurationUnits':  $(r).find('select[name="sequenceRelDataElementMaxDurationUnits"]').val(),
					'hasPropertyConstraint': $(r).find('input[name="sequenceRelDataElementSpecifyProperty"]').is(':checked'),
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
		}
	});
	return relations;
}

function saveSequence (elem) {
	var sequence = new Object();
	var $relationElems = $(elem).find('.sequence-relations-container').find('.sequence-relation');
	var propId = $('#propId').val();

	if (propId) {
		sequence.id = propId;
	}
	sequence.type = 'sequence';
	sequence.abbrevDisplayName = $('input#propAbbrevDisplayName').val();
	sequence.displayName = $('textarea#propDisplayName').val();
	sequence.primaryDataElement = collectSequenceDataElement(elem);
	sequence.relatedDataElements = collectSequenceRelations($relationElems);

	postProposition(sequence);
}

function saveValueThreshold (elem) {
	var valueThreshold = new Object();
	
	var $valueThresholds = $(elem).find('.value-thresholds-container').find('.value-threshold');
	
	var propId = $('#propId').val();
	if (propId) {
		valueThreshold.id = propId;
	}
	valueThreshold.type = 'valuethreshold';
	valueThreshold.abbrevDisplayName = $('input#propAbbrevDisplayName').val();
	valueThreshold.displayName = $('textarea#propDisplayName').val();
	valueThreshold.name = $(elem).find('input[name="valueThresholdValueName"]').val();
	valueThreshold.thresholdsOperator = $(elem).find('select[name="valueThresholdType"]').val();
	valueThreshold.valueThresholds = collectValueThresholds($valueThresholds);

	postProposition(valueThreshold);
}

function collectValueThresholds ($valueThresholds) {
	var valueThresholdsArr = new Array();
	
	
	$valueThresholds.each(function (i,r) {
		var thresholdedDataEltFromDropBox = 
		$(r).find('ul.sortable').find('li').first();
		var thresholdedDataElement = collectDataElement(thresholdedDataEltFromDropBox);
		
		var $relatedDataElements = $(r).find('ul.sortable').find('li');

		valueThresholdsArr.push({
			'dataElement': thresholdedDataElement,
			'lowerComp': $(r).find('select[name="thresholdLowerComp"]').val(),
			'lowerValue': $(r).find('input[name="thresholdLowerVal"]').val(),
			'lowerUnits': null, //placeholder
			'upperComp': $(r).find('select[name="thresholdUpperComp"]').val(),
			'upperValue': $(r).find('input[name="thresholdUpperVal"]').val(),
			'upperUnits': null, //placeholder
			'isBeforeOrAfter': $(r).find('select[name="thresholdDataElementTemporalRelationCB"]').is(":checked"),
			'relationOperator': $(r).find('select[name="thresholdDataElementTemporalRelation"]').val(),
			'relatedDataElements': collectRelatedDataElementKeys($relatedDataElements),
			'atLeastCount': $(r).find('input[name="thresholdWithinAtLeast"]').val(),
			'atLeastTimeUnit': $(r).find('select[name="thresholdWithinAtLeastUnits"]').val(),
			'atMostCount': $(r).find('input[name="thresholdWithinAtMost"]').val(),
			'atMostTimeUnit': $(r).find('select[name="thresholdWithinAtMostUnits"]').val()
		});
	});
	return valueThresholdsArr;
}

function collectRelatedDataElementKeys($relatedDataElements) {
	var keys = new Array();
	
	$relatedDataElements.each(function (i, p) {
		keys.push($(p).data('key'));
	});
	
	return keys;
}

function saveCategorization (elem) {
	var $memberDataElements = $(elem).find('ul.sortable').find('li');
	var childElements = collectDataElements($memberDataElements);

	var categoricalType = $(elem).find('ul.sortable').data('proptype');
	if (categoricalType == 'empty') {
		categoricalType = $('#propSubType').val();
	}

	var categorization = {
		'type': 'categorization',
		'abbrevDisplayName': $('input#propAbbrevDisplayName').val(),
		'displayName': $('textarea#propDisplayName').val(),
		'categoricalType': categoricalType,
		'children': childElements
	}

	var propId = $('#propId').val();
	if (propId) {
		categorization.id = propId;
	}

	postProposition(categorization);
}

function collectDataElements($dataElementsFromDropBox) {
	var childElements = new Array();

	$dataElementsFromDropBox.each(function (i, p) {
		childElements.push(collectDataElement(p));
	});
	
	return childElements;
}

function collectDataElement(dataElementFromDropBox) {
	var system = $(dataElementFromDropBox).data('space') === 'system';
	var child = {
		'key': $(dataElementFromDropBox).data('key')
	};

	if (system) {
		child['type'] =  'system';
	} else {
		child['type'] =  $(dataElementFromDropBox).data('type').toLowerCase();
		if (child['type'] === 'categorization') {
			child['categoricalType'] = 
			$(dataElementFromDropBox).data('subtype');
		}
	}
	return child;
}

function saveFrequency (elem) {
	var $dataElement = $(elem).find('ul[data-type="main"]').find('li').first();
	var frequency = {
		'type': 'frequency',
		'abbrevDisplayName': $('input#propAbbrevDisplayName').val(),
		'displayName': $('textarea#propDisplayName').val(),
		'atLeast': $(elem).find('input[name=freqAtLeastField]').val(),
		'isConsecutive': $(elem).find('input[name=freqIsConsecutive]').is(':checked'),
		'dataElement': {
			'dataElementKey': $dataElement.data('key'),
			'withValue': $(elem).find('select[name="freqDataElementValue"]').val(),
			'hasDuration': $(elem).find('input[name="freqDataElementSpecifyDuration"]').is(':checked'),
			'minDuration': $(elem).find('input[name="freqDataElementMinDurationValue"]').val(),
			'minDurationUnits': $(elem).find('select[name="freqDataElementMinDurationUnits"]').val(),
			'maxDuration': $(elem).find('input[name="freqDataElementMaxDurationValue"]').val(),
			'maxDurationUnits': $(elem).find('select[name="freqDataElementMaxDurationUnits"]').val(),
			'hasPropertyConstraint': $(elem).find('input[name="freqDataElementSpecifyProperty"]').is(':checked'),
			'property': $(elem).find('select[name="freqDataElementPropertyName"]').val(),
			'propertyValue': $(elem).find('input[name="freqDataElementPropertyValue"]').val()
		},
		'isWithin': $(elem).find('input[name=freqIsWithin]').is(':checked'),
		'withinAtLeast': $(elem).find('input[name=freqWithinAtLeast]').val(),
		'withinAtLeastUnits': $(elem).find('select[name=freqWithinAtLeastUnits]').val(),
		'withinAtMost': $(elem).find('input[name=freqWithinAtMost]').val(),
		'withinAtMostUnits': $(elem).find('select[name=freqWithinAtMostUnits]').val()
	}
	
	var propId = $('#propId').val();

	if (propId) {
		frequency.id = propId;
	}
	
	postProposition(frequency);
}

function addPossibleProposition (key, desc) {
	if (possiblePropositions[key]) {
		possiblePropositions[key].count++;
	} else {
		possiblePropositions[key] = new Object();
		possiblePropositions[key].count = 1;
		possiblePropositions[key].desc = desc;
	}
}

function removePossibleProposition (key) {
	if (possiblePropositions[key] && possiblePropositions[key].count > 0) {
		possiblePropositions[key].count--;
	}
}


function setPropositionSelects (elem) {
	var selects = $(elem).find('select').filter('[name="propositionSelect"]');
	$(selects).each( function (i, sel) {
		var items = $(sel).closest('.drop-parent').find('li');
		$(sel).empty();
		$.each(possiblePropositions, function (key, val) {
			if (possiblePropositions[key].count > 0) {
				var add = true;
				$(items).each( function (i, item) {
					if ($(item).data('key') == key) {
						add = false;
					}
				});
				if (add) {
					$(sel).append($('<option></option>').attr('value', key).text(possiblePropositions[key].desc));
				}
			}
		});
	});
}

function attachDeleteAction (elem) {
	$(elem).each(function(i, item) {
		$(item).click(function () {
			var $toRemove = $(item).closest('li');
			var $sortable = $toRemove.closest('ul.sortable');
			var $infoLabel = $sortable.siblings('div.label-info');
			var $target = $sortable.parent();
			var dialog = $('<div></div>');
			$(dialog).dialog({
				'title': 'Confirm removal of selected element',
				'modal': true,
				'buttons': {
					"Confirm": function() {
						removePossibleProposition($toRemove.data('key'));
						setPropositionSelects($sortable.closest('[data-definition-container="true"]'));
						$toRemove.remove();
						if ($sortable.find('li').length == 0) {
							$sortable.data('proptype','empty');
							$infoLabel.show();
						}

						// remove the properties from the drop down
						$('select[data-properties-provider=' + $target.attr('id') + ']').each(function (i, item) {
							$(item).empty();
						});

						$(this).dialog("close");
						$(this).remove();
					},
					"Cancel": function() {
						$(this).dialog("close");
						$(this).remove();
					}
				}
			});
			$(dialog).html($toRemove.text());
			$(dialog).dialog("open");
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
		'to the drop box to make it a member of the category.' +
		'</p>' +
		'<p>' +
		'For Sequence:<br/>' +
		'Coming soon...' +
		'</p>' +
		'<p>' +
		'For Frequency:<br/>' +
		'Coming soon...' +
		'</p>' +
		'<p>' +
		'For Value Threshold:<br/>' +
		'Coming soon...' +
		'</p>'
		)
	.dialog({
		autoOpen: false,
		title: 'Building New Data Elements Help'
	});

	$('#help_select').click( function() {
		dialog.dialog('open');
	});

	$('#expand_div').hide();
	$('#collapse_div').hide();

	$('#categorizationdefinition').hide();
	$('#temporaldefinition').hide();
	$('#sequencedefinition').hide();
	$('#frequencydefinition').hide();
	$('#valuethresholddefinition').hide();

	// make sure all the 'drop here' labels are hidden if we are editing
	// an existing proposition.  Also populate any proposition select
	// dropdowns if needed.
	if ($('#propId').val()) {
		$('div.label-info').hide();
		$('ul.sortable').find('li').each(function (i, item) {
			addPossibleProposition($(item).data('key'), $(item).data('key'));
		});
		var type = $('input[name="type"]:checked').val();
		setPropositionSelects($('#' + type + 'definition'));
	}

	// make any deletable items actually delete
	$('ul.sortable').find('li').find('span.delete').each(function(i,item) {
		attachDeleteAction(item);
	});

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
				$('#categorizationdefinition').hide();
				$('#temporaldefinition').hide();
				$('#sequencedefinition').hide();
				$('#frequencydefinition').hide();
				$('#valuethresholddefinition').hide();
			}
		}

		return result;
	}

	function onFinishCallback(){
		var type = $("input:radio[name='type']:checked").val();
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

	/**
	 * Insert search-form
	 */
	// This doesn't work yet because we don't load all of the nodes
	//	$('#systemTree').before(
	//			$('<form id="search"><span></span><input type="text" value=""><input type="submit" value="search"><input type="reset" value="X"></form>').
	//			bind({
	//				reset: function(evt){
	//					$('#systemTree').jstree('clear_search');
	//					$('#search span').html('');
	//				},
	//				submit: function(evt){
	//					var searchvalue = $('#search input[type="text"]').val();
	//					if(searchvalue!='') {
	//						$('#systemTree').jstree('search', searchvalue);
	//						$('#search span').html('');
	//					} else {
	//						$('#systemTree').jstree('clear_search');
	//						$('#search span').html('Please enter searchvalue');
	//					}
	//					return false;
	//				}
	//			})
	//	);

	$('a#add-to-sequence').click(function (e) {
		var total = $('table.sequence-relation').length;
		var newCount = total + 1;
		var data = $('table.sequence-relation').filter(':last').clone();
		var appendTo = $('td.sequence-relations-container');
		data.find('ul.sortable').empty();
		data.find('span.count').text(newCount);
		data.find('div.thresholdedDataElement').attr('id','thresholdedDataElement' + newCount);
		data.find('select[name="sequenceRelDataElementPropertyName"]').attr('data-properties-provider','relatedDataElement' + newCount);
		appendTo.append(data);
		setPropositionSelects($(appendTo).closest('[data-definition-container="true"]'));
	});
	
	$('a#add-threshold').click(function (e) {
		var total = $('tr.value-threshold').length;
		var newCount  = total + 1;
		var data = $('tr.value-threshold').filter(':last').clone();
		var appendTo = $('table.value-thresholds-container');
		data.find('ul.sortable').empty();
		data.find('div.jstree-drop').attr('id', 'threshold' + newCount);
		appendTo.append(data);
	});

});

function dropFinishCallback (data) {
	var target = data.e.currentTarget;
	var textContent = data.o[0].children[1].childNodes[1].textContent;

	if (idIsNotInList(target, data.o[0].id)) {

		var infoLabel = $(target).find('div.label-info');
		infoLabel.hide();

		var sortable = $(target).find('ul.sortable');
		var newItem = $('<li></li>')
		.data("space", $(data.o[0]).data("space"))
		.data("desc", textContent)
		.data("type", $(data.o[0]).data("type"))
		.data("subtype", $(data.o[0]).data("subtype") || '')
		.data("key", $(data.o[0]).data("proposition") || $(data.o[0]).data('key'));

		// set the properties in the properties select
		if ($(data.o[0]).data('properties')) {
			var properties = $(data.o[0]).data('properties').split(",");
			$('select[data-properties-provider=' + $(target).attr('id') + ']').each(function (i, item) {
				$(item).empty();
				$(properties).each(function (j, property) {
					$(item).append($('<option></option>').attr('value', property).text(property));
				});
			});
		}

		// check that all types in the categorization are the same
		if ($(sortable).data('drop-type') === 'multiple' && $(sortable).data("proptype") !== "empty") {
			if ($(sortable).data("proptype") !== $(newItem).data("type")) {
				var $dialog = $('<div>All the definition elements must be of the same type.</div>').dialog({
					'title': 'Definition Criteria',
					'modal': true,
					'buttons': {
						'OK': function () {
							$(this).dialog('close');
							$(this).remove();
						}
					}
				});
				return;
			}
		} else {
			var tmptype = $(newItem).data("type");
			$(sortable).data("proptype", tmptype);
		}

		var X = $("<span></span>", {
			class: "delete"
		});
		attachDeleteAction(X);

		var txt = $("<span></span>", {
			text : textContent
		});

		if ($(sortable).data('drop-type') === 'single') {
			$(sortable).find('li').each(function (i,item) {
				removePossibleProposition($(item).data('key'));
				$(item).remove();
			});
		}

		newItem.append(X);
		newItem.append(txt);
		sortable.append(newItem);

		addPossibleProposition(newItem.data('key'), txt.text());
		setPropositionSelects($(sortable).closest('[data-definition-container="true"]'));
	}
}


function initTrees() {

	$("#systemTree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "systemlist" ,
				"data": function(n) {
					return {
						key : n.attr ? n.attr("data-key") : "root"
					};
				}

			}
		},
		"crrm" : {
			// prevent movement and reordering of nodes
			"move" : {
				"check_move" : function (m) {
					return false;
				}
			}
		},
		"dnd" : {
			"drop_finish": dropFinishCallback,
			"drop_check": function (data) {
				var target = data.r;
				var sortable = $(target).find('ul.sortable');
				var datatype = $(sortable).data("proptype");
				var droppable = false;

				if (datatype == "empty" || datatype == $(data.o).data("type") || $(sortable).data('drop-type') === 'single') {
					droppable = true;
				}
				
				return droppable;
			}
		/*
			,
			"drag_check" : function(data) {
				if (data.r.attr("id") == "phtml_1") {
					return false;
				}
				return {
					after : false,
					before : false,
					inside : true
				};
			}
			*/
		},
		// search disabled until we figure out a way to search for nodes not currently loaded in the tree
		//		"search" : {
		//			"show_only_matches" : true,
		//		},
		"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd"/*, "search"*/ ]
	})
	/*
	.bind("open_node.jstree", function(e, data)
	{
		if(data.rslt.obj[0].id == undefined) {
			alert("No href defined for this element");
		}
	})
	*/
	;

	$("#userTree").jstree({
		"json_data" : {
			"ajax" : {
				"url" : "userproplist?key=root"
			}
		},
		"dnd" : {
			"drop_finish" : dropFinishCallback
		/*
		,
			"drag_check" : function(data) {
				if (data.r.attr("id") == "phtml_1") {
					return false;
				}
				return {
					after : false,
					before : false,
					inside : true
				};
			}
			*/
		},
		"plugins" : [ "themes", "json_data", "ui", "dnd" ]
	})
/*
	.bind("select_node.jstree", function(e, data)
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


	})
	*/
;



}

function showHelp() {

}
function idIsNotInList(target, id) {
	var retVal = true;
	$(target).find('ul.sortable').find('li').each( function(i, item) {
		if ($(item).data('key') == id) {
			retVal = false;
		}

	});
	return retVal;
}
