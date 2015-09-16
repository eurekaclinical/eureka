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

    NewCtrl.$inject = ['CohortService', 'CohortTreeService'];

    function NewCtrl(CohortService, CohortTreeService) {
        let vm = this;
        let getTreeData = CohortTreeService.getTreeData;
        vm.toggleNode = toggleNode;

        initTree();

        function initTree() {
            vm.loading = true;
            getTreeData('root').then(data => {
                console.log(data);
                vm.dummyTreeData = data;
                delete vm.loading;
            }, displayError);
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function toggleNode(node) {
            if (!node.nodes) {
                node.loading = true;
                getTreeData(node.attr.id).then(data => {
                    console.log(data);
                    node.nodes = data;
                    delete node.loading;
                }, displayError);
            }
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