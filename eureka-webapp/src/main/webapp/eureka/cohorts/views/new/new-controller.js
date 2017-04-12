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

    NewCtrl.$inject = ['CohortService', '$state', '$rootScope'];

    function NewCtrl(CohortService, $state, $rootScope) {
        let vm = this;
        let createCohort = CohortService.createCohort;
        let userId = $rootScope.user.info.id;

        vm.submitCohortForm = submitCohortForm;
        vm.memberList = [];
        vm.currentMemeberList = [];

        function submitCohortForm() {
            let cohortObject = {};
            cohortObject.name = vm.name;
            cohortObject.description = vm.description;
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