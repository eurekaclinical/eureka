"use strict";

var eurekaApp = angular.module('eurekaApp', ['ngRoute', 'angularValidator' ]);

eurekaApp.directive('testdirective', ['$timeout', function (timer) {
    return function(scope, element, attrs) {
        console.log('ROW: index = ', scope.$index);
        scope.$watch('$last',function (v) {
           var logIt = function () {
               console.log("LAST");

               $('ul.sortable').each(function (i, list) {
                $(list).find('li').each(function (j, item) {
                    eureka.editor.addDroppedElement(item, $(list));
                    console.log("item: " + item.attributes['data-key'].value);
                });

                $('span.delete-icon').each(function (i, item) {
			      $(item).click(function () {
				    var $toRemove = $(item).closest('li');
				    var $sortable = $toRemove.closest('ul.sortable');
				    var dialog = $('#deleteModal');
				    $(dialog).find('#deleteContent').html('Are you sure you want to remove data element &quot;' + $toRemove.text().trim() + '&quot;?');
				    $(dialog).find('#deleteButton').on('click', function (e) {
					   eureka.editor.deleteItem($toRemove, $sortable, 0);
					   $(dialog).modal('hide');
				    });
				    $(dialog).modal('show');
			     });
		      });
            });
           };

           timer(logIt, 0);

        });

    }       
}]);
