(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('cohortMembersDropZone', CohortMembersDropZone);

    function CohortMembersDropZone() {
        return {
            restrict: 'E',
            bindToController: {
                memberList: '='
            },
            scope: {},
            replace: false,
            templateUrl: 'eureka/directives/cohort-members-drop-zone/cohort-members-drop-zone.html',
            controller: 'CohortMembersDropZoneCtrl',
            controllerAs: 'dropZone'
        };
    }
}());