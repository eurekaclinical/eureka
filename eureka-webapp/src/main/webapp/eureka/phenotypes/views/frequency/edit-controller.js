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
				.then(function (data) {
					vm.minDistanceBetweenUnitOptions = data;
					vm.maxDistanceBetweenUnitOptions = data;
					angular.forEach(data, function (timeunitOption) {
						if (timeunitOption.default) {
							if (!vm.minDistanceBetweenUnits) {
								vm.minDistanceBetweenUnits = timeunitOption.id;
							}
							if (!vm.maxDistanceBetweenUnits) {
								vm.maxDistanceBetweenUnits = timeunitOption.id;
							}
						}
					});
				}, function (msg) {
					vm.timeUnitsErrorMsg = msg;
				});

		PhenotypeService.getFrequencyTypes()
				.then(function (data) {
					vm.frequencyTypeOptions = data;
					angular.forEach(data, function (frequencyTypeOption) {
						if (frequencyTypeOption.default) {
							if (!vm.frequencyType) {
								vm.frequencyType = frequencyTypeOption.id;
							}
						}
					});
				}, function (msg) {
					vm.frequencyTypesErrorMsg = msg;
				});

		if (vm.nowEditing !== undefined) {
			PhenotypeService.getPhenotype(vm.nowEditing).then(function (data) {
				vm.name = data.displayName;
				vm.description = data.description;
				vm.id = data.id;
				vm.type = data.type;
				vm.userId = data.userId;
				vm.conceptOrPhenotype = {
					name: data.phenotype.phenotypeKey,
					displayName: data.phenotype.phenotypeDisplayName,
					type: data.phenotype.type
				};
				if (data.phenotype.hasDuration) {
					vm.minDuration = data.phenotype.minDuration;
					if (data.phenotype.minDurationUnits) {
						vm.minDurationUnits = data.phenotype.minDurationUnits;
					}
					vm.maxDuration = data.phenotype.maxDuration;
					if (data.phenotype.maxDurationUnits) {
						vm.maxDurationUnits = data.phenotype.maxDurationUnits;
					}
				}
				if (data.isWithin) {
					vm.minDistanceBetween = data.withinAtLeast;
					if (data.withinAtLeastUnits) {
						vm.minDistanceBetweenUnits = data.withinAtLeastUnits;
					}
					vm.maxDistanceBetween = data.withinAtMost;
					if (data.withinAtMostUnits) {
						vm.maxDistanceBetweenUnits = data.withinAtMostUnits;
					}
				}
				vm.threshold = data.atLeast;
				vm.isConsecutive = data.isConsecutive;
				vm.frequencyType = data.frequencyType;
				if (data.phenotype.hasPropertyConstraint) {
					vm.propertyName = data.phenotype.property;
					vm.propertyValue = data.phenotype.propertyValue;
				}
			}, displayGetPhenotypeError);
		}

		let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

		vm.save = function () {
			var frequency = {
				displayName: vm.name,
				description: vm.description,
				id: vm.id,
				phenotype: {
					phenotypeKey: vm.conceptOrPhenotype.name,
					phenotypeDisplayName: vm.conceptOrPhenotype.displayName,
					type: vm.conceptOrPhenotype.type
				},
				atLeast: vm.threshold,
				isConsecutive: vm.conceptOrPhenotype === 'VALUE_THRESHOLD' ? vm.isConsecutive : false,
				frequencyType: vm.frequencyType,
				type: 'FREQUENCY'
			};
			if (vm.userId) {
				frequency.userId = vm.userId;
			} else {
				frequency.userId = $rootScope.user.info.id;
			}
			if (vm.minDuration || vm.maxDuration) {
				frequency.phenotype.hasDuration = true;
				frequency.phenotype.minDuration = vm.minDuration;
				frequency.phenotype.minDurationUnits = vm.minDurationUnits;
				frequency.phenotype.maxDuration = vm.maxDuration;
				frequency.phenotype.maxDurationUnits = vm.maxDurationUnits;
			} else {
				frequency.phenotype.hasDuration = false;
			}
			if (vm.minDistanceBetween || vm.maxDistanceBetween) {
				frequency.isWithin = true;
				frequency.withinAtLeast = vm.minDistanceBetween;
				frequency.withinAtLeastUnits = vm.minDistanceBetweenUnits;
				frequency.withinAtMost = vm.maxDistanceBetween;
				frequency.withinAtMostUnits = vm.maxDistanceBetweenUnits;
			} else {
				frequency.isWithin = false;
			}
			if (vm.propertyValue) {
				frequency.phenotype.hasPropertyConstraint = true;
				frequency.phenotype.property = vm.propertyName;
				frequency.phenotype.propertyValue = vm.propertyValue;
			} else {
				frequency.phenotype.hasPropertyConstraint = false;
			}
			if (vm.nowEditing) {
				PhenotypeService.updatePhenotype(frequency).then(
						function () {
							onRouteChangeOff();
							returnToPhenotypesPage();
						}, displaySaveError);
			} else {
				PhenotypeService.createPhenotype(frequency).then(
						function () {
							onRouteChangeOff();
							returnToPhenotypesPage();
						}, displaySaveError);
			}
		};

		vm.cancel = function () {
			returnToPhenotypesPage();
		};

		function returnToPhenotypesPage() {
			$state.transitionTo('phenotypes');
		}

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