(function() {


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
    .controller('phenotypes.MainCtrl', MainCtrl)
    .controller('phenotypes.ModalCtrl', ModalCtrl);

  MainCtrl.$inject = ['$state', 'PhenotypeService', 'NgTableParams', '$uibModal'];
  ModalCtrl.$inject = ['$uibModalInstance'];

  function ModalCtrl($uibModalInstance, currentUser){
      var mo = this;
      mo.currentUser = currentUser;
      mo.ok = function () {
          $uibModalInstance.close();
      };

      mo.cancel = function () {
          $uibModalInstance.dismiss('cancel');
      };

    }
  function MainCtrl($state, PhenotypeService, NgTableParams, $uibModal) {

    var vm = this;
    vm.remove = remove;
    var copyData = [];

    function remove(key) {
      PhenotypeService.removePhenotype(key.id);
      vm.tableParams.filter({});
      for (var i = 0; i < vm.props.length; i++) {
        if (vm.props[i].displayName === key.displayName) {
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

    vm.showModal = function(user, indexOfRow){
        var currentItem = user;
        var currentRow = indexOfRow;
        $uibModal.open({
            templateUrl: 'myModal.html',
            controller: 'cohorts.ModalCtrl',
            controllerAs: 'mo',
            resolve: {
                currentUser: function () {
                    return user;
                }
            }
        })
        .result.then(
            function () {
                remove(currentItem);
                //   vm.copyData.splice(currentRow, 1);
                 vm.tableParams.reload();
                ;
            }, 
            function () {
                //do nothing but cancel
            }
        );
    }

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
})();
