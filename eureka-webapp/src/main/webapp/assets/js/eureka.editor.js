// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.editor = new function () {

	var self = this;

	self.propType = null;
	self.propSubType = null;
        self.propId = null;
	self.droppedElements = {};

	self.setup = function (propType, propSubType, propId, systemTreeElem, userTreeElem, containerElem, saveButtonElem, deleteButtonElem, listElem, treeCssUrl, searchModalElem,
	searchValidationModalElem,searchNoResultsModalElem,searchUpdateDivElem) {
		self.propId = propId;
		self.propType = propType;
		self.propSubType = (propSubType == '') ? null : propSubType;

		if (propId != null && propId != '') {
			$('.label-info').hide();
		}

		$(saveButtonElem).on('click', function () {
			self.save($(containerElem));
		});
		
		self.attachClearModalHandlers();

		self.attachDeleteAction(deleteButtonElem);

		$(listElem).each(function (i, list) {
			$(list).find('li').each(function (j, item) {
				self.addDroppedElement(item, $(list));
			});
		});

		self.setPropositionSelects($(containerElem));
		eureka.tree.setupUserTree(userTreeElem, self.dropFinishCallback);
		eureka.tree.setupSystemTree(systemTreeElem, treeCssUrl, searchModalElem, self.dropFinishCallback,searchValidationModalElem,searchNoResultsModalElem,
		searchUpdateDivElem);
	};
	
	self.attachClearModalHandlers = function () {
		var deleteModal = $('#deleteModal');
		if (deleteModal) {
			var $deleteButton = $(deleteModal).find('#deleteButton');
			$(deleteModal).on('hidden.bs.modal', function(e) {
				$deleteButton.off('click');
			});
		}
		var replaceModal = $('#replaceModal');
		if (replaceModal) {
			var $replaceButton = $(replaceModal).find('#replaceButton');
			$(replaceModal).on('hidden.bs.modal', function(e) {
				$replaceButton.off('click');
			});
		}
	};

	self.objSize = function (obj) {
		var size = 0;
		for (var key in obj) {
			if (obj.hasOwnProperty(key)) {
				size++;
			}
		}
		return size;
	};

	self.getIn = function (obj, path) {
		var current = obj;
		for (var i = 0; i < path.length; i++) {
			current = current[path[i]];
			if (!current) {
				break;
			}
		}
		return current;
	};

	self.setIn = function (obj, path, value) {
		var current = obj;
		for (var i = 0; i < path.length - 1; i++) {
			var tmp = current[path[i]];
			if (!tmp) {
				tmp = {};
				current[path[i]] = tmp;
			}
			current = tmp;
		}
		current[path[path.length - 1]] = value;
	};

	self.enableFrequencyFields = function (dropped) {
		if ($(dropped).data('type') == 'VALUE_THRESHOLD') {
			$('#valueThresholdConsecutiveLabel').removeClass('hide');
		}
	};

	self.disableFrequencyFields = function () {
		$('#valueThresholdConsecutiveLabel').addClass('hide');
	};

	self.dndActions = {
		'FREQUENCY': self.enableFrequencyFields
	};
	self.deleteActions = {
		'FREQUENCY': self.disableFrequencyFields
	};

	self.deleteElement = function (displayName, key) {
		var $dialog = $('<div></div>')
			.html('Are you sure you want to delete phenotype &quot;' +
				displayName.trim() + '&quot;? You cannot undo this action.')
			.dialog({
				title: "Delete Data Element",
				modal: true,
				resizable: false,
				buttons: {
					"Delete": function () {
						$.ajax({
							type: 'POST',
							url: 'deleteprop?key=' + encodeURIComponent(key),
							success: function (data) {
								$(this).dialog("close");
								window.location.href = "editorhome";
							},
							error: function (data, statusCode) {
								var $errorDialog = $('<div></div>')
									.html(window.eureka.util.sanitizeResponseText(data.responseText))
									.dialog({
										title: "Error Deleting Data Element",
										buttons: {
											"OK": function () {
												$(this).dialog("close");
											}
										},
										close: function () {
											$dialog.dialog("close");
										}
									});
								$errorDialog.dialog("open");
							}
						});
					},
					"Cancel": function () {
						$(this).dialog("close");
					}
				},
				close: function () {
					// do nothing here.
				}
			});
		$dialog.dialog("open");
	};

	self.addDroppedElement = function (dropped, dropTarget) {
		var elementKey = $(dropped).data('key');
		var sourceId = $(dropTarget).data('count');
		var sourcePath = [self.propType, elementKey, 'sources', sourceId];
		var defPath = [self.propType, elementKey, 'definition'];
		var definition = self.getIn(self.droppedElements, defPath);

		self.setIn(self.droppedElements, sourcePath, dropTarget);
		if (!definition) {
			var properties = ['key', 'desc', 'type', 'subtype', 'space'];
			definition = {};
			$.each(properties, function (i, property) {
				definition[property] = $(dropped).data(property);
			});
			self.setIn(self.droppedElements, defPath, definition);
		}

		var allSourcesPath = [self.propType, elementKey, 'sources'];
		var allSources = self.getIn(self.droppedElements, allSourcesPath);
		var size = self.objSize(allSources);
		if (size > 1) {
			for (var key in allSources) {
				if (allSources.hasOwnProperty(key)) {
					var source = allSources[key];
					var items = $(source).find('li');
					$(items).each(function (i, item) {
						var span = $(item).find('span.desc');
						var newText = $(dropped).data('desc') + ' [' + $(source).data('count') + ']';
						$(span).text(newText);
					});
				}
			}
		}
	};

	self.idIsNotInList = function (target, id) {
		var retVal = true;
		$(target).find('ul.sortable').find('li').each(function (i, item) {
			if ($(item).data('key') == id) {
				retVal = false;
			}
		});
		return retVal;
	};

	self.dropFinishCallback = function (data) {
		var target = data.e.currentTarget;
		var textContent = data.o[0].children[1].childNodes[1].textContent;

		if (self.idIsNotInList(target, data.o[0].id)) {
			var sortable = $(target).find('ul.sortable');
			var newItem = $('<li></li>')
				.attr("data-space", $(data.o[0]).data("space"))
				.attr("data-desc", textContent)
				.attr("data-type", $(data.o[0]).data("type"))
				.attr("data-subtype", $(data.o[0]).data("subtype") || '')
				.attr('data-key', $(data.o[0]).data("proposition") || $(data.o[0]).data('key'));


			// check that all types in the categorization are the same
			if ($(sortable).data('drop-type') === 'multiple' && $(sortable).data("proptype") !== 'empty') {
				if ($(sortable).data("proptype") !== $(newItem).data("type")) {
					return;
				}
			} else {
				var tmptype = $(newItem).data("type");
				$(sortable).data("proptype", tmptype);
			}

			//this loop is executed only during replacement of a system element when droptype==single. In all other
			// cases(adding element to multiple droptype lists, adding a new element to an empty list) the else
			// statement is executed.
			if ($(sortable).data('drop-type') === 'single' && $(sortable).find('li').length > 0) {
				var $toRemove = $(sortable).find('li');
				var dialog = $('#replaceModal');
				$(dialog).find('#replaceContent').html('Are you sure you want to replace phenotype &quot;' + $toRemove.text().trim() + '&quot;?');
				$(dialog).find('#replaceButton').on('click', function (e) {
					self.deleteItem($toRemove, $(sortable), 0);
					self.addNewItemToList(data, $(sortable), newItem);
					$(dialog).modal('hide');
				});
				$(dialog).modal('show');
			}
			else {
				self.addNewItemToList(data, sortable, newItem);
			}
		}
	};

	self.deleteItem = function (toRemove, sortable, replace) {
		var infoLabel = sortable.siblings('div.label-info');
		var target = sortable.parent();
		self.removeDroppedElement(toRemove, sortable);
		self.setPropositionSelects(sortable.closest('[data-definition-container="true"]'));
		toRemove.remove();
		if (sortable.find('li').length == 0 && replace == 0) {
			sortable.data('proptype', 'empty');
			infoLabel.show();
		}

		// remove the properties from the drop down
		$('select[data-properties-provider=' + $(target).attr('id') + ']').each(function (i, item) {
			$(item).empty();
		});
		//remove the disabled attribute to the property textbox if any.
		var parentTable = $(target).parent().parent().parent();
		var inputProperty = $(parentTable).find('input.propertyValueField');
		$(inputProperty).removeAttr('disabled');
		var inputPropertycheckbox = $(parentTable).find('input.propertyValueConstraint');
		$(inputPropertycheckbox).removeAttr('disabled');

		// perform any additional delete actions
		if (self.deleteActions && self.deleteActions[self.propType]) {
			self.deleteActions[self.propType]();
		}
	};
        
	self.removeIn = function (obj, path) {
		var current = obj;
		for (var i = 0; i < path.length - 1; i++) {
			current = current[path[i]];
			if (current == null) {
				break;
			}
		}
		delete current[path[path.length - 1]];
	};
        
	self.removeDroppedElement = function (removed, removeTarget) {
		var elementKey = $(removed).data('key');
		var sourceId = $(removeTarget).data('count');
		var path = [self.propType, elementKey, 'sources', sourceId];
		self.removeIn(self.droppedElements, path);

		var allSourcesPath = [self.propType, elementKey, 'sources'];
		var allSources = self.getIn(self.droppedElements, allSourcesPath);
		var size = self.objSize(allSources);
		if (size <= 1) {
			for (var key in allSources) {
				if (allSources.hasOwnProperty(key)) {
					var source = allSources[key];
					var items = $(source).find('li');
					$(items).each(function (i, item) {
						var span = $(item).find('span.desc');
						var newText = $(removed).data('desc');
						$(span).text(newText);
					});
				}
			}
		}
	};

	self.setPropositionSelects = function (elem) {
		var droppedElems = self.droppedElements[self.propType];

		// this prevents an error in the inner loop which tries to
		// loop over this null object.
		if (droppedElems == null) {
			return;
		}

		var selects = $(elem).find('select[name="propositionSelect"]');
		$(selects).each(function (i, sel) {
			var $sortable = $(sel).closest('.drop-parent').find('ul.sortable');
			var originalSource = $(sel).data('sourceid');
			$(sel).attr('data-sourceid', '');
			$(sel).empty();
			$.each(droppedElems, function (elemKey, elemValue) {
				var sources = droppedElems[elemKey]['sources'];
				$.each(sources, function (sourceKey, sourceValue) {
					var $items = $(sourceValue).find('li');
					var selectedItem;
					$items.each(function (i, item) {
						if ($(item).data('key') == elemKey) {
							selectedItem = item;
						}
						if (selectedItem && $sortable.data('count') != $(sourceValue).data('count')) {
							var sourceId = $(sourceValue).data('count');
							var value = $(selectedItem).data('key') + '__' + sourceId;
							var desc = $(selectedItem).data('desc');
							if (self.objSize(sources) > 1) {
								desc += ' [' + sourceKey + ']';
							}
							var opt = $('<option></option>', {
								'value': value
							}).text(desc);
							if (value == $(selectedItem).data('key') + '__' + originalSource) {
								opt.attr('selected', 'selected');
							}
							$(sel).append(opt);
						}
					});
				});
			});
		});
	};

	self.addNewItemToList = function (data, sortable, newItem) {
		var target = $(sortable).parent();

		$(target).find('div.label-info').hide();

		var X = $("<span></span>", {
			'class': 'glyphicon glyphicon-remove delete-icon',
			'data-action': 'remove'
		});
		self.attachDeleteAction(X);

		newItem.append(X);
		newItem.append(data.o[0].children[1].childNodes[1].textContent);
		sortable.append(newItem);

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
		else {
			var parent = $(target).closest('.form-group').parent();
			var inputProperty = $(parent).find('input.propertyValueField');
			$(inputProperty).attr('disabled', 'disabled');
			var inputPropertycheckbox = $(parent).find('input.propertyValueConstraint');
			$(inputPropertycheckbox).attr('disabled', 'disabled');
		}

		// add the newly dropped element to the set of dropped elements
		self.addDroppedElement(newItem, sortable);
		self.setPropositionSelects($(sortable).closest('[data-definition-container="true"]'));

		// finally, call any actions specific to the type of proposition being entered/edited
		if (self.dndActions && self.dndActions[self.propType]) {
			self.dndActions[self.propType](data.o[0]);
		}
	};

	self.attachDeleteAction = function (elem) {
		$(elem).each(function (i, item) {
			$(item).click(function () {
				var $toRemove = $(item).closest('li');
				var $sortable = $toRemove.closest('ul.sortable');
				var dialog = $('#deleteModal');
				$(dialog).find('#deleteContent').html('Are you sure you want to remove phenotype &quot;' + $toRemove.text().trim() + '&quot;?');
				$(dialog).find('#deleteButton').on('click', function (e) {
					self.deleteItem($toRemove, $sortable, 0);
					$(dialog).modal('hide');
				});
				$(dialog).modal('show');
			});
		});
	};

	self.collectValueThresholds = function ($valueThresholds) {
		var valueThresholdsArr = new Array();

		$valueThresholds.each(function (i, r) {
			var dataEltFromDropBox = $(r).find('div.thresholdedPhenotype').find('li');
			var dataElt = self.collectPhenotype(dataEltFromDropBox);

			var $relatedPhenotypes = $(r).find('div.thresholdedRelatedPhenotypes').find('li');

			valueThresholdsArr.push({
				'phenotype': dataElt,
				'lowerComp': $(r).find('select[name="thresholdLowerComp"]').val(),
				'lowerValue': $(r).find('input[name="thresholdLowerVal"]').val(),
				'lowerUnits': null, //placeholder
				'upperComp': $(r).find('select[name="thresholdUpperComp"]').val(),
				'upperValue': $(r).find('input[name="thresholdUpperVal"]').val(),
				'upperUnits': null, //placeholder
				'relationOperator': $(r).find('select[name="thresholdPhenotypeTemporalRelation"]').val(),
				'relatedPhenotypes': self.collectPhenotypes($relatedPhenotypes),
				'withinAtLeast': $(r).find('input[name="thresholdWithinAtLeast"]').val(),
				'withinAtLeastUnit': $(r).find('select[name="thresholdWithinAtLeastUnits"]').val(),
				'withinAtMost': $(r).find('input[name="thresholdWithinAtMost"]').val(),
				'withinAtMostUnit': $(r).find('select[name="thresholdWithinAtMostUnits"]').val()
			});
		});
		return valueThresholdsArr;
	};

	self.collectSequence = function(elem) {
		var $sortable = $(elem).find('ul.sortable[data-type="main"]');
		var $mainProposition = $sortable.find('li').first();
		return {
			'id': $sortable.data('count'),
			'phenotypeKey': $mainProposition.data('key'),
			'hasDuration': $(elem).find('input[name="mainPhenotypeSpecifyDuration"]').is(':checked'),
			'minDuration': $(elem).find('input[name="mainPhenotypeMinDurationValue"]').val(),
			'minDurationUnits': $(elem).find('select[name="mainPhenotypeMinDurationUnits"]').val(),
			'maxDuration': $(elem).find('input[name="mainPhenotypeMaxDurationValue"]').val(),
			'maxDurationUnits': $(elem).find('select[name="mainPhenotypeMaxDurationUnits"]').val(),
			'hasPropertyConstraint': $(elem).find('input[name="mainPhenotypeSpecifyProperty"]').is(':checked'),
			'property': $(elem).find('select[name="mainPhenotypePropertyName"]').val(),
			'propertyValue': $(elem).find('input[name="mainPhenotypePropertyValue"]').val()
		}
	};

	self.collectSequenceRelations = function($relationElems) {
		var relations = new Array();

		$relationElems.each(function (i,r) {
			var $sortable = $(r).find('ul.sortable');
			var $proposition = $sortable.find('li').first();
			var id = $sortable.data('count');
			var key = $proposition.data('key');
			var sequentialValue = $(r).find('select[name="propositionSelect"]').val();
			var sequentialData = sequentialValue.split('__');
			var sequentialPhenotype = sequentialData[0];
			var sequentialPhenotypeSource = sequentialData[1];

			if (key) {
				relations.push({
					'phenotypeField': {
						'id': id,
						'phenotypeKey': $proposition.data('key'),
						'hasDuration': $(r).find('input[name="sequenceRelPhenotypeSpecifyDuration"]').is(':checked'),
						'minDuration': $(r).find('input[name="sequenceRelPhenotypeMinDurationValue"]').val(),
						'minDurationUnits':  $(r).find('select[name="sequenceRelPhenotypeMinDurationUnits"]').val(),
						'maxDuration': $(r).find('input[name="sequenceRelPhenotypeMaxDurationValue"]').val(),
						'maxDurationUnits':  $(r).find('select[name="sequenceRelPhenotypeMaxDurationUnits"]').val(),
						'hasPropertyConstraint': $(r).find('input[name="sequenceRelPhenotypeSpecifyProperty"]').is(':checked'),
						'property': $(r).find('select[name="sequenceRelPhenotypePropertyName"]').val(),
						'propertyValue': $(r).find('input[name="sequenceRelPhenotypePropertyValue"]').val()
					},
					'relationOperator': $(r).find('select[name="sequenceRelPhenotypeTemporalRelation"]').val(),
					'sequentialPhenotype': sequentialPhenotype,
					'sequentialPhenotypeSource': sequentialPhenotypeSource,
					'relationMinCount': $(r).find('input[name="sequenceRhsPhenotypeMinDistanceValue"]').val(),
					'relationMinUnits': $(r).find('select[name="sequenceRhsPhenotypeMinDistanceUnits"]').val(),
					'relationMaxCount': $(r).find('input[name="sequenceRhsPhenotypeMaxDistanceValue"]').val(),
					'relationMaxUnits': $(r).find('select[name="sequenceRhsPhenotypeMaxDistanceUnits"]').val()
				});
			}
		});
		return relations;
	};

	self.saveValueThreshold = function (elem) {
		var valueThreshold = new Object();

		var $valueThresholds = $(elem).find('.value-thresholds-container').find('.value-threshold');

		if (self.propId) {
			valueThreshold.id = self.propId;
		}
		valueThreshold.type = 'VALUE_THRESHOLD';
		valueThreshold.displayName = $('input#propDisplayName').val();
		valueThreshold.description = $('textarea#propDescription').val();
		valueThreshold.name = $(elem).find('input[name="valueThresholdValueName"]').val();
		valueThreshold.thresholdsOperator = $(elem).find('select[name="valueThresholdType"]').val();
		valueThreshold.valueThresholds = self.collectValueThresholds($valueThresholds);

		self.post(valueThreshold);
	};

	self.post = function (postData) {
		$.ajax({
			type: "POST",
			url: 'saveprop',
			processData: false,
            contentType: 'application/json; charset=UTF-8',
			data: JSON.stringify(postData),
			success: function (data) {
				window.location.href = 'editorhome'
			},
			error: function (data, statusCode, errorThrown) {
				var content = 'Error while saving ' + postData.displayName + '. ' + window.eureka.util.sanitizeResponseText(data.responseText) + '. Status Code: ' + statusCode;
				$('#errorModal').find('#errorContent').html(content);
				$('#errorModal').modal('show');
				if (errorThrown != null) {
					console.log(errorThrown);
				}
			}
		});
	}

	self.saveCategorization = function (elem) {
		var $memberPhenotypes = $(elem).find('ul.sortable').find('li');
		var childElements = self.collectPhenotypes($memberPhenotypes);

		var categoricalType = self.propSubType;

		var categorization = {
			'type': 'CATEGORIZATION',
			'displayName': $('input#propDisplayName').val(),
			'description': $('textarea#propDescription').val(),
			'categoricalType': categoricalType,
			'children': childElements
		}

		if (self.propId) {
			categorization.id = self.propId;
		}
		self.post(categorization);
	};

	self.collectPhenotypes = function ($phenotypesFromDropBox) {
		var childElements = new Array();

		$phenotypesFromDropBox.each(function (i, p) {
			childElements.push(self.collectPhenotype(p));
		});

		return childElements;
	};

	self.collectPhenotype = function (phenotypeFromDropBox) {
		var system = $(phenotypeFromDropBox).data('space') === 'system'
			|| $(phenotypeFromDropBox).data('space') === 'SYSTEM';
		var child = {
			'phenotypeKey': $(phenotypeFromDropBox).data('key')
		};

		if (system) {
			child['type'] = 'SYSTEM';
		} else {
			child['type'] = $(phenotypeFromDropBox).data('type');
		}
		return child;
	};
        
	self.saveFrequency = function (elem) {
		var $phenotype = $(elem).find('ul[data-type="main"]').find('li').first();
		var frequency = {
			'type': 'FREQUENCY',
			'frequencyType': $('select[name="freqTypes"]').val(),
			'displayName': $('input#propDisplayName').val(),
			'description': $('textarea#propDescription').val(),
			'atLeast': $(elem).find('input[name=freqAtLeastField]').val(),
			'isConsecutive': $(elem).find('input[name=freqIsConsecutive]').is(':checked'),
			'phenotype': {
				'phenotypeKey': $phenotype.data('key'),
				'hasDuration': $(elem).find('input[name="freqPhenotypeSpecifyDuration"]').is(':checked'),
				'minDuration': $(elem).find('input[name="freqPhenotypeMinDurationValue"]').val(),
				'minDurationUnits': $(elem).find('select[name="freqPhenotypeMinDurationUnits"]').val(),
				'maxDuration': $(elem).find('input[name="freqPhenotypeMaxDurationValue"]').val(),
				'maxDurationUnits': $(elem).find('select[name="freqPhenotypeMaxDurationUnits"]').val(),
				'hasPropertyConstraint': $(elem).find('input[name="freqPhenotypeSpecifyProperty"]').is(':checked'),
				'property': $(elem).find('select[name="freqPhenotypePropertyName"]').val(),
				'propertyValue': $(elem).find('input[name="freqPhenotypePropertyValue"]').val()
			},
			'isWithin': $(elem).find('input[name=freqIsWithin]').is(':checked'),
			'withinAtLeast': $(elem).find('input[name=freqWithinAtLeast]').val(),
			'withinAtLeastUnits': $(elem).find('select[name=freqWithinAtLeastUnits]').val(),
			'withinAtMost': $(elem).find('input[name=freqWithinAtMost]').val(),
			'withinAtMostUnits': $(elem).find('select[name=freqWithinAtMostUnits]').val()
		}

		if (self.propId) {
			frequency.id = self.propId;
		}

		self.post(frequency);
	};

	self.saveSequence = function (elem) {
		var sequence = new Object();
		var $relationElems = $(elem).find('.sequence-relations-container').find('.sequence-relation');

		if (self.propId) {
			sequence.id = self.propId;
		}
		sequence.type = 'SEQUENCE';
		sequence.displayName = $('input#propDisplayName').val();
		sequence.description = $('textarea#propDescription').val();
		sequence.primaryPhenotype = self.collectSequence(elem);
		sequence.relatedPhenotypes = self.collectSequenceRelations($relationElems);

		self.post(sequence);
	};

	self.saveFunctions = {
		'SEQUENCE': self.saveSequence,
		'CATEGORIZATION': self.saveCategorization,
		'FREQUENCY': self.saveFrequency,
		'VALUE_THRESHOLD': self.saveValueThreshold
	};

	self.validateCategorization = function (elem) {
		var name = $('input#propDisplayName').val()
		return name != null && name.length > 0;
	}

	self.validateSequence = function (elem) {
		var name = $('input#propDisplayName').val()
		return name != null && name.length > 0;
	}

	self.validateFrequency = function (elem) {
		var name = $('input#propDisplayName').val()
		return name != null && name.length > 0;
	}

	self.validateValueThreshold = function (elem) {
		var name = $('input#propDisplayName').val()
		return name != null && name.length > 0;
	}

	self.validationFunctions = {
		'SEQUENCE': self.validateSequence,
		'CATEGORIZATION': self.validateCategorization,
		'FREQUENCY': self.validateFrequency,
		'VALUE_THRESHOLD': self.validateValueThreshold
	};

	self.save = function (containerElem) {
		var validationFunction = self.validationFunctions[self.propType];
		if (validationFunction(containerElem)) {
			self.saveFunctions[self.propType](containerElem);
		} else {
			var content = 'Please ensure that the element display name is filled out.';
			$('#errorModal').find('#errorContent').html(content);
			$('#errorModal').modal('show');
		}
	};

	self.duplicateSequenceRelation = function (duplicateElement, newCount) {
		var data = $(duplicateElement).clone();
		var sortable = data.find('ul.sortable');
		var inputPropertycheckbox = data.find('input.propertyValueConstraint');
		$(inputPropertycheckbox).removeAttr('disabled');
		var inputProperty = data.find('input.propertyValueField');
		$(inputProperty).removeAttr('disabled');
		sortable.empty();
		sortable.attr('data-count', newCount + 1);
		data.find('span.count').text(newCount);
		data.find('div.sequencedPhenotype').attr('id', 'relatedPhenotype' + newCount);
		data.find('select[name="sequenceRelPhenotypePropertyName"]').attr('data-properties-provider', 'relatedPhenotype' + newCount);
//		eureka.trees.setPropositionSelects($(appendTo).closest('[data-definition-container="true"]'));
		return data;
	}

	self.duplicateThreshold = function (duplicateElement, newCount) {
		var data = $(duplicateElement).clone();
		data.find('span.count').text(newCount);
		data.find('ul.sortable').empty();
		data.find('div.jstree-drop').attr('id', 'threshold' + newCount);
		return data;
	};

	self.setupDuplication = function (duplicateButton, duplicateElementContainer, duplicateElement, duplicateFunc, optFunc) {
		$(duplicateButton).on('click', function () {
			var $elems = $(duplicateElementContainer).find(duplicateElement);
			var newCount = $elems.length + 1;
			var toDuplicate = $elems.filter(':last');
			duplicated = duplicateFunc(toDuplicate, newCount);
			$(duplicateElementContainer).append(duplicated);

			if (optFunc) {
				optFunc($(duplicateElementContainer));
			}
		});
	};
};
