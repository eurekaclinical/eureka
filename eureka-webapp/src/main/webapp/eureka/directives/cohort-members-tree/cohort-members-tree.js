(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('cohortMembersTree', CohortMembersTree);

    function CohortMembersTree() {
        return {
            restrict: 'E',
            bindToController: {
                memberList: '='
            },
            scope: {},
            replace: false,
            templateUrl: 'eureka/directives/cohort-members-tree/cohort-members-tree.html',
            controller: 'CohortMembersTreeCtrl',
            controllerAs: 'tree'
        };
    }
}());