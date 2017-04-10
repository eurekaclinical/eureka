(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.phenotypes.controller:EditCtrl
     * @description
     * This is the edit controller for the phenotypes section of the application.
     * @requires $scope
     * @requires $location
     * @requires eureka.phenotypes.PhenotypeService
     */

    angular
        .module('eureka.phenotypes')
        .controller('phenotypes.EditCtrl', EditCtrl);

    EditCtrl.$inject = ['$stateParams', 'PhenotypeService'];

    function EditCtrl($stateParams, PhenotypeService) {

        var vm = this;
        var currentType = '';
        /*
            if ($stateParams.id) {

                    PhenotypeService.getPhenotype($stateParams.id).then(function(data) {
                        vm.destination = data;
                        currentType = data.type;
                        setType();
                        //getPhenotypes(data);

                    }, displayError);

            } */

        vm.type = _.startCase($stateParams.type);
        vm.timeUnits = ['minutes', 'hours', 'days'];

        function setType() {
            switch (currentType) {
                case 'CATEGORIZATION':
                    vm.description = 'This category data element may be used wherever its member data elements are accepted.';
                    vm.name = 'Categorization';
                    break;
                case 'SEQUENCE':
                    vm.description = 'Computes intervals with the same start and stop time as the Main data element below when ' +
                        'the temporal relationships below are satisfied.';
                    vm.name = 'Sequence';
                    break;
                case 'FREQUENCY':
                    vm.description = 'Computes an interval over the temporal extent of the intervals contributing to the ' +
                        'specified frequency count below.';
                    vm.name = 'Frequency';
                    break;
                case 'VALUE_THRESHOLD':
                    vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
                    vm.name = 'Value Threshold';
                    break;
            }
        }
        vm.openMenu = function($mdOpenMenu, evt) {
            $mdOpenMenu(evt);
        };

        function displayError(msg) {
            vm.errorMsg = msg;
        }

    }
}());