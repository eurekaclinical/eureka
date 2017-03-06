(function() {
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
        .controller('cohorts.MainCtrl', MainCtrl)
        .controller('cohorts.ModalCtrl', ModalCtrl);
        
    MainCtrl.$inject = ['CohortService', 'NgTableParams','$uibModal'];
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
    
    function MainCtrl(CohortService, NgTableParams, $uibModal) {
        var vm = this;
        var copyData = [];

        vm.remove = remove;

        function remove(key) {
            CohortService.removeCohort(key);
            for (var i = 0; i < vm.copyData.length; i++) {
                if (vm.copyData[i].name === key) {
                    vm.copyData.splice(i, 1);
                    break;
                }
            }
        }

        function displayError(msg) {
            vm.errorMsg = msg;
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
            vm.tableParams = new NgTableParams({}, { dataset: vm.copyData});
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
            vm.deferred = CohortService.getCohorts(vm.query).then(success, displayError);
        };

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
                { name: ' ',  field: 'edit',
                  cellTemplate: '<a href="${editUrl}" title="Edit">'+
                  '<span class="glyphicon glyphicon-pencil edit-icon" title="Edit"></span></a> '+
                  '<span class="glyphicon glyphicon-remove delete-icon" title="Delete"></span>'
                },
                { name:'Name', field: 'name' },
                { name:'Descripton', field: 'description' },
                { name:'Type', field: 'type'},
                { name:'Created', field: 'created_at', enableCellEdit:false, cellFilter: 'date:"longDate"'}
            ],
            data: []
        };
       

        CohortService.getCohorts().then(success, displayError);

     }
})();