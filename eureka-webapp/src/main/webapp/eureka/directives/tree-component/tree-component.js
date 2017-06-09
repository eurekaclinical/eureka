(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('treeComponent', TreeComponent);

    function TreeComponent() {
        return {
            restrict: 'AE',
            scope: {
                selectedItems: '=',
				multipleSelectStr: '@?multipleSelect'
            },
            bindToController: true,
            replace: false,
            templateUrl: 'eureka/directives/tree-component/tree-component.html',
            controller: 'TreeComponentCtrl',
            controllerAs: 'vm'
        };
    }
}());