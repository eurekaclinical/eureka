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
			.controller('phenotypes.frequency.EditCtrl', EditCtrl)
			.controller('phenotypes.frequency.CancelCreateModalCtrl', CancelCreateModalCtrl)
			.controller('phenotypes.frequency.CancelEditModalCtrl', CancelEditModalCtrl);

	EditCtrl.$inject = ['PhenotypeService', '$stateParams', '$state', '$rootScope', '$scope', '$uibModal'];
	CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
	CancelEditModalCtrl.$inject = ['$uibModalInstance', 'displayName'];

	function EditCtrl(PhenotypeService, $stateParams, $state, $rootScope, $scope, $uibModal) {
		let vm = this;
		vm.nowEditing = $stateParams.key;
		vm.treeMultiDropZoneItems = [];
		vm.treeMultiDropZoneInitialKeys = [];

		PhenotypeService.getTimeUnits()
				.then(function (response) {
					vm.minDistanceBetweenUnitOptions = response.data;
					vm.maxDistanceBetweenUnitOptions = response.data;
					angular.forEach(response.data, function (timeunitOption) {
						if (timeunitOption.default) {
							if (!vm.minDistanceBetweenUnits) {
								vm.minDistanceBetweenUnits = timeunitOption;
							}
							if (!vm.maxDistanceBetweenUnits) {
								vm.maxDistanceBetweenUnits = timeunitOption;
							}
						}
					});
				}, function (msg) {
					if (vm.getTimeUnitsError) {
						vm.onTimeUnitsError({message: msg});
					}
				});

		PhenotypeService.getFrequencyTypes()
				.then(function (response) {
					vm.frequencyTypeOptions = response.data;
					angular.forEach(response.data, function (frequencyTypeOption) {
						if (frequencyTypeOption.default) {
							if (!vm.frequencyType) {
								vm.frequencyType = frequencyTypeOption;
							}
						}
					});
				}, function (msg) {
					if (vm.getTimeUnitsError) {
						vm.onTimeUnitsError({message: msg});
					}
				});

		if (vm.nowEditing !== undefined) {
			PhenotypeService.getPhenotype(vm.nowEditing).then(function (data) {
				vm.name = data.displayName;
				vm.description = data.description;
				vm.id = data.id;
				vm.type = data.type;
				vm.userId = data.userId;
				vm.phenotypeOrConcept = {
					name: data.phenotypeOrConcept.phenotypeKey,
					displayName: data.phenotypeOrConcept.phenotypeDisplayName,
					type: data.phenotypeOrConcept.type
				}
				vm.minDuration = data.phenotypeOrConcept.minDuration;
				vm.minDurationUnits = data.phenotypeOrConcept.minDurationUnits;
				vm.maxDuration = data.phenotypeOrConcept.maxDuration;
				vm.maxDurationUnits = data.phenotypeOrConcept.maxDurationUnits;
				vm.minDistanceBetween = data.withinAtLeast;
				vm.minDistanceBetweenUnits = data.withinAtLeastUnits;
				vm.maxDistanceBetween = data.withinAtMost;
				vm.maxDistanceBetweenUnits = data.withinAtMostUnits;
				vm.threshold = data.atLeast;
				vm.isConsecutive = data.isConsecutive;
				vm.frequencyType = data.frequencyType;
				vm.propertyName = data.phenotypeOrConcept.property;
				vm.propertyValue = data.phenotypeOrConcept.propertyValue;
			}, displayGetPhenotypeError);
		}

		let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

		vm.save = function () {

		};

		vm.cancel = function () {
			$state.transitionTo('phenotypes');
		};

		function displayConceptOrPhenotypeError(message) {
			vm.conceptOrPhenotypeErrorMsg = message;
		}

		function displayTimeUnitsError(message) {
			vm.timeUnitsErrorMsg = message;
		}

		function displayGetPhenotypeError(message) {
			vm.getPhenotypeErrorMsg = message;
		}

		function displaySaveError(message) {
			vm.saveErrorMsg = message;
		}

		function routeChange(event, toState, toParams, fromState, fromParams) {
			if (!event.currentScope.frequencyForm || !event.currentScope.frequencyForm.$dirty) {
				return;
			}
			event.preventDefault();
			if (vm.id) {
				$uibModal.open({
					templateUrl: 'cancelEditModal.html',
					controller: 'phenotypes.frequency.CancelEditModalCtrl',
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
					controller: 'phenotypes.frequency.CancelCreateModalCtrl',
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