(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('CohortMembersDropZoneCtrl', CohortMembersDropZoneCtrl);

    CohortMembersDropZoneCtrl.$inject = [];

    function CohortMembersDropZoneCtrl() {
        let vm = this;

        vm.treeOptions = {
            accept: addMember
        };

        function nodeAllowed(node, memberList) {
            memberList = memberList || vm.memberList;
            if (memberList.indexOf(node) !== -1) {
                return false;
            }
            let differentType = vm.memberList.some(function(memberNode) {
                return node.type !== memberNode.type;
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
