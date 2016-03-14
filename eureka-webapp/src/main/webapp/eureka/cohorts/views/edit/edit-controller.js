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
        
    EditCtrl.$inject = ['CohortService', '$stateParams'];
    
    function EditCtrl(CohortService, $stateParams) {
        var vm = this;

        if ($stateParams.key) {

            CohortService.getCohort($stateParams.key).then(function(data) {
                vm.destination = data;
                getPhenotypes(data);

            }, displayError);

        }

        function getPhenotypes(data) {
            CohortService.getPhenotypes(data.cohort).then( function( promises ) {

            var phenotypes = [];
            for (var i = 0; i < promises.length; i++) {
                phenotypes.push(new Object({
                    phenotypeKey: promises[i].data.key,
                    phenotypeDisplayName: promises[i].data.displayName,
                    type: 'SYSTEM'
                }));

            }

            vm.destination.phenotypes = phenotypes;

            eureka.editor.setup(data.id !== null ? data.id : null,
                '#userTree', '#definitionContainer', '#savePropositionButton');

            });

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
     }
})();