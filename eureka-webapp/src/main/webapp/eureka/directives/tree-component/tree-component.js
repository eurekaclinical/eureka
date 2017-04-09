(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('treeComponent', TreeComponent);

    function TreeComponent() {
        return {
            restrict: 'E',
            bindToController: {
                memberList: '='
            },
            scope: {},
            replace: false,
            templateUrl: 'eureka/directives/tree-component/tree-component.html',
            controller: 'TreeComponentCtrl',
            controllerAs: 'vm'
        };
    }
}());