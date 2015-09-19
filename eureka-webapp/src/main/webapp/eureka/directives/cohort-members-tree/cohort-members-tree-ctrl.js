(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('CohortMembersTreeCtrl', CohortMembersTreeCtrl);

    CohortMembersTreeCtrl.$inject = ['CohortTreeService', 'CohortFilterService'];

    function CohortMembersTreeCtrl(CohortTreeService, CohortFilterService) {
        let vm = this;
        let getTreeData = CohortTreeService.getTreeData;
        let filterCohorts = CohortFilterService.filterCohorts;
        vm.toggleNode = toggleNode;
        vm.nodeAllowed = nodeAllowed;
        vm.filterCohortList = filterCohortList;
        vm.memberList = vm.memberList || [];

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        initTree();

        function initTree() {
            vm.loading = true;
            getTreeData('root').then(data => {
                console.log(data);
                vm.treeData = data;
                delete vm.loading;
            }, displayError);
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

        function filterCohortList(){
            vm.loading = true;
            vm.treeData = [];
            filterCohorts(vm.treeSearch).then(data => {
                vm.treeData = data;
                delete vm.loading;
            }, displayError);
        }

    }
}());