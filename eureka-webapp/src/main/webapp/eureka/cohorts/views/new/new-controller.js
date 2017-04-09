(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:NewCtrl
     * @description
     * This is the new controller for the cohorts section of the application.
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.NewCtrl', NewCtrl);

    NewCtrl.$inject = ['CohortService', '$state'];

    function NewCtrl(CohortService, $state) {
        let vm = this;
        let createCohort = CohortService.createCohort;

        vm.submitCohortForm = submitCohortForm;
        vm.memberList = [];

        function submitCohortForm() {
            let cohortObject = {};
            cohortObject.name = vm.cohort.name;
            cohortObject.description = vm.cohort.description;
            cohortObject.memberList = vm.memberList;

            createCohort(cohortObject).then(data => {
                // if successful we prob need to redirect back to the main table
                console.log('we made it back', data);
                $state.transitionTo('cohorts');
            }, displayError);

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }

}());