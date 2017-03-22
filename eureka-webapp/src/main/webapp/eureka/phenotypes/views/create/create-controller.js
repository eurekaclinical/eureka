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

    CreateCtrl.$inject = ['$stateParams', 'PhenotypeService', 'NgTableParams'];

    function CreateCtrl($stateParams, PhenotypeService, NgTableParams) {

        var vm = this;

        vm.breadCrumbs = [{ id: 'root', text: 'root' }];

        PhenotypeService.getTreeRoot().then(function(data) {
            //  vm.treeData = data;

            //vm.props = data;
            //copyData = data;
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: data });



        }, displayError);

        vm.selectNode = function(node) {
            var currentNode = node;
            var hasChildren = node.children;
            PhenotypeService.getTreeNode(currentNode.id).then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                if (hasChildren) {
                    vm.breadCrumbs.push({ id: currentNode.id, text: currentNode.text });
                }
                vm.tableParams = new NgTableParams({}, { dataset: data });



            }, displayError);

        }

        vm.setBreadCrumbs = function(node) {
            var currentNode = node;
            var hasChildren = node.children;
            var pos = 0;
            PhenotypeService.getTreeNode(currentNode.id).then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                if (hasChildren) {
                    vm.breadCrumbs.push({ id: currentNode.id, text: currentNode.text });

                }

                vm.tableParams = new NgTableParams({}, { dataset: data });

                pos = vm.breadCrumbs.map(function(e) { return e.id; }).indexOf(currentNode.id);
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

    }
}());