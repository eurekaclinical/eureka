(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('globalGrid', GlobalGrid);

    function GlobalGrid() {
        return {
            restrict: 'E',
            //scope: true,
            templateUrl: 'eureka/directives/global-grid/global-grid.html',
            scope: {
                options : '@',
            },
            link: function(scope, element, attrs) {
              
            scope.gridOptions = {
            enableSorting: true,
            paginationPageSizes: [10, 20, 30],
            paginationPageSize: 10,
            columnDefs: [

            ],
            data:  [
           
            ]
            }; 
            scope.gridOptions.columnDefs = JSON.parse(attrs.gridOptions);
             scope.gridOptions.data = JSON.parse(attrs.gridData);
            
            }
        };
    }
}());