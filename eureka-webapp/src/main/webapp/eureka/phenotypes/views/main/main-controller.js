(function () {


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
    .controller('phenotypes.ModalCtrl', ModalCtrl)
    .controller('phenotypes.DeleteModalCtrl', DeleteModalCtrl)
    .controller('phenotypes.MainCtrl', MainCtrl);



  ModalCtrl.$inject = ['$uibModalInstance'];
  DeleteModalCtrl.$inject = ['$uibModalInstance', 'data'];
  MainCtrl.$inject = ['$state', 'PhenotypeService', 'NgTableParams', '$uibModal'];

  function ModalCtrl($uibModalInstance, currentUser) {
    var mo = this;
    mo.currentUser = currentUser;
    mo.ok = function () {
      $uibModalInstance.close();
    };

    mo.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  }

  function DeleteModalCtrl($uibModalInstance, data) {
    var mo = this;
    mo.data = data;
    mo.ok = function () {
      $uibModalInstance.close();
    };

    mo.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  }
  function MainCtrl($state, PhenotypeService, NgTableParams, $uibModal) {

    var vm = this;
    var copyData = [];

    vm.currentSelectedItem = {};
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
      vm.tableParams = new NgTableParams({}, { dataset: copyData });
    }

    function displayError(msg) {
      vm.errorMsg = msg;
    }

    vm.removeFilter = function () {
      vm.filter.show = false;
      vm.query.filter = '';

      if (vm.filter.form.$dirty) {
        vm.filter.form.$setPristine();
      }
    };

    vm.deletePhenotype = function (user, indexOfRow) {
      var currentItem = user;
      var currentRow = indexOfRow;

      $uibModal.open({
        templateUrl: 'myModal.html',
        controller: 'phenotypes.ModalCtrl',
        controllerAs: 'mo',
        resolve: {
          currentUser: function () {
            return user;
          }
        }
      })
        .result.then(
        function () {
          removePhenotype(currentItem);
        },
        function (arg) {
          console.log(arg);
        }
        );
    }

    function deleteSuccess() {
      vm.tableParams.filter({});
      for (var i = 0; i < vm.props.length; i++) {
        if (vm.props[i].displayName === vm.currentSelectedItem.displayName) {
          vm.props.splice(i, 1);
          break;
        }
      }
      vm.tableParams.reload();
    }

    function deleteError(data) {
      $uibModal.open({
        templateUrl: 'errorModal.html',
        controller: 'phenotypes.DeleteModalCtrl',
        controllerAs: 'mod',
        resolve: {
          data: function () {
            return data;
          }
        }
      })
        .result.then(
        function () {

        },
        function (arg) {

        }
        );
    }

    function removePhenotype(data) {
      vm.currentSelectedItem = data;
      vm.deferred = PhenotypeService.removePhenotype(data.id).then(deleteSuccess, deleteError);
    }

    vm.onOrderChange = function () {
      return PhenotypeService.getSummarizedUserElements(vm.query);
    };

    vm.onPaginationChange = function () {
      return PhenotypeService.getSummarizedUserElements(vm.query);
    };

    PhenotypeService.getSummarizedUserElements().then(success, displayError);

    var messages = PhenotypeService.getPhenotypeMessages();
    vm.messages = messages;

    vm.openMenu = function ($mdOpenMenu, evt) {
      $mdOpenMenu(evt);
    };

    vm.navigateTo = function (phenotypeType) {
      $state.transitionTo('createPhenotype', {
        type: phenotypeType
      });
    };

  }
})();
