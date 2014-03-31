// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.editor = new function () {

	var self = this;

	self.propId = null;
	self.propType = null;
	self.propSubType = null;
	self.droppedElements = {};

	self.setup = function (propType, propSubType, propId, systemTreeElem, userTreeElem, containerElem, saveButtonElem, deleteButtonElem, listElem, treeCssUrl, searchModalElem) {
		self.propId = propId;
		self.propType = propType;
		self.propSubType = (propSubType == '') ? null : propSubType;

		if (propId != null && propId != '') {
			$('.label-info').hide();
		}


		$(saveButtonElem).on('click', function () {
			self.save($(containerElem));
		});

		self.attachDeleteAction(deleteButtonElem);

		$(listElem).each(function (i, list) {
			$(list).find('li').each(function (j, item) {
				self.addDroppedElement(item, $(list));
			});
		});

		self.setPropositionSelects($(containerElem));
		eureka.tree.setupUserTree(userTreeElem, self.dropFinishCallback);
		eureka.tree.setupSystemTree(systemTreeElem, treeCssUrl, searchModalElem, self.dropFinishCallback);
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

	self.enableFrequencyFields = function (dropped) {
		if ($(dropped).data('type') == 'VALUE_THRESHOLD') {
			$('#valueThresholdConsecutiveLabel').css('display', 'inline');
		}
	};

	self.disableFrequencyFields = function () {
		$('#valueThresholdConsecutiveLabel').css('display', 'none');
	};

	self.dndActions = {
		'FREQUENCY': self.enableFrequencyFields
	};
	self.deleteActions = {
		'FREQUENCY': self.disableFrequencyFields
	};

	self.deleteElement = function (displayName, key) {
		var $dialog = $('<div></div>')
			.html('Are you sure you want to delete data element ' +
				displayName + '? You cannot undo this action.')
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
									.html(data.responseText)
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
				$(dialog).find('#replaceContent').html('Are you sure you want to replace data element ' + $toRemove.text() + '?');
				$(dialog).find('#confirmButton').on('click', function (e) {
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

	self.attachDeleteAction = function (elem) {
		$(elem).each(function (i, item) {
			$(item).click(function () {
				var $toRemove = $(item).closest('li');
				var $sortable = $toRemove.closest('ul.sortable');
				var dialog = $('#deleteModal');
				$(dialog).find('#deleteContent').html('Are you sure you want to remove data element ' + $toRemove.text() + '?');
				$(dialog).find('#confirmButton').on('click', function (e) {
					self.deleteItem($toRemove, $sortable, 0);
					$(dialog).modal('hide');
				});
				$(dialog).modal('show');
			});
		});
	};

	self.collectDataElements = function ($dataElementsFromDropBox) {
		var childElements = new Array();

		$dataElementsFromDropBox.each(function (i, p) {
			childElements.push(self.collectDataElement(p));
		});

		return childElements;
	};

	self.post = function (postData) {
		$.ajax({
			type: "POST",
			url: 'saveprop',
			data: JSON.stringify(postData),
			success: function (data) {
				window.location.href = 'editorhome'
			},
			error: function (data, statusCode, errorThrown) {
				var content = 'Error while saving ' + postData.displayName + '. ' + data.responseText + '. Status Code: ' + statusCode;
				$('#errorModal').find('#errorContent').html(content);
				$('#errorModal').modal('show');
				if (errorThrown != null) {
					console.log(errorThrown);
				}
			}
		});
	}

	self.collectDataElement = function (dataElementFromDropBox) {
		var system = $(dataElementFromDropBox).data('space') === 'system'
			|| $(dataElementFromDropBox).data('space') === 'SYSTEM';
		var child = {
			'dataElementKey': $(dataElementFromDropBox).data('key')
		};

		if (system) {
			child['type'] = 'SYSTEM';
		} else {
			child['type'] = $(dataElementFromDropBox).data('type');
		}
		return child;
	};

	self.collectValueThresholds = function ($valueThresholds) {
		var valueThresholdsArr = new Array();

		$valueThresholds.each(function (i, r) {
			var dataEltFromDropBox = $(r).find('div.thresholdedDataElement').find('li');
			var dataElt = self.collectDataElement(dataEltFromDropBox);

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
				'relatedDataElements': self.collectDataElements($relatedDataElements),
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

	self.saveCategorization = function (elem) {
		var $memberDataElements = $(elem).find('ul.sortable').find('li');
		var childElements = self.collectDataElements($memberDataElements);

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

	self.saveFrequency = function (elem) {
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

		if (self.propId) {
			frequency.id = self.propId;
		}

		self.post(frequency);
	};

	self.saveSequence = function (elem) {
		var sequence = new Object();
		var $relationElems = $(elem).find('.sequence-relations-container').find('.sequence-relation');
		var propId = $('#propId').val();

		if (propId) {
			sequence.id = propId;
		}
		sequence.type = 'SEQUENCE';
		sequence.displayName = $('input#propDisplayName').val();
		sequence.description = $('textarea#propDescription').val();
		sequence.primaryDataElement = self.collectSequence(elem);
		sequence.relatedDataElements = self.collectSequenceRelations($relationElems);

		self.post(sequence);
	};

	self.saveFunctions = {
		'SEQUENCE': self.saveSequence,
		'CATEGORIZATION': self.saveCategorization,
		'FREQUENCY': self.saveFrequency,
		'VALUE_THRESHOLD': self.saveValueThreshold
	};

	self.save = function (containerElem) {
		self.saveFunctions[self.propType](containerElem);
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
		data.find('div.sequencedDataElement').attr('id', 'relatedDataElement' + newCount);
		data.find('select[name="sequenceRelDataElementPropertyName"]').attr('data-properties-provider', 'relatedDataElement' + newCount);
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
