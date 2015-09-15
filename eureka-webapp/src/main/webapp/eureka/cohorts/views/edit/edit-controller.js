(function() {
    'use strict';

    angular
        .module('cohorts')
        .controller('cohorts.EditCtrl', EditCtrl);
        
    EditCtrl.$inject = ['CohortService', '$stateParams'];
    
    function EditCtrl(CohortService, $stateParams) {
        var vm = this;

        if ($stateParams.key) {

            CohortService.getCohort($stateParams.key).then(function(data) {
                vm.destination = data;
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