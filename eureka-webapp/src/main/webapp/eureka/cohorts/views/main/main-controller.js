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

    function ErrorModalCtrl($uibModalInstance, currentUser) {
        var mo = this;
        mo.currentUser = currentUser;
        mo.ok = function () {
            $uibModalInstance.close();
        };

        mo.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }

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

        function success(cohorts) {
            vm.cohortsList = cohorts;
            vm.gridOptions.data = cohorts;
            vm.copyData = cohorts;
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: vm.copyData });
        }

        vm.removeFilter = function () {
            vm.filter.show = false;
            vm.query.filter = '';

            if (vm.filter.form.$dirty) {
                vm.filter.form.$setPristine();
            }
        };

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
        }

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

        vm.onOrderChange = function () {
            return CohortService.getCohorts(vm.query);
        };

        vm.onPaginationChange = function () {
            return CohortService.getCohorts(vm.query);
        };

        // UI-Grid
        vm.gridOptions = {
            enableSorting: true,
            paginationPageSizes: [10, 20, 30],
            paginationPageSize: 10,
            columnDefs: [
                {
                    name: ' ', field: 'edit',
                    cellTemplate: '<a href="${editUrl}" title="Edit">' +
                    '<span class="glyphicon glyphicon-pencil edit-icon" title="Edit"></span></a> ' +
                    '<span class="glyphicon glyphicon-remove delete-icon" title="Delete"></span>'
                },
                { name: 'Name', field: 'name' },
                { name: 'Descripton', field: 'description' },
                { name: 'Type', field: 'type' },
                { name: 'Created', field: 'created_at', enableCellEdit: false, cellFilter: 'date:"longDate"' }
            ],
            data: []
        };

        CohortService.getCohorts().then(success, displayLoadError);
    }
})();