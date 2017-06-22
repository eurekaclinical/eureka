(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('TreeMultiDropZoneCtrl', TreeMultiDropZoneCtrl)
			.controller('TreeMultiDropZoneDeleteModalCtrl', DeleteModalCtrl)
			.controller('TreeAddModalCtrl', AddModalCtrl);

	TreeMultiDropZoneCtrl.$inject = ['$scope', 'PhenotypeService', 'TreeService', '$uibModal'];
	DeleteModalCtrl.$inject = ['$uibModalInstance', 'displayName'];
	AddModalCtrl.$inject = ['$uibModalInstance'];

	function TreeMultiDropZoneCtrl($scope, PhenotypeService, TreeService, $uibModal) {
		let vm = this;

		vm.add = function () {
			if (!vm.items) {
				vm.items = [];
			}
			$uibModal.open({
				templateUrl: 'addItemsModal.html',
				controller: 'TreeAddModalCtrl',
				controllerAs: 'mo'
			}).result.then(
					function (itemsToAdd) {
						for (let i = 0; i < itemsToAdd.length; i++) {
							let selectedItem = itemsToAdd[i];
							let found = false;
							for (let j = 0; j < vm.items.length; j++) {
								if (selectedItem.key === vm.items[j].key) {
									found = true;
									break;
								}
							}
							if (!found) {
								vm.items.push({
									name: selectedItem.key,
									displayName: selectedItem.displayName,
									type: selectedItem.type
								});
							}
						}
					},
					function () {
					}
			);
		};

		vm.remove = function (itemToRemove) {
			$uibModal.open({
				templateUrl: vm.deleteModalTemplateUrl,
				controller: 'TreeMultiDropZoneDeleteModalCtrl',
				controllerAs: 'mo',
				resolve: {
					displayName: function () {
						return itemToRemove.displayName;
					}
				}
			}).result.then(
					function () {
						let index = vm.items.indexOf(itemToRemove);
						if (index > -1) {
							vm.items.splice(index, 1);
							vm.keys.split(index, 1);
						}
					},
					function (arg) {
					}
			);
		};

		vm.populate = function () {
			if (vm.keys) {
				let phenotypeKeys = [];
				let conceptKeys = [];
				for (let i = 0; i < vm.keys.length; i++) {
					let key = vm.keys[i];
					if (key.startsWith('USER:')) {
						phenotypeKeys.push(key);
					} else {
						conceptKeys.push(key);
					}
				}
				
				TreeService.getTreeNodes(conceptKeys).then(function (concepts) {
					let keyToDisplayName = {};
					for (let i = 0; i < conceptKeys.length; i++) {
						for (let j = 0; j < concepts.length; j++) {
							if (concepts[j].key === conceptKeys[i]) {
								keyToDisplayName[vm.keys[i]] = {
									displayName: concepts[j].displayName,
									type: concepts[j].type
								};
								break;
							}
						}
					}
					vm.items = [];
					for (var i = 0; i < conceptKeys.length; i++) {
						let dn = keyToDisplayName[conceptKeys[i]];
						if (!dn) {
							vm.items.push({
								name: conceptKeys[i],
								displayName: conceptKeys[i],
								type: null
							});
							vm.displayError('Unknown concept ' + conceptKeys[i]);
						} else {
							vm.items.push({
								name: conceptKeys[i],
								displayName: dn.displayName,
								type: dn.type
							});
						}
					}
					for (let i = 0; i < phenotypeKeys.length; i++) {
						PhenotypeService.getPhenotype(phenotypeKeys[i]).then(function (phenotype) {
							vm.items.push({
								name: phenotype.key, 
								displayName: phenotype.displayName,
								type: phenotype.type
							});
						}, function (msg) {
							vm.items.push({
								name: phenotypeKeys[i],
								displayName: phenotypeKeys[i]
							});
							vm.displayError({message: 'Unknown phenotype ' + phenotypeKeys[i]});
						});
					}
				}, vm.displayError);
			}
		};

		$scope.$watch(function () {
			return vm.keys;
		}, function (newValue, oldValue) {
			vm.populate();
		}, true);
	}

	function DeleteModalCtrl($uibModalInstance, displayName) {
		var mo = this;
		mo.displayName = displayName;
		mo.ok = function () {
			$uibModalInstance.close();
		};

		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

	}

	function AddModalCtrl($uibModalInstance) {
		var mo = this;
		mo.itemsToAdd = [];
		mo.ok = function () {
            $uibModalInstance.close(mo.itemsToAdd);
		};
		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	}
}());