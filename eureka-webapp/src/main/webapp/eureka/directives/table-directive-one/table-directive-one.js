(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('tableDirectiveOne', TableDirectiveOne);

    function TableDirectiveOne() {
        return {
            restrict: 'EA',
            scope: true,
           	link: function (scope, element, attrs) {
				scope.tableData=scope.$eval(attrs.tableData);
                scope.tableParameters=scope.$eval(attrs.tableParameters);
			},
            templateUrl: 'eureka/directives/table-directive-one/table-directive-one.html',
        };
    }
}());