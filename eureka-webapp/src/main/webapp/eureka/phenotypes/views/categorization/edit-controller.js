(function () {
	'use strict';

	/**
	 * @ngdoc controller
	 * @name eureka.cohorts.controller:EditCtrl
	 * @description
	 * This is the edit controller for the cohorts section of the application.
	 */

	angular
			.module('eureka.phenotypes')
			.controller('phenotypes.categorization.EditCtrl', EditCtrl)
			.controller('phenotypes.categorization.CancelCreateModalCtrl', CancelCreateModalCtrl)
			.controller('phenotypes.categorization.CancelEditModalCtrl', CancelEditModalCtrl);

	EditCtrl.$inject = ['PhenotypeService', '$stateParams', '$state', '$rootScope', '$scope', '$uibModal'];
	CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
	CancelEditModalCtrl.$inject = ['$uibModalInstance', 'displayName'];

	function EditCtrl(PhenotypeService, $stateParams, $state, $rootScope, $scope, $uibModal) {
		let vm = this;
		vm.nowEditing = $stateParams.key;
		vm.treeMultiDropZoneInitialKeys = [];

		if (vm.nowEditing !== undefined) {
			PhenotypeService.getPhenotype(vm.nowEditing).then(
					function (data) {
						vm.name = data.displayName;
						vm.description = data.description;
						vm.id = data.id;
						vm.type = data.type;
						vm.categoricalType = data.categoricalType;
						vm.userId = data.userId;
						vm.key = data.key;
						vm.userId = data.userId;
						traverseNodes(data.children);
					}, displayGetPhenotypeError);
		}

		let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

		vm.save = function () {
			let categorization = {};
			categorization.displayName = vm.name;
			categorization.description = vm.description;
			categorization.children = [];
			if (vm.treeMultiDropZoneItems) {
				for (let i = 0; i < vm.treeMultiDropZoneItems.length; i++) {
					let item = vm.treeMultiDropZoneItems[i];
					categorization.children.push({
						phenotypeKey: item.name
					});
				}
			}
			categorization.categoricalType = vm.categoricalType;
			if (vm.nowEditing !== undefined) {
				categorization.id = vm.id;
				categorization.key = vm.key;
				categorization.userId = vm.userId;
				categorization.type = vm.type;
				PhenotypeService.updatePhenotype(categorization).then(function () {
					onRouteChangeOff();
					$state.transitionTo('phenotypes');
				}, displayError);
			} else {
				categorization.type = 'CATEGORIZATION';
				categorization.userId = $rootScope.user.info.id;
				PhenotypeService.createPhenotype(categorization).then(function () {
					onRouteChangeOff();
					$state.transitionTo('phenotypes');
				}, displayError);
			}

		};

		vm.cancel = function () {
			$state.transitionTo('phenotypes');
		};

		function displayGetPhenotypeError(message) {
			vm.getPhenotypeErrorMsg = message;
		}

		function displayTreeMultiDropZoneItemsError(message) {
			vm.treeMultiDropZoneItemsErrorMsg = message;
		}

		function displayError(msg) {
			vm.errorMsg = msg;
		}

		function traverseNodes(data) {
			for (let i = 0; i < data.length; i++) {
				vm.treeMultiDropZoneInitialKeys.push(data[i].phenotypeKey);
			}
		}

		function routeChange(event, toState, toParams, fromState, fromParams) {
			if (!event.currentScope.categorizationForm || !event.currentScope.categorizationForm.$dirty) {
				return;
			}
			event.preventDefault();
			if (vm.id) {
				$uibModal.open({
					templateUrl: 'cancelEditModal.html',
					controller: 'phenotypes.categorization.CancelEditModalCtrl',
					controllerAs: 'mo',
					resolve: {
						displayName: function () {
							return vm.name;
						}
					}
				}).result.then(
						function () {
							onRouteChangeOff();
							$state.transitionTo(toState);
						},
						function () {}
				);
			} else {
				$uibModal.open({
					templateUrl: 'cancelCreateModal.html',
					controller: 'phenotypes.categorization.CancelCreateModalCtrl',
					controllerAs: 'mo'
				}).result.then(
						function () {
							onRouteChangeOff();
							$state.transitionTo(toState);
						},
						function () {}
				);
			}
		}
	}

	function CancelCreateModalCtrl($uibModalInstance) {
		var mo = this;
		mo.ok = function () {
			$uibModalInstance.close();
		};

		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

	}

	function CancelEditModalCtrl($uibModalInstance, displayName) {
		var mo = this;
		mo.displayName = displayName;
		mo.ok = function () {
			$uibModalInstance.close();
		};

		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

	}

}());