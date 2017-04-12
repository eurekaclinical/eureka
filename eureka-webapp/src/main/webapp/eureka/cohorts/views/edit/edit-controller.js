(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:EditCtrl
     * @description
     * This is the edit controller for the cohorts section of the application.
     * @requires cohorts.CohortService
     * @requires $stateParams
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.EditCtrl', EditCtrl);

    EditCtrl.$inject = ['CohortService', '$stateParams', 'dragAndDropService'];

    function EditCtrl(CohortService, $stateParams, dragAndDropService) {
        var vm = this;
        vm.memberList = [];

        if ($stateParams.key) {
            vm.memberList = [];
            CohortService.getCohort($stateParams.key).then(function(data) {
                var test = [];
                vm.destination = data;
                data.cohort.node.displayName = data.cohort.node.name;
                test.push(data.cohort.node)
                vm.memberList = test;
                dragAndDropService.setNodes(data.cohort.node)

            }, displayError);

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
})();