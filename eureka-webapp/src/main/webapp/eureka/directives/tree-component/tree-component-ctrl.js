(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('TreeComponentCtrl', TreeComponentCtrl);

    TreeComponentCtrl.$inject = ['PhenotypeService', 'NgTableParams', 'TreeService', '$scope'];

    function TreeComponentCtrl(PhenotypeService, NgTableParams, TreeService, $scope) {
        let vm = this;

        vm.breadCrumbs = [{ key: 'root', displayName: 'root' }];
        let currentNodes = [];

        TreeService.getTreeRoot().then(function(data) {
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: data });
        }, displayError);

		TreeService.getUserListRoot().then(function(data) {
			// NG Table
			vm.tableParamsUser = new NgTableParams({}, { dataset: data });
		}, displayError);

        vm.selectNode = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            if (hasChildren !== false) {
                TreeService.getTreeNode(currentNode.key).then(function(data) {
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
                setNodes(node)
            }
            getMemberList()
        }
        vm.addUserNode = function(node) {
            if (node) {
                setNodes(node);
            }
            getMemberList();
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
            vm.currentMemeberList = getNodes()
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function setNodes(obj, arg2) {
            currentNodes = vm.currentMemeberList;
            if (arg2 == undefined || arg2 === null) {
                if (obj !== undefined) {
                    let currentList = [];
                    currentList = currentNodes;
                    let isDuplicate = false;
                    //lets do it the long way first then we will refactor.  Lets see if there are duplicates JS
                    if (currentList.length < 1) {
                        currentNodes.push(obj);
                    } else {
                        for (var i = 0; i < currentList.length; i++) {
                            if (currentList[i].hasOwnProperty('name')) {
                                if (currentList[i].name === obj.key) {
                                    isDuplicate = true;
                                    break;
                                }

                            } else if (currentList[i].hasOwnProperty('key')) {
                                if (currentList[i].key === obj.key) {
                                    isDuplicate = true;
                                    break;
                                }

                            }


                        }
                        if (isDuplicate !== true) {
                            currentNodes.push(obj);
                        }

                    }
                }
            } else {
                currentNodes = obj;
            }
        }


        function getNodes() {
            return currentNodes;
        }

        function clearNodes() {
            currentNodes = [];
        }

    }
}());