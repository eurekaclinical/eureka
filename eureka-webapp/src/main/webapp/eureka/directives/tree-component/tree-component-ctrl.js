(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('TreeComponentCtrl', TreeComponentCtrl);

	TreeComponentCtrl.$inject = ['PhenotypeService', 'NgTableParams', 'TreeService'];

	function TreeComponentCtrl(PhenotypeService, NgTableParams, TreeService) {
		let vm = this;
		vm.multipleSelect = parseBoolean(vm.multipleSelectStr);

		vm.breadCrumbs = [{key: 'root', displayName: 'root'}];

		TreeService.getTreeRoot().then(function (data) {
			// NG Table
			vm.tableParams = new NgTableParams({}, {dataset: data});
		}, displayError);

		PhenotypeService.getPhenotypeRoot().then(function (data) {
			// NG Table
			vm.tableParamsUser = new NgTableParams({}, {dataset: data});
		}, displayError);

		vm.selectNode = function (node) {
			var currentNode = node;
			var hasChildren = node.parent;
			if (hasChildren !== false) {
				TreeService.getTreeNode(currentNode.key).then(function (data) {
					// NG Table
					if (hasChildren) {
						vm.breadCrumbs.push({key: currentNode.key, displayName: currentNode.displayName, parent: currentNode.parent});
					}
					vm.tableParams = new NgTableParams({}, {dataset: data.children});
				}, displayError);
			}
		};

		vm.addNode = function (node) {
			if (node) {
				setNodes(node);
			}
		};
		vm.addUserNode = function (node) {
			if (node) {
				setNodes(node);
			}
		};
		vm.removeNode = function (node) {
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
		vm.setBreadCrumbs = function (node) {
			var currentNode = node;
			var hasChildren = node.parent;
			var pos = 0;
			var returnedData = [];

			TreeService.getTreeNode(currentNode.key).then(function (data) {
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
					vm.breadCrumbs.push({key: currentNode.key, displayName: currentNode.displayName});

				}

				vm.tableParams = new NgTableParams({}, {dataset: returnedData});

				pos = vm.breadCrumbs.map(function (e) {
					return e.key;
				}).indexOf(currentNode.key);
				vm.breadCrumbs.length = pos + 1;

			}, displayError);
		};

		/**
		 * Parses mixed type values into booleans. This is the same function as filter_var in PHP using boolean validation
		 * @param  {Mixed}        value 
		 * @param  {Boolean}      nullOnFailure = false
		 * @return {Boolean|Null}
		 */
		function parseBoolean(value, nullOnFailure = false) {
			switch (value) {
				case true:
				case 'true':
				case 1:
				case '1':
				case 'on':
				case 'yes':
					value = true;
					break;
				case false:
				case 'false':
				case 0:
				case '0':
				case 'off':
				case 'no':
					value = false;
					break;
				default:
					if (nullOnFailure) {
						value = null;
					} else {
						value = false;
					}
					break;
			}
			return value;
		}

		function displayError(msg) {
			vm.errorMsg = msg;
		}

		function setNodes(obj) {
			if (obj !== undefined) {
				let isDuplicate = false;
				if (!vm.selectedItems || !vm.multipleSelect) {
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