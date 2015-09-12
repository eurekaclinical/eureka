/*angular.module('eurekaApp').controller(
    "CohortEditController", ['$scope', 'CohortService', '$stateParams',
        function( $scope, CohortService, $stateParams) {

            var vm = this;

            if ($stateParams.key) {

                CohortService.getCohort($stateParams.key).then(function(data) {
                    vm.cohortDestination = data;
                    vm.getPhenotypes = function() {
                        CohortService.getPhenotypes(data.cohort).then( function( promises ) {

                            var phenotypes = [];
                            for (var i = 0; i < promises.length; i++) {
                                phenotypes.push(new Object({
                                    "dataElementKey": promises[i].data.key,
                                    "dataElementDisplayName": promises[i].data.displayName,
                                    "type": "SYSTEM"
                                }));

                            }

                            vm.cohortDestination.phenotypes = phenotypes;

                            eureka.editor.setup(data.id != null ? data.id : null,
                                '#userTree', '#definitionContainer', '#savePropositionButton');

                        });

                    };

                    vm.getPhenotypes();


                }, displayError);

            }

            function displayError(msg) {
                vm.errorMsg = msg;
            }

        }]
); */

(function() {
    'use strict';

    angular
        .module('cohorts')
        .controller('cohorts.EditController', EditController);
        
    EditController.$inject = ['CohortService', '$stateParams'];
    
    function EditController(CohortService, $stateParams) {
        var vm = this;

        if ($stateParams.key) {

            CohortService.getCohort($stateParams.key).then(function(data) {
                vm.cohortDestination = data;
                getPhenotypes();

            }, displayError);

        }

        function getPhenotypes() {
            CohortService.getPhenotypes(data.cohort).then( function( promises ) {

            var phenotypes = [];
            for (var i = 0; i < promises.length; i++) {
                phenotypes.push(new Object({
                    "dataElementKey": promises[i].data.key,
                    "dataElementDisplayName": promises[i].data.displayName,
                    "type": "SYSTEM"
                }));

            }

            vm.cohortDestination.phenotypes = phenotypes;

            eureka.editor.setup(data.id !== null ? data.id : null,
                '#userTree', '#definitionContainer', '#savePropositionButton');

            });

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
     }
})();