(function(){
    'use strict';

    angular
        .module('phenotypes')
        .directive('phenotypeEditor', phenotypeEditor);

    phenotypeEditor.$inject = ['$http', '$templateCache', '$timeout', 'listDragAndDropService'];

    function phenotypeEditor(http, templateCache, timer, listDragAndDropService) {

        return {
            scope: {
                item: '=phenotypeEditor'
            },
            replace: true,
            restrict: 'EA',
            templateUrl: 'eureka/phenotypes/directives/phenotype-editor/phenotype-editor.html',
            link: function (scope, element, attrs, ctrl, transclude) {
                scope.$watch('$last', function (v) {
                    var editorAction = function () {

                        $('ul.sortable').each(function (i, list) {
                            $(list).find('li').each(function (j, item) {
                                listDragAndDropService.addDroppedElement(item, $(list));
                            });

                            $('span.delete-icon').each(function (i, item) {
                                $(item).click(function () {
                                    var $toRemove = $(item).closest('li');
                                    var $sortable = $toRemove.closest('ul.sortable');
                                    var dialog = $('#deleteModal');
                                    $(dialog).find('#deleteContent').html('Are you sure you want to remove ' +
                                    'data element &quot;' + $toRemove.text().trim() + '&quot;?');
                                    $(dialog).find('#deleteButton').on('click', function (e) {
                                        listDragAndDropService.deleteItem($toRemove, $sortable, 0);
                                        $(dialog).modal('hide');
                                    });
                                    $(dialog).modal('show');
                                });
                            });
                        });
                        var parent = element.parent();
                        if (parent.is('div')) {
                            parent.append($('<ul></ul>').
                                attr('data-drop-type', 'multiple').
                                attr('data-proptype', 'empty').
                                addClass('sortable').append(element.parent().children()));

                        }

                    };

                    timer(editorAction, 0);

                });
            }
        };

    }

}());