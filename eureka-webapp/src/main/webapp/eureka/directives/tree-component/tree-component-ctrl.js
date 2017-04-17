(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('TreeComponentCtrl', TreeComponentCtrl);

    TreeComponentCtrl.$inject = ['PhenotypeService', 'NgTableParams', 'dragAndDropService', 'TreeService', '$scope'];

    function TreeComponentCtrl(PhenotypeService, NgTableParams, dragAndDropService, TreeService, $scope) {
        let vm = this;


        vm.breadCrumbs = [{ key: 'root', displayName: 'root' }];
        //start get tree list
        vm.currentMemeberList = [];

        init();


        TreeService.getTreeRoot().then(function(data) {
            //  vm.treeData = data;

            //vm.props = data;
            //copyData = data;
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: data });


            callUserRoot();
        }, displayError);

        function init() {

            dragAndDropService.clearNodes();
            /*if (vm.currentMemeberList) {
                dragAndDropService.setNodes(vm.currentMemeberList)
            }*/

        }

        function callUserRoot() {
            TreeService.getUserListRoot().then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                vm.tableParamsUser = new NgTableParams({}, { dataset: data.data });



            }, displayError);
        }
        vm.selectNode = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            if (hasChildren !== false) {
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
            } else {
                //do nothing it is a document
            }

        }
        vm.addNode = function(node) {

            if (node) {
                dragAndDropService.setNodes(node);
            }
            getMemberList()
        }
        vm.addUserNode = function(node) {
            /* After changes add user node will need to be refactored will take care of later in week.
            if (node) {
                node.displayName = node.text;
                dragAndDropService.setNodes(node);
            }
            getMemberList() */
        }
        vm.removeNode = function(node) {
            for (var i = 0; i < vm.currentMemeberList.length; i++) {
                // we will need to look for both key and name.  name is key in update, but key is present on create.
                if (node.key === undefined) {
                    if (vm.currentMemeberList[i].name === node.name) {
                        vm.currentMemeberList.splice(i, 1);
                        break;
                    }
                } else {
                    if (vm.currentMemeberList[i].key === node.key) {
                        vm.currentMemeberList.splice(i, 1);
                        break;
                    }
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

        function getMemberList() {
            vm.currentMemeberList = dragAndDropService.getNodes()
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }




    }
}());