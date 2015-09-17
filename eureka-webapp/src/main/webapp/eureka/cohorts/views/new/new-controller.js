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

    NewCtrl.$inject = ['CohortTreeService'];

    function NewCtrl(CohortTreeService) {
        let vm = this;
        let getTreeData = CohortTreeService.getTreeData;
        vm.toggleNode = toggleNode;
        vm.nodeAllowed = nodeAllowed;
        vm.memberList = [];

        vm.treeOptions = {
            accept: addMember
        };

        initTree();

        function initTree() {
            vm.loading = true;
            getTreeData('root').then(data => {
                console.log(data);
                vm.treeData = data;
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

        function nodeAllowed(node, memberList) {
            memberList = memberList || vm.memberList;
            if (memberList.indexOf(node) !== -1) {
                return false;
            }
            let differentType = vm.memberList.some(function(memberNode) {
                return node.attr['data-type'] !== memberNode.attr['data-type'];
            });
            return !differentType;
        }

        function addMember(nodeScope, memberListScope) {
            let node = nodeScope.$modelValue;
            let memberList = memberListScope.$modelValue;
            return nodeAllowed(node, memberList);
        }

    }

}());