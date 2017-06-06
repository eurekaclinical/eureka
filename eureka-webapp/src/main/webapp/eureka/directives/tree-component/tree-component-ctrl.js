(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('TreeComponentCtrl', TreeComponentCtrl);

    TreeComponentCtrl.$inject = ['PhenotypeService', 'NgTableParams', 'TreeService'];

    function TreeComponentCtrl(PhenotypeService, NgTableParams, TreeService) {
        let vm = this;

        vm.breadCrumbs = [{ key: 'root', displayName: 'root' }];
		
        TreeService.getTreeRoot().then(function(data) {
            // NG Table
            vm.tableParams = new NgTableParams({}, { dataset: data });
        }, displayError);

		PhenotypeService.getPhenotypeRoot().then(function(data) {
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
            }
        };

        vm.addNode = function(node) {
            if (node) {
                setNodes(node);
            }
        };
        vm.addUserNode = function(node) {
            if (node) {
                setNodes(node);
            }
        };
        vm.removeNode = function(node) {
			if (vm.selectedItems) {
				for (var i = 0; i < vm.selectedItems.length; i++) {
					// we will need to look for both key and name.  name is key in update, but key is present on create.
					if (node.key === undefined) {
						if (vm.selectedItems[i].name === node.name) {
							vm.selectedItems.splice(i, 1);
							break;
						}
					} else {
						if (vm.selectedItems[i].key === node.key) {
							vm.selectedItems.splice(i, 1);
							break;
						}
					}
				}
			}
        };
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
        };

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function setNodes(obj) {
			if (obj !== undefined) {
				let isDuplicate = false;
				//lets do it the long way first then we will refactor.  Lets see if there are duplicates JS
				if (!vm.selectedItems) {
					vm.selectedItems = [];
				}
				if (vm.selectedItems.length < 1) {
					vm.selectedItems.push(obj);
				} else {
					for (var i = 0; i < vm.selectedItems.length; i++) {
						if (vm.selectedItems[i].hasOwnProperty('name')) {
							if (vm.selectedItems[i].name === obj.key) {
								isDuplicate = true;
								break;
							}

						} else if (vm.selectedItems[i].hasOwnProperty('key')) {
							if (vm.selectedItems[i].key === obj.key) {
								isDuplicate = true;
								break;
							}

						}


					}
					if (isDuplicate !== true) {
						vm.selectedItems.push(obj);
					}

				}
			}
        }

    }
}());