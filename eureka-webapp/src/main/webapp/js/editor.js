/* Eureka WebApp. Copyright (C) 2012 Emory University. Licensed under http://www.apache.org/licenses/LICENSE-2.0. */

eureka.dataElement = {
	post: function(postData, successFunc) {
		$.ajax({
			type: "POST",
			url: 'saveprop',
			//contentType: "application/json; charset=utf-8",
			data: JSON.stringify(postData),
			dataType: "json",
			success: function (data) {
				window.location.href = 'editorhome'
			}, 
			error: function(data, statusCode) {
				var dialog = $('<div>Could not save data element ' + postData.displayName + '. ' + data.responseText + '</div>');
				$(dialog).dialog({
					'title': 'Save Failed',
					'modal': true,
					'resizable': false,
					'buttons': {
						"OK": function() {
							$(this).dialog("close");
							$(this).remove();
						}
					}
				});
				$(dialog).dialog("open");
			} 
		});
	},

	collectSequence: function(elem) {
		// var type = $mainProposition.data('type');
		var $sortable = $(elem).find('ul.sortable[data-type="main"]');
		var $mainProposition = $sortable.find('li').first();
		return {
			'id': $sortable.data('count'),
			'dataElementKey': $mainProposition.data('key'),
			'hasDuration': $(elem).find('input[name="mainDataElementSpecifyDuration"]').is(':checked'),
			'minDuration': $(elem).find('input[name="mainDataElementMinDurationValue"]').val(),
			'minDurationUnits': $(elem).find('select[name="mainDataElementMinDurationUnits"]').val(),
			'maxDuration': $(elem).find('input[name="mainDataElementMaxDurationValue"]').val(),
			'maxDurationUnits': $(elem).find('select[name="mainDataElementMaxDurationUnits"]').val(),
			'hasPropertyConstraint': $(elem).find('input[name="mainDataElementSpecifyProperty"]').is(':checked'),
			'property': $(elem).find('select[name="mainDataElementPropertyName"]').val(),
			'propertyValue': $(elem).find('input[name="mainDataElementPropertyValue"]').val()
		}
	},

	collectSequenceRelations: function($relationElems) {
		var relations = new Array();

		$relationElems.each(function (i,r) {
			var $sortable = $(r).find('ul.sortable');
			var $proposition = $sortable.find('li').first();
			var id = $sortable.data('count');
			var key = $proposition.data('key');
			var sequentialValue = $(r).find('select[name="propositionSelect"]').val();
			var sequentialData = sequentialValue.split('__');
			var sequentialDataElement = sequentialData[0];
			var sequentialDataElementSource = sequentialData[1];

			if (key) {
				relations.push({
					'dataElementField': {
						'id': id,
						'dataElementKey': $proposition.data('key'),
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
					'sequentialDataElement': sequentialDataElement,
					'sequentialDataElementSource': sequentialDataElementSource,
					'relationMinCount': $(r).find('input[name="sequenceRhsDataElementMinDistanceValue"]').val(),
					'relationMinUnits': $(r).find('select[name="sequenceRhsDataElementMinDistanceUnits"]').val(),
					'relationMaxCount': $(r).find('input[name="sequenceRhsDataElementMaxDistanceValue"]').val(),
					'relationMaxUnits': $(r).find('select[name="sequenceRhsDataElementMaxDistanceUnits"]').val()
				});
			}
		});
		return relations;
	},

	collectValueThresholds: function($valueThresholds) {
		var valueThresholdsArr = new Array();

		$valueThresholds.each(function (i,r) {
			var dataEltFromDropBox = $(r).find('div.thresholdedDataElement').find('li');
			var dataElt = eureka.dataElement.collectDataElement(dataEltFromDropBox);

			var $relatedDataElements = $(r).find('div.thresholdedRelatedDataElements').find('li');

			valueThresholdsArr.push({
				'dataElement': dataElt,
				'lowerComp': $(r).find('select[name="thresholdLowerComp"]').val(),
				'lowerValue': $(r).find('input[name="thresholdLowerVal"]').val(),
				'lowerUnits': null, //placeholder
				'upperComp': $(r).find('select[name="thresholdUpperComp"]').val(),
				'upperValue': $(r).find('input[name="thresholdUpperVal"]').val(),
				'upperUnits': null, //placeholder
				'relationOperator': $(r).find('select[name="thresholdDataElementTemporalRelation"]').val(),
				'relatedDataElements': eureka.dataElement.collectDataElements($relatedDataElements),
				'withinAtLeast': $(r).find('input[name="thresholdWithinAtLeast"]').val(),
				'withinAtLeastUnit': $(r).find('select[name="thresholdWithinAtLeastUnits"]').val(),
				'withinAtMost': $(r).find('input[name="thresholdWithinAtMost"]').val(),
				'withinAtMostUnit': $(r).find('select[name="thresholdWithinAtMostUnits"]').val()
			});
		});
		return valueThresholdsArr;
	},

	collectDataElements: function($dataElementsFromDropBox) {
		var childElements = new Array();

		$dataElementsFromDropBox.each(function (i, p) {
			childElements.push(eureka.dataElement.collectDataElement(p));
		});

		return childElements;
	},

	collectDataElement: function(dataElementFromDropBox) {
		var system = $(dataElementFromDropBox).data('space') === 'system'
		|| $(dataElementFromDropBox).data('space') === 'SYSTEM';
		var child = {
			'dataElementKey': $(dataElementFromDropBox).data('key')
		};

		if (system) {
			child['type'] =  'SYSTEM';
		} else {
			child['type'] =  $(dataElementFromDropBox).data('type');
		}
		return child;
	},

	saveSequence: function(elem) {
		var sequence = new Object();
		var $relationElems = $(elem).find('.sequence-relations-container').find('.sequence-relation');
		var propId = $('#propId').val();

		if (propId) {
			sequence.id = propId;
		}
		sequence.type = 'SEQUENCE';
		sequence.displayName = $('input#propDisplayName').val();
		sequence.description = $('textarea#propDescription').val();
		sequence.primaryDataElement = eureka.dataElement.collectSequence(elem);
		sequence.relatedDataElements = eureka.dataElement.collectSequenceRelations($relationElems);

		eureka.dataElement.post(sequence);
	},

	saveValueThreshold: function(elem) {
		var valueThreshold = new Object();

		var $valueThresholds = $(elem).find('.value-thresholds-container').find('.value-threshold');

		var propId = $('#propId').val();
		if (propId) {
			valueThreshold.id = propId;
		}
		valueThreshold.type = 'VALUE_THRESHOLD';
		valueThreshold.displayName = $('input#propDisplayName').val();
		valueThreshold.description = $('textarea#propDescription').val();
		valueThreshold.name = $(elem).find('input[name="valueThresholdValueName"]').val();
		valueThreshold.thresholdsOperator = $(elem).find('select[name="valueThresholdType"]').val();
		valueThreshold.valueThresholds = eureka.dataElement.collectValueThresholds($valueThresholds);

		eureka.dataElement.post(valueThreshold);
	},

	saveCategorization: function(elem) {
		var $memberDataElements = $(elem).find('ul.sortable').find('li');
		var childElements = eureka.dataElement.collectDataElements($memberDataElements);

		var categoricalType = $('#propSubType').val();

		var categorization = {
			'type': 'CATEGORIZATION',
			'displayName': $('input#propDisplayName').val(),
			'description': $('textarea#propDescription').val(),
			'categoricalType': categoricalType,
			'children': childElements
		}

		var propId = $('#propId').val();
		if (propId) {
			categorization.id = propId;
		}

		eureka.dataElement.post(categorization);
	},

	saveFrequency: function(elem) {
		var $dataElement = $(elem).find('ul[data-type="main"]').find('li').first();
		var frequency = {
			'type': 'FREQUENCY',
			'frequencyType': $('select[name="freqTypes"]').val(),
			'displayName': $('input#propDisplayName').val(),
			'description': $('textarea#propDescription').val(),
			'atLeast': $(elem).find('input[name=freqAtLeastField]').val(),
			'isConsecutive': $(elem).find('input[name=freqIsConsecutive]').is(':checked'),
			'dataElement': {
				'dataElementKey': $dataElement.data('key'),
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

		eureka.dataElement.post(frequency);
	},
	
	save: function(dataElementType, elem) {
		var saveFuncs = {
			'SEQUENCE': eureka.dataElement.saveSequence,
			'CATEGORIZATION': eureka.dataElement.saveCategorization,
			'FREQUENCY': eureka.dataElement.saveFrequency,
			'VALUE_THRESHOLD': eureka.dataElement.saveValueThreshold
		};
		saveFuncs[dataElementType](elem);
	}
}
// END "namespacing"

$("ul.sortable").sortable();
$("ul.sortable").disableSelection();

function enableFrequencyFields(dropped) {
	if ($(dropped).data('type') == 'VALUE_THRESHOLD') {
		$('#valueThresholdConsecutiveLabel').css('display','inline');
	}
}

function disableFrequencyFields() {
	$('#valueThresholdConsecutiveLabel').css('display','none');
}

$(document).ready(function(){

	$('#wizard').smartWizard({
		'labelFinish': 'Save',
		keyNavigation: false,
		onLeaveStep:leaveAStepCallback,
		onFinish:onFinishCallback
	});

	$('#expand_div').hide();
	$('#collapse_div').hide();

	$('#CATEGORIZATIONdefinition').hide();
	$('#temporaldefinition').hide();
	$('#SEQUENCEdefinition').hide();
	$('#FREQUENCYdefinition').hide();
	$('#VALUE_THRESHOLDdefinition').hide();

	// make sure all the 'drop here' labels are hidden if we are editing
	// an existing proposition.  Also populate any proposition select
	// dropdowns if needed.
	if ($('#propId').val()) {
		var propType = $('input[name="type"]:checked').val();
		var $def = $('#' + propType + 'definition');
		var $sortables = $def.find('ul.sortable');

		$('div.label-info').hide();
		$sortables.each(function (i, list) {
			$(list).find('li').each(function (j, item) {
				eureka.trees.addDroppedElement(propType, item, $(list));
			});
		});
		eureka.trees.setPropositionSelects($def);
	}

	// make any deletable items actually delete
	$('ul.sortable').find('li').find('span.delete').each(function(i,item) {
		eureka.trees.attachDeleteAction(item);
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
				$('#CATEGORIZATIONdefinition').hide();
				$('#temporaldefinition').hide();
				$('#SEQUENCEdefinition').hide();
				$('#FREQUENCYdefinition').hide();
				$('#VALUE_THRESHOLDdefinition').hide();
			}
		}

		return result;
	}

	function onFinishCallback(){
		var type = $("input:radio[name='type']:checked").val();
		eureka.dataElement.save(type, $('#' + type + 'definition'));
	}


	function validateSteps(step){
		var isStepValid = true;
		// validate step 1
		if (step == 1) {
			var type = $("input:radio[name='type']:checked").val();
			if (type == null || type == "") {
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
			var propDisplayName = $('#propDisplayName').val();
			var propDescription= $('#propDescription').val();

			if((!propDisplayName && propDisplayName.length <= 0) ||
				(!propDescription && propDescription.length <= 0)){
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
	
	var dndActions = {
		'FREQUENCY': enableFrequencyFields
	};
	var deleteActions = {
		'FREQUENCY': disableFrequencyFields
	};
	eureka.trees.init(dndActions, deleteActions);

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
		var total = $('fieldset.sequence-relation').length;
		var newCount = total + 1;
		var data = $('fieldset.sequence-relation').filter(':last').clone();
		var appendTo = $('td.sequence-relations-container');
		var sortable = data.find('ul.sortable');
		var inputPropertycheckbox = data.find('input.propertyValueConstraint');
		$(inputPropertycheckbox).removeAttr('disabled');
		var inputProperty=data.find('input.propertyValueField');
		$(inputProperty).removeAttr('disabled');
		sortable.empty();
		sortable.attr('data-count', newCount + 1);
		data.find('span.count').text(newCount);
		//data.find('div.thresholdedDataElement').attr('id','thresholdedDataElement' + newCount);
		data.find('div.sequencedDataElement').attr('id','relatedDataElement' + newCount);
		data.find('select[name="sequenceRelDataElementPropertyName"]').attr('data-properties-provider','relatedDataElement' + newCount);
		appendTo.append(data);
		eureka.trees.setPropositionSelects($(appendTo).closest('[data-definition-container="true"]'));
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

function showHelp() {

}

