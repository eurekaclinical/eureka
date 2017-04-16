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

    EditCtrl.$inject = ['CohortService', '$stateParams', '$state', 'dragAndDropService'];

    function EditCtrl(CohortService, $stateParams, $state, dragAndDropService) {
        var vm = this;
        let updateCohortCall = CohortService.updateCohort;
        vm.memberList = [];
        vm.description;
        vm.name;
        vm.currentCohort = {};

        if ($stateParams.key) {
            vm.memberList = [];
            CohortService.getCohort($stateParams.key).then(function(data) {
                var test = [];
                vm.destination = data;
                vm.currentCohort.name = data.name;
                vm.currentCohort.description = data.description;
                vm.currentCohort.id = data.id;
                console.log(data.cohort.node);

                // lets call the traverse function to go through the returned nodes structure
                traverseNodes(data.cohort.node);

            }, displayError);

        }

        vm.updateCohort = function() {
            vm.currentCohort.name = vm.destination.name;
            vm.currentCohort.description = vm.destination.description;

            updateCohortCall(vm.currentCohort).then(data => {
                console.log('we made it back', data);
                $state.transitionTo('cohorts');
            }, displayError);

        }

        // This function takes the saved nodes and goes through the tree and plucks all with a valid id. Then adds to membelist which populates the dropzone
        function traverseNodes(data) {

            const reducer = (results, node) => {
                //         console.log(results);
                //console.log(node, !_.isEmpty(node.id), node.id, _.isEmpty(node.id));
                node.id && results.push(node);
                _.without([node.left_node, node.right_node], undefined).reduce(reducer, results);
                return results;
            }

            const fullResults = [data].reduce(reducer, []);
            console.log(fullResults + '%%%%%%%%%')

            for (var i = 0; i < fullResults.length; i++) {
                fullResults[i].displayName = fullResults[i].name
            }
            vm.memberList = fullResults;
            dragAndDropService.setNodes(fullResults, 'arg2');

            vm.currentCohort.memberList = vm.memberList;
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
})();