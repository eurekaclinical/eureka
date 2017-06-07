(function () {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:MainCtrl
     * @description
     * This is the main controller for the cohorts section of the application.
     * @requires cohorts.CohortService
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.DeleteModalCtrl', DeleteModalCtrl)
        .controller('cohorts.MainCtrl', MainCtrl);

    DeleteModalCtrl.$inject = ['$uibModalInstance', 'cohortName'];
    MainCtrl.$inject = ['CohortService', 'NgTableParams', '$uibModal'];

    function DeleteModalCtrl($uibModalInstance, cohortName) {
        var mo = this;
        mo.cohortName = cohortName;
        mo.ok = function () {
            $uibModalInstance.close();
        };

        mo.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }

    function MainCtrl(CohortService, NgTableParams, $uibModal) {
        var vm = this;
        var copyData = [];

        vm.remove = remove;
        vm.currentSelectedItem = {};

        function remove(key) {
            CohortService.removeCohort(key);
            vm.tableParams.filter({});
            for (var i = 0; i < vm.copyData.length; i++) {
                if (vm.copyData[i].name === key) {
                    vm.copyData.splice(i, 1);
                    break;
                }
            }
        }

        function displayDeleteError(msg) {
            vm.deleteErrorMsg = msg;
        }

        function displayLoadError(msg) {
            vm.loadErrorMsg = msg;
        }

        function success(cohorts) {
			vm.copyData = cohorts;
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: vm.copyData });
        }
		
        vm.deleteCohort = function (currentItem) {
            $uibModal.open({
                templateUrl: 'deleteModal.html',
                controller: 'cohorts.DeleteModalCtrl',
                controllerAs: 'mo',
                resolve: {
                    cohortName: function () {
                        return currentItem;
                    }
                }
            })
                .result.then(
                function () {
                    removeCohort(currentItem);
					displayDeleteError('');
                }
                );
        };

        function deleteSuccess() {
            vm.tableParams.filter({});
            for (var i = 0; i < vm.copyData.length; i++) {
                if (vm.copyData[i].name === vm.currentSelectedItem) {
                    vm.copyData.splice(i, 1);
                    break;
                }
            }
            vm.tableParams.reload();
        }

        function removeCohort(data) {
            vm.currentSelectedItem = data;
            vm.deferred = CohortService.removeCohort(data).then(deleteSuccess, displayDeleteError);
        }

        CohortService.getCohorts().then(success, displayLoadError);
    }
})();