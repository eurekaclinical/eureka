/* globals self */
(function(){
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.listDragAndDropService
     * @description
     * This is the list drag and drop service.
     */

    angular
        .module('eureka')
        .factory('listDragAndDropService', listDragAndDropService);

    listDragAndDropService.$inject = [];

    function listDragAndDropService() {

        return ({
            getIn: getIn,
            setIn: setIn,
            removeIn: removeIn,
            objSize: objSize,
            addDroppedElement: addDroppedElement,
            deleteElement: deleteElement,
            deleteItem: deleteItem,
            removeDroppedElement: removeDroppedElement,
            addNewItemToList: addNewItemToList,
            idIsNotInList: idIsNotInList,
            setPropositionSelects: setPropositionSelects,
            attachDeleteAction: attachDeleteAction

        });

        function getIn(obj, path) {
            var current = obj;
            for (var i = 0; i < path.length; i++) {
                current = current[path[i]];
                if (!current) {
                    break;
                }
            }
            return current;
        }

        function setIn(obj, path, value) {
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
        }

        function removeIn(obj, path) {
            var current = obj;
            for (var i = 0; i < path.length - 1; i++) {
                current = current[path[i]];
                if (current === null) {
                    break;
                }
            }
            delete current[path[path.length - 1]];
        }

        function objSize(obj) {
            var size = 0;
            for (var key in obj) {
                if (obj.hasOwnProperty(key)) {
                    size++;
                }
            }
            return size;
        }

        function addDroppedElement(dropped, dropTarget) {
            var elementKey = $(dropped).data('key');
            var sourceId = $(dropTarget).data('count');
            var sourcePath = [self.propType, elementKey, 'sources', sourceId];
            var defPath = [self.propType, elementKey, 'definition'];
            var definition = getIn(self.droppedElements, defPath);

            setIn(self.droppedElements, sourcePath, dropTarget);
            if (!definition) {
                var properties = ['key', 'desc', 'type', 'subtype', 'space'];
                definition = {};
                $.each(properties, function (i, property) {
                    definition[property] = $(dropped).data(property);
                });
                setIn(self.droppedElements, defPath, definition);
            }

            var allSourcesPath = [self.propType, elementKey, 'sources'];
            var allSources = getIn(self.droppedElements, allSourcesPath);
            var size = objSize(allSources);
            if (size > 1) {
                for (var key in allSources) {
                    if (allSources.hasOwnProperty(key)) {
                        var source = allSources[key];
                        var items = $(source).find('li');
                        $(items).each(function(i, item) {
                            var span = $(item).find('span.desc');
                            var newText = $(dropped).data('desc') + ' [' + $(source).data('count') + ']';
                            $(span).text(newText);
                        });
                    }
                }
            }
        }

        function deleteElement(displayName, key) {
            var $dialog = $('<div></div>')
                .html('Are you sure you want to delete cohort &quot;' +
                displayName.trim() + '&quot;? You cannot undo this action.')
                .dialog({
                    title: 'Delete Data Element',
                    modal: true,
                    resizable: false,
                    buttons: {
                        'Delete': function () {
                            $.ajax({
                                type: 'POST',
                                url: 'deletecohort?key=' + encodeURIComponent(key),
                                success: function (data) {
                                    $(this).dialog('close');
                                    window.location.href = 'editorhome';
                                },
                                error: function (data, statusCode) {
                                    var $errorDialog = $('<div></div>')
                                        .html(data.responseText)
                                        .dialog({
                                            title: 'Error Deleting Data Element',
                                            buttons: {
                                                'OK': function () {
                                                    $(this).dialog('close');
                                                }
                                            },
                                            close: function () {
                                                $dialog.dialog('close');
                                            }
                                        });
                                    $errorDialog.dialog('open');
                                }
                            });
                        },
                        'Cancel': function () {
                            $(this).dialog('close');
                        }
                    },
                    close: function () {
                        // do nothing here.
                    }
                });
            $dialog.dialog('open');
        }


        function removeDroppedElement(removed, removeTarget) {
            var elementKey = $(removed).data('key');
            var sourceId = $(removeTarget).data('count');
            var path = [self.propType, elementKey, 'sources', sourceId];
            removeIn(self.droppedElements, path);

            var allSourcesPath = [self.propType, elementKey, 'sources'];
            var allSources = getIn(self.droppedElements, allSourcesPath);
            var size = objSize(allSources);
            if (size <= 1) {
                for (var key in allSources) {
                    if (allSources.hasOwnProperty(key)) {
                        var source = allSources[key];
                        var items = $(source).find('li');
                        $(items).each(function(i, item) {
                            var span = $(item).find('span.desc');
                            var newText = $(removed).data('desc');
                            $(span).text(newText);
                        });
                    }
                }
            }
        }

        function deleteItem(toRemove, sortable, replace) {
            var infoLabel = sortable.siblings('div.label-info');
            var target = sortable.parent();
            removeDroppedElement(toRemove, sortable);
            setPropositionSelects(sortable.closest('[data-definition-container="true"]'));
            toRemove.remove();
            if (sortable.find('li').length === 0 && replace === 0) {
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
        }

        function idIsNotInList(target, id) {
            var retVal = true;
            $(target).find('ul.sortable').find('li').each(function (i, item) {
                if ($(item).data('key') === id) {
                    retVal = false;
                }
            });
            return retVal;
        }


        function addNewItemToList(data, sortable, newItem) {
            var target = $(sortable).parent();

            $(target).find('div.label-info').hide();

            var X = $('<span></span>', {
                'class': 'glyphicon glyphicon-remove delete-icon',
                'data-action': 'remove'
            });

            attachDeleteAction(X);
            var textContent = data.data.origin.get_node(data.data.obj[0].id).original.text;

            newItem.append(X);
            newItem.append(textContent);


            sortable.append(newItem);

            // set the properties in the properties select
            if ($(data.data.obj[0]).data('properties')) {
                var properties = $(data.data.obj[0]).data('properties').split(',');
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
            addDroppedElement(newItem, sortable);
            setPropositionSelects($(sortable).closest('[data-definition-container="true"]'));

            // finally, call any actions specific to the type of proposition being entered/edited
            if (self.dndActions && self.dndActions[self.propType]) {
                self.dndActions[self.propType](data.data.obj[0]);
            }
        }

        function setPropositionSelects(elem) {
            var droppedElems = self.droppedElements[self.propType];

            // this prevents an error in the inner loop which tries to
            // loop over this null object.
            if (droppedElems === null) {
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
                            if ($(item).data('key') === elemKey) {
                                selectedItem = item;
                            }
                            if (selectedItem && $sortable.data('count') !== $(sourceValue).data('count')) {
                                var sourceId = $(sourceValue).data('count');
                                var value = $(selectedItem).data('key') + '__' + sourceId;
                                var desc = $(selectedItem).data('desc');
                                if (self.objSize(sources) > 1) {
                                    desc += ' [' + sourceKey + ']';
                                }
                                var opt = $('<option></option>', {
                                    'value': value
                                }).text(desc);
                                if (value === $(selectedItem).data('key') + '__' + originalSource) {
                                    opt.attr('selected', 'selected');
                                }
                                $(sel).append(opt);
                            }
                        });
                    });
                });
            });
        }

        function attachDeleteAction(elem) {
            $(elem).each(function (i, item) {
                $(item).click(function () {
                    var $toRemove = $(item).closest('li');
                    var $sortable = $toRemove.closest('ul.sortable');
                    var dialog = $('#deleteModal');
                    $(dialog).find('#deleteContent').html('Are you sure you want to remove data element &quot;' +
                        $toRemove.text().trim() + '&quot;?');
                    $(dialog).find('#deleteButton').on('click', function (e) {
                        deleteItem($toRemove, $sortable, 0);
                        $(dialog).modal('hide');
                    });
                    $(dialog).modal('show');
                });
            });
        }

        self.dndActions = {
        };
        self.deleteActions = {
        };

    }

}());