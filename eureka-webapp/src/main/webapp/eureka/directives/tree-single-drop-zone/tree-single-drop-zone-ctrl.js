(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('TreeSingleDropZoneCtrl', TreeSingleDropZoneCtrl)
			.controller('TreeSingleDropZoneDeleteModalCtrl', DeleteModalCtrl)
			.controller('TreeSetModalCtrl', SetModalCtrl);

	TreeSingleDropZoneCtrl.$inject = ['$scope', 'PhenotypeService', 'TreeService', '$uibModal'];
	DeleteModalCtrl.$inject = ['$uibModalInstance', 'displayName'];
	SetModalCtrl.$inject = ['$uibModalInstance'];

	function TreeSingleDropZoneCtrl($scope, PhenotypeService, TreeService, $uibModal) {
		let vm = this;

		vm.set = function () {
			$uibModal.open({
				templateUrl: 'setItemModal.html',
				controller: 'TreeSetModalCtrl',
				controllerAs: 'mo'
			}).result.then(
					function (selectedItem) {
						if (selectedItem !== null && (!vm.item || selectedItem.key !== vm.item.key)) {
							vm.item = {
								name: selectedItem.key,
								displayName: selectedItem.displayName,
								type: selectedItem.type
							};
						}
					},
					function () {
					}
			);
		};

		vm.remove = function (itemToRemove) {
			$uibModal.open({
				templateUrl: vm.deleteModalTemplateUrl,
				controller: 'TreeSingleDropZoneDeleteModalCtrl',
				controllerAs: 'mo',
				resolve: {
					displayName: function () {
						return itemToRemove.displayName;
					}
				}
			}).result.then(
					function () {
						vm.item = null;
						vm.key = null;
					},
					function (arg) {
					}
			);
		};

		vm.populate = function () {
			if (vm.key) {
				let phenotypeKey = null;
				let conceptKey = null;
				if (vm.key.startsWith('USER:')) {
					phenotypeKey = vm.key;
				} else {
					conceptKey = vm.key;
				}
				vm.item = null;
				if (conceptKey !== null) {
					TreeService.getTreeNode(conceptKey).then(function (concept) {
						vm.item = {
							name: conceptKey,
							displayName: concept.displayName,
							type: concept.type
						};
						if (concept.type === 'SYSTEM') {
							vm.item.systemType = concept.systemType;
						}
					}, function (msg) {
						vm.item = {
							name: conceptKey,
							displayName: conceptKey
						};
						vm.displayError({message: 'Unknown concept ' + conceptKey});
					});
				} else if (phenotypeKey !== null) {
					PhenotypeService.getPhenotype(phenotypeKey).then(function (phenotype) {
						vm.item = {
							name: phenotype.key,
							displayName: phenotype.displayName,
							type: phenotype.type
						};
					}, function (msg) {
						vm.item = {
							name: phenotypeKey,
							displayName: phenotypeKey
						};
						vm.displayError({message: 'Unknown phenotype ' + phenotypeKey});
					});
				}
			}
		};

		$scope.$watch(function () {
			return vm.key;
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

	function SetModalCtrl($uibModalInstance) {
		var mo = this;
		mo.itemsToAdd = [];
		mo.ok = function () {
			$uibModalInstance.close(mo.itemsToAdd !== null ? mo.itemsToAdd[0] : null);
		};
		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	}
}());