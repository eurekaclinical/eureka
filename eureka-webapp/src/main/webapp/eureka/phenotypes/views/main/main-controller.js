(function() {
  'use strict';

  /**
   * @ngdoc controller
   * @name eureka.phenotypes.controller:MainCtrl
   * @description
   * This is the main controller for the phenotypes section of the application.
   * @requires $scope
   * @requires $location
   * @requires eureka.phenotypes.PhenotypeService
   */

  angular
    .module('eureka.phenotypes')
    .controller('phenotypes.MainCtrl', MainCtrl);

  MainCtrl.$inject = ['$state', 'PhenotypeService', 'NgTableParams'];

  function MainCtrl($state, PhenotypeService, NgTableParams) {

    var vm = this;
    vm.remove = remove;
    var copyData = [];

    function remove(key) {
      PhenotypeService.removeCohort(key);
      for (var i = 0; i < vm.props.length; i++) {
        if (vm.props[i].name === key) {
          vm.props.splice(i, 1);
          break;
        }
      }
    }

    vm.selected = [];

    vm.filter = {
      options: {
        debounce: 500
      }
    };

    vm.query = {
      filter: '',
      order: 'name',
      limit: 5,
      page: 1
    };

    function success(data) {
      vm.props = data;
      copyData = data;
      // NG Table
      vm.tableParams = new NgTableParams({}, { dataset: copyData});
    }

    function displayError(msg) {
      vm.errorMsg = msg;
    }

    vm.removeFilter = function () {
      vm.filter.show = false;
      vm.query.filter = '';

      if(vm.filter.form.$dirty) {
        vm.filter.form.$setPristine();
      }
    };

    // in the future we may see a few built in alternate headers but in the mean time
    // you can implement your own search header and do something like
    vm.search = function (predicate) {
      vm.filter = predicate;
      vm.deferred = PhenotypeService.getSummarizedUserElements(vm.query).then(success, displayError);
    };

    vm.onOrderChange = function () {
      return PhenotypeService.getSummarizedUserElements(vm.query);
    };

    vm.onPaginationChange = function () {
      return PhenotypeService.getSummarizedUserElements(vm.query);
    };

    PhenotypeService.getSummarizedUserElements().then(success, displayError);

    var messages =  PhenotypeService.getPhenotypeMessages();
    vm.messages = messages;

    vm.openMenu = function($mdOpenMenu, evt) {
      $mdOpenMenu(evt);
    };

    vm.navigateTo = function(phenotypeType) {
      $state.transitionTo('createPhenotype', {
        type: phenotypeType
      });
    };

  }
}());
