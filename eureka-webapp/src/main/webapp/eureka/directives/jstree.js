(function(){
    'use strict';

    angular
        .module('eureka')
        .directive('jstree', jstree);

    jstree.$inject = ['listDragAndDropService'];

    function jstree(listDragAndDropService) {

        return {
            scope: {
                data: '='
            },
            template: '<div id="filter">Tree did not load.</div>',
            restrict: 'E',
            controller: function ($scope, $element, $attrs,$rootScope) {

            },
            link: function (scope, element, attrs) {
                self.droppedElements = {};
                self.propId = null;
                self.propType = null;
                self.propSubType = null;
                var initData = null;
                var searchUpdateDivElem = attrs.searchUpdateDiv;
                var searchValidationModalElem =  attrs.searchValidationModal;
                var searchNoResultsModalElem = attrs.searchNoResultsModal;
                var treeCssUrl = attrs.treeCssUrl;

                $(element).jstree({
                    core: {
                        data: {
                            url: attrs.treeUrl,
                            dataType: 'json',
                            data: function (n) {
                                return {
                                    key: n.id === '#' ? 'root' : n.id
                                };
                            },

                        },

                    },


                    plugins: [ 'themes', 'json_data', 'ui', 'crrm', 'dnd', 'search' ]
                });

                $(element).before(
                    $('<form id="search">' +
                        '<span></span>' +
                        '<div class="input-group"><input id="searchText" class="form-control" type="text" />' +
                        '<div class="input-group-btn">' +
                        '<input id="searchTree" class="btn btn-default" type=submit value="search" />' +
                        '<input class="btn btn-default" type="reset" value="X" /></div></div>' +
                        '</form>').
                        bind({
                            reset: function(evt){
                                $(element).jstree('clear_search');
                                $(element).jstree(true).settings.core.data = initData;
                                $(element).jstree(true).refresh();
                                $('#search span').html('');
                            },

                            submit: function(evt){
                                $(element).jstree('clear_search');
                                var searchvalue = $('#search input[type="text"]').val();
                                initData = $(element).jstree(true).settings.core.data;
                                if(searchvalue!='' && searchvalue.length>=4) {
                                    $(element).hide();
                                    var $elem = $(searchUpdateDivElem);
                                    $elem.text('Search is in progress. Please wait...');
                                    $elem.show();


                                    $(element).jstree('destroy');
                                    $(element).jstree({
                                        'core' : {
                                            'data': {
                                                'url': function (node) {
                                                    return node.id === '#' ?
                                                    attrs.treeSearch + '?str='+searchvalue :
                                                        attrs.treeUrl;

                                                },
                                                'dataType': 'json',
                                                'data': function (node) {
                                                    return {
                                                        key: node.id === '#' ? 'root' : node.id

                                                    };
                                                }
                                            }
                                        },
                                        'themes': {
                                            'name': 'default',
                                            'theme': 'default',
                                            'url': treeCssUrl
                                        },
                                        'plugins': [ 'themes', 'json_data', 'ui', 'crrm', 'dnd', 'search' ]
                                    }).bind('loaded.jstree', function (e, data) {

                                        if (data.instance._cnt == 0) {
                                            console.log('empty');
                                            var $elem = $(searchNoResultsModalElem);
                                            $elem.find('#searchContent').html('There are no entries in our database that matched your search criteria.');
                                            $elem.modal('toggle');

                                            $elem.hide();
                                            $(element).jstree('clear_search');
                                            $(element).jstree(true).settings.core.data = initData;
                                            $(element).jstree(true).refresh();
                                            $('#searchText').val('');

                                            $(element).show();

                                            $elem = $(searchUpdateDivElem);
                                            $elem.hide();

                                        }
                                        else if (data.instance._cnt > 200) {
                                            var $elem = $(searchModalElem);
                                            $elem.find('#searchContent').html('The number of search results exceeded the  '+
                                                ' maximum limit and all results might not be displayed.'+
                                                ' Please give a more specific search query to see all results.');
                                            $elem.modal('toggle');

                                            $elem.hide();
                                            $(element).jstree('clear_search');
                                            $(element).jstree(true).settings.core.data = initData;
                                            $(element).jstree(true).refresh();
                                            $('#searchText').val('');

                                            $(element).show();

                                            $elem = $(searchUpdateDivElem);
                                            $elem.hide();

                                        }

                                    });


                                    $elem.hide();
                                    $(element).show();


                                } else if(searchvalue.length<4){
                                    var $elem = $(searchValidationModalElem);
                                    $elem.find('#searchContent').html('Please enter a search value with length greater than 3.');
                                    $elem.modal('toggle');
                                    $(element).show();

                                }
                                return false;
                            }


                        })
                );

                $(document).on('dnd_move.vakata', function (e, data) {
                    var t = $(data.event.target);
                    if(!t.closest('.jstree').length) {
                        if (t.closest('.tree-drop').length) {
                            data.helper.find('.jstree-icon').removeClass('jstree-er').addClass('jstree-ok');
                        }
                        else {
                            data.helper.find('.jstree-icon').removeClass('jstree-ok').addClass('jstree-er');
                        }
                    }

                });

                $(document).on('dnd_stop.vakata', function (e, data) {
                    self.dropFinishCallback(data);


                });

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



                self.dropFinishCallback = function (data) {
                    var target = data.event.target;
                    // SBA
                    target = $(target).closest('#patCohortDefinition');
                    var textContent = data.data.origin.get_node(data.data.obj[0].id).original.text;

                    if (listDragAndDropService.idIsNotInList(target, data.data.obj[0].id)) {
                        var sortable = $(target).find('ul.sortable');
                        var elementKey = data.data.obj[0].id;
                        var newItem = $('<li></li>')
                            .attr('data-space', data.data.origin.get_node(elementKey).original.attr['data-space'])
                            .attr('data-desc', textContent)
                            .attr('data-type', data.data.origin.get_node(elementKey).original.attr['data-type'])
                            .attr('data-subtype',data.data.origin.get_node(elementKey).original.attr['data-subtype'] || '')
                            .attr('data-key', data.data.origin.get_node(elementKey).original.attr['data-proposition'] ||
                            data.data.origin.get_node(elementKey).original.attr['data-key']);


                        // check that all types in the categorization are the same
                        // SBA look here
                        if ($(sortable).data('drop-type') === 'multiple' && $(sortable).data('proptype') !== 'empty') {
                            if ($(sortable).data('proptype') !== $(newItem).data('type')) {
                                return;
                            }
                        } else {
                            var tmptype = $(newItem).data('type');
                            $(sortable).data('proptype', tmptype);
                        }

                        //this loop is executed only during replacement of a system element when droptype==single. In all other
                        // cases(adding element to multiple droptype lists, adding a new element to an empty list) the else
                        // statement is executed.
                        if ($(sortable).data('drop-type') === 'single' && $(sortable).find('li').length > 0) {
                            var $toRemove = $(sortable).find('li');
                            var dialog = $('#replaceModal');
                            $(dialog).find('#replaceContent').html('Are you sure you want to replace data element &quot;' + $toRemove.text().trim() + '&quot;?');
                            $(dialog).find('#replaceButton').on('click', function (e) {
                                listDragAndDropService.deleteItem($toRemove, $(sortable), 0);
                                listDragAndDropService.addNewItemToList(data, $(sortable), newItem);
                                $(dialog).modal('hide');
                            });
                            $(dialog).modal('show');
                        }
                        else {
                            listDragAndDropService.addNewItemToList(data, sortable, newItem);
                        }
                    }
                };

            }
        };
    }

}());