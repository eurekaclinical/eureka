(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:NewCtrl
     * @description
     * This is the new controller for the cohorts section of the application.
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.NewCtrl', NewCtrl);

    NewCtrl.$inject = ['CohortService'];

    function NewCtrl(CohortService) {
        var vm = this;

        getTableData();

        function getTableData() {
            CohortService.getTableData().then(function (data) {
                console.log(data);
                vm.dummyTreeData = data;
            }, displayError);
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
      /*  vm.dummyTreeData = [{
            'id': 1,
            'title': 'node1',
            'nodes': [
                {
                    'id': 11,
                    'title': 'node1.1',
                    'nodes': [
                        {
                            'id': 111,
                            'title': 'node1.1.1',
                            'nodes': []
                        }
                    ]
                },
                {
                    'id': 12,
                    'title': 'node1.2',
                    'nodes': []
                }
            ]
        }];*/

    }

}());