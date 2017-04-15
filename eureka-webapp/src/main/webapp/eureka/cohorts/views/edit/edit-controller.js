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
        let updateCohort = CohortService.updateCohort;
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

                // lets test
                testFunction(data.cohort.node);

                //end of test
                /*  data.cohort.node.displayName = data.cohort.node.name;
                  test.push(data.cohort.node)
                  vm.memberList = test;
                  dragAndDropService.setNodes(data.cohort.node) */

            }, displayError);

        }

        vm.updateCohort = function() {
            vm.currentCohort.name = vm.destination.name;
            vm.currentCohort.description = vm.destination.description;
            CohortService.updateCohort(vm.currentCohort);

        }

        // will need to clean up this test function after I get it working JS
        function testFunction(data) {

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