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
				title: "Delete Phenotype",
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
										title: "Error Deleting Phenotype",
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
				$(dialog).find('#deleteContent').html('Are you sure you want to remove phenotype &quot;' + $toRemove.text().trim() + '&quot;?');
				$(dialog).find('#deleteButton').on('click', function (e) {
					self.deleteItem($toRemove, $sortable, 0);
					$(dialog).modal('hide');
				});
				$(dialog).modal('show');
			});
		});
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

        self.post = function (postData) {
                var user = null;
                $.ajax({
                    url: '../proxy-resource/users/me',
                    dataType: 'json',
                    success: function(data) {
                        user = data;
                        if (user != null) {
                            var cohortDestination = {};
                            var type = "";
                            if (postData.id != null) {
                                    cohortDestination.id = postData.id;
                                    type = "PUT";
                            }
                            else {
                                    cohortDestination.id = null;
                                    type = "POST";
                            }

                            cohortDestination.type = 'COHORT';
                            cohortDestination.ownerUserId = user.id;
                            cohortDestination.name = postData['name'];
                            cohortDestination.description = postData['description'];
                            cohortDestination.phenotypeFields = null;
                            cohortDestination.cohort = self.toCohort(postData.phenotypes);

                            cohortDestination.read = false;
                            cohortDestination.write = false;
                            cohortDestination.execute = false;

                            cohortDestination.created_at = null;
                            cohortDestination.updated_at = null;

                            cohortDestination.links = null;
                            console.log(JSON.stringify(cohortDestination));
                            $.ajax({
                                type: type,
								processData: false,
								contentType: 'application/json; charset=UTF-8',
                                url: '../proxy-resource/destinations',
                                data: JSON.stringify(cohortDestination),
                                success: function (postData) {
                                    window.location.href = 'cohorthome'
                                },
                                error: function (postData, statusCode, errorThrown) {
                                    var content = 'Error while saving ' + postData.displayName + '. ' + window.eureka.util.sanitizeResponseText(postData.responseText) + '. Status Code: ' + postData.status;
                                    $('#errorModal').find('#errorContent').html(eureka.util.getUserMessage(postData.status,content));
                                    $('#errorModal').modal('show');
                                    if (errorThrown != null) {
                                        console.log(errorThrown);
                                    }
                                }
                            });
                        }                        
                    },
                    error: function (data, statusCode, errorThrown) {
                        var content = 'Error while saving ' + postData.displayName + '. ' + data.responseText + '. Status Code: ' + data.status;
                        $('#errorModal').find('#errorContent').html(eureka.util.getUserMessage(data.status,content));
                        $('#errorModal').modal('show');
                        if (errorThrown != null) {
                            console.log(errorThrown);
                        }
                    }
                });
        }
    
        self.toCohort = function (phenotypes) {
            var cohort = {id: null};
            var node = {id: null, start: null, finish: null, type: 'Literal'};
            if (phenotypes.length == 1) {
                node.name = phenotypes[0].phenotypeKey;
            } else if (phenotypes.length > 1) {
                first = true;
                prev = null;
                for (var i = phenotypes.length - 1; i >= 0; i--) {
                    var literal = {id: null, start: null, finish: null, type: 'Literal'};
                    literal.name = phenotypes[i].phenotypeKey;
                    if (first) {
                        first = false;
                        prev = literal;
                    } else {
                        var binaryOperator = {id: null, type: 'BinaryOperator', op: 'OR'};
                        binaryOperator.left_node = literal;
                        binaryOperator.right_node = prev;
                        prev = binaryOperator;
                    }
                }
                node = prev;
            } else {
                node = null;
            }
            cohort.node = node;
            return cohort;

        }

        self.saveCohort = function (elem) {
            var $memberPhenotypes = $(elem).find('ul.sortable').find('li');
            var childElements = self.collectPhenotypes($memberPhenotypes);

            var cohort = {
                'name': $('input#patCohortDefName').val(),
                'description': $('textarea#patCohortDescription').val(),
                'phenotypes': childElements
            }

            if (self.propId) {
                cohort.id = self.propId;
            }

            self.post(cohort);
        };

        self.validateCohort = function (elem) {
            var name = $('input#patCohortDefName').val();
            var membersCount = $(elem).find('ul.sortable').find('li').length;
            if(name == null || name.length == 0){
                var content = 'Please ensure that the cohort name is filled out.';
                $('#errorModal').find('#errorContent').html(content);
                $('#errorModal').modal('show');
            }else if(membersCount==0){
                var content = 'Please ensure that the cohort members are filled out.';
                $('#errorModal').find('#errorContent').html(content);
                $('#errorModal').modal('show');
            }else return true;
        }
        self.save = function (containerElem) {
            if (self.validateCohort(containerElem)) {
                self.saveCohort(containerElem);
            }
        };

};
