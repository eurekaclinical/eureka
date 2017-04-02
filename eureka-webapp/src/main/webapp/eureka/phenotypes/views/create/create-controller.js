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

    CreateCtrl.$inject = ['$stateParams', 'PhenotypeService', 'NgTableParams', 'dragAndDropService', 'TreeService'];

    function CreateCtrl($stateParams, PhenotypeService, NgTableParams, dragAndDropService, TreeService) {

        var vm = this;

        vm.breadCrumbs = [{ key: 'root', displayName: 'root' }];
        //start get tree list
        vm.currentMemeberList = [];
        getMemberList();



        TreeService.getTreeRoot().then(function(data) {
            //  vm.treeData = data;

            //vm.props = data;
            //copyData = data;
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: data });


            callUserRoot();
        }, displayError);

        function callUserRoot() {
            TreeService.getUserListRoot().then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                vm.tableParamsUser = new NgTableParams({}, { dataset: data });



            }, displayError);
        }
        vm.selectNode = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            TreeService.getTreeNode(currentNode.key).then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                if (hasChildren) {
                    vm.breadCrumbs.push({ key: currentNode.key, displayName: currentNode.displayName, parent: currentNode.parent });
                }
                vm.tableParams = new NgTableParams({}, { dataset: data.children });



            }, displayError);

        }
        vm.addNode = function(node) {

            if (node) {
                dragAndDropService.setNodes(node);
            }
            getMemberList()
        }
        vm.removeNode = function(node) {
            for (var i = 0; i < vm.currentMemeberList.length; i++) {
                if (vm.currentMemeberList[i].key === node.key) {
                    vm.currentMemeberList.splice(i, 1);
                    break;
                }
            }
        }
        vm.setBreadCrumbs = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            var pos = 0;
            var returnedData = [];

            TreeService.getTreeNode(currentNode.key).then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                if (data.hasOwnProperty('parent') && data['parent']) {
                    returnedData = data.children;
                } else {
                    returnedData = data;
                }
                if (hasChildren) {
                    vm.breadCrumbs.push({ key: currentNode.key, displayName: currentNode.displayName });

                }

                vm.tableParams = new NgTableParams({}, { dataset: returnedData });

                pos = vm.breadCrumbs.map(function(e) { return e.key; }).indexOf(currentNode.key);
                vm.breadCrumbs.length = pos + 1;

            }, displayError);

        }
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

        function getMemberList() {
            vm.currentMemeberList = dragAndDropService.getNodes()
        }

    }
}());