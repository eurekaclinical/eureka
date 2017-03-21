(function() {
  'use strict';

  /**
   * @ngdoc controller
   * @name eureka.phenotypes.controller:CreateCtrl
   * @description
   * This is the create controller for the phenotypes section of the application.
   * @requires $scope
   * @requires $location
   * @requires eureka.phenotypes.PhenotypeService
   */

  angular
    .module('eureka.phenotypes')
    .controller('phenotypes.CreateCtrl', CreateCtrl);

  CreateCtrl.$inject = ['$stateParams', 'PhenotypeService'];

  function CreateCtrl($stateParams, PhenotypeService) {

    var vm = this;

    vm.type = _.startCase($stateParams.type);
    vm.timeUnits = ['minutes', 'hours', 'days'];

    switch ($stateParams.type) {
      case 'categorization':
        vm.description = 'This category data element may be used wherever its member data elements are accepted.';
        break;
      case 'sequence':
        vm.description = 'Computes intervals with the same start and stop time as the Main data element below when ' +
          'the temporal relationships below are satisfied.';
        break;
      case 'frequency':
        vm.description = 'Computes an interval over the temporal extent of the intervals contributing to the ' +
          'specified frequency count below.';
        break;
      case 'value-threshold':
        vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
        break;
    }

    vm.openMenu = function($mdOpenMenu, evt) {
      $mdOpenMenu(evt);
    };

    function displayError(msg) {
      vm.errorMsg = msg;
    }

  }
}());
