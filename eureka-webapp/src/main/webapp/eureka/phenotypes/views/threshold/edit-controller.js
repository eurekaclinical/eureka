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
			.controller('phenotypes.threshold.EditCtrl', EditCtrl)
			.controller('phenotypes.threshold.CancelCreateModalCtrl', CancelCreateModalCtrl)
			.controller('phenotypes.threshold.CancelEditModalCtrl', CancelEditModalCtrl);

	EditCtrl.$inject = ['PhenotypeService', '$stateParams', '$state', '$rootScope', '$scope', '$uibModal'];
	CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
	CancelEditModalCtrl.$inject = ['$uibModalInstance', 'displayName'];

	function EditCtrl(PhenotypeService, $stateParams, $state, $rootScope, $scope, $uibModal) {
		let vm = this;
		vm.nowEditing = $stateParams.key;
		vm.thresholdObject = {};
		vm.conceptOrPhenotypes = [];

		vm.setTimeUnitDefault = function (valueThreshold) {
			angular.forEach(vm.distanceBetweenUnitOptions, function (timeunitOption) {
				if (timeunitOption.default) {
					if (!valueThreshold.withinAtLeastUnit) {
						valueThreshold.withinAtLeastUnit = timeunitOption.id;
					}
					if (!valueThreshold.withinAtMostUnit) {
						valueThreshold.withinAtMostUnit = timeunitOption.id;
					}
				}
			});
		};

		PhenotypeService.getTimeUnits()
				.then(function (data) {
					vm.distanceBetweenUnitOptions = data;
					if (vm.thresholdObject.valueThresholds) {
						for (let i = 0; i < vm.thresholdObject.valueThresholds.length; i++) {
							vm.setTimeUnitDefault(vm.thresholdObject.valueThresholds[i]);
						}
					}
				}, function (msg) {
					vm.timeUnitsErrorMsg = msg;
				});

		vm.setValueComparatorDefault = function (valueThreshold) {
			if (vm.lowerCompOptions && !valueThreshold.lowerComp) {
				valueThreshold.lowerComp = vm.lowerCompOptions[0].id;
			}
			if (vm.upperCompOptions && !valueThreshold.upperComp) {
				valueThreshold.upperComp = vm.upperCompOptions[0].id;
			}
		};

		PhenotypeService.getValueComparators()
				.then(function (data) {
					vm.upperCompOptions = [];
					vm.lowerCompOptions = [];
					angular.forEach(data, function (compOption) {
						if (compOption.threshold !== 'UPPER_ONLY') {
							vm.lowerCompOptions.push(compOption);
						}
						if (compOption.threshold !== 'LOWER_ONLY') {
							vm.upperCompOptions.push(compOption);
						}
					});
					if (vm.thresholdObject.valueThresholds) {
						for (let i = 0; i < vm.thresholdObject.valueThresholds.length; i++) {
							vm.setValueComparatorDefault(vm.thresholdObject.valueThresholds[i]);
						}
					}
				}, function (msg) {
					vm.valueComparatorsErrorMsg = msg;
				});

		PhenotypeService.getThresholdsOperators()
				.then(function (data) {
					vm.thresholdTypeOptions = data;
					vm.thresholdObject.thresholdsOperator = vm.thresholdTypeOptions[0].id;
				}, function (msg) {
					vm.thresholdsOperatorsErrorMsg = msg;
				});

		vm.setRelationOperatorDefault = function (valueThreshold) {
			angular.forEach(vm.relationOperatorOptions, function (relationOperatorOption) {
				if (relationOperatorOption.default) {
					if (!valueThreshold.relationOperator) {
						valueThreshold.relationOperator = relationOperatorOption.id;
					}
				}
			});
		};

		PhenotypeService.getRelationOperators()
				.then(function (data) {
					vm.relationOperatorOptions = data;
					angular.forEach(vm.thresholdObject.valueThresholds, function (valueThreshold) {
						vm.setRelationOperatorDefault(valueThreshold);
					});

				}, function (msg) {
					vm.relationOperatorsErrorMsg = msg;
				});

		vm.addThreshold = function () {
			if (!vm.thresholdObject.valueThresholds) {
				vm.thresholdObject.valueThresholds = [];
			}
			vm.thresholdObject.valueThresholds.push({});
		};

		vm.removeThreshold = function (index) {
			vm.thresholdObject.valueThresholds.splice(index, 1);
			vm.conceptsOrPhenotypes.splice(index, 1);
			vm.contextItems.splice(index, 1);
		};

		if (vm.nowEditing !== undefined) {
			PhenotypeService.getPhenotype(vm.nowEditing).then(function (data) {
				vm.thresholdObject = data;
				let initialConceptOrPhenotypeKeys = [];
				let initialContextKeys = [];
				for (let i = 0; i < vm.thresholdObject.valueThresholds.length; i++) {
					let valueThreshold = vm.thresholdObject.valueThresholds[i];
					let phenotype = valueThreshold.phenotype;
					initialConceptOrPhenotypeKeys.push(phenotype.phenotypeKey);
					if (!phenotype.hasDuration) {
						phenotype.minDuration = null;
						phenotype.maxDuration = null;
					}
					if (!phenotype.hasPropertyConstraint) {
						phenotype.propertyValue = null;
					}
					let relatedPhenotypeKeys = [];
					if (valueThreshold.relatedPhenotypes) {
						for (let j = 0; j < valueThreshold.relatedPhenotypes.length; j++) {
							relatedPhenotypeKeys.push(valueThreshold.relatedPhenotypes[j].phenotypeKey);
						}
					}
					initialContextKeys.push(relatedPhenotypeKeys);
				}
				vm.initialConceptOrPhenotypeKeys = initialConceptOrPhenotypeKeys;
				vm.initialContextKeys = initialContextKeys;
			}, displayGetPhenotypeError);
		} else {
			vm.thresholdObject.type = 'VALUE_THRESHOLD';
			vm.thresholdObject.userId = $rootScope.user.info.id;
			vm.addThreshold();
		}

		let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

		vm.save = function () {
			for (let i = 0; i < vm.thresholdObject.valueThresholds.length; i++) {
				let valueThreshold = vm.thresholdObject.valueThresholds[i];
				if (!valueThreshold.phenotype) {
					valueThreshold.phenotype = {};
				}
				let phenotype = valueThreshold.phenotype;
				phenotype.phenotypeKey = vm.conceptOrPhenotypes[i].name;
				phenotype.phenotypeDisplayName = vm.conceptOrPhenotypes[i].displayName;
				phenotype.type = vm.conceptOrPhenotypes[i].type;
				if (phenotype.minDuration || phenotype.maxDuration) {
					phenotype.hasDuration = true;
				} else {
					phenotype.hasDuration = false;
				}
				if (phenotype.propertyValue) {
					phenotype.hasPropertyConstraint = true;
				} else {
					phenotype.hasPropertyConstraint = false;
				}
				if (vm.contextItems) {
					let relatedPhenotypes = vm.contextItems[i];
					if (relatedPhenotypes) {
						valueThreshold.relatedPhenotypes = [];
						for (let j = 0; j < relatedPhenotypes.length; j++) {
							valueThreshold.relatedPhenotypes.push({
								phenotypeKey: relatedPhenotypes[j].name,
								phenotypeDisplayName: relatedPhenotypes[j].displayName,
								type: relatedPhenotypes[j].type
							});
						}
					}
				}
			}
			if (vm.nowEditing) {
				PhenotypeService.updatePhenotype(vm.thresholdObject).then(
						function () {
							onRouteChangeOff();
							returnToPhenotypesPage();
						}, displaySaveError);
			} else {
				PhenotypeService.createPhenotype(vm.thresholdObject).then(
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

		vm.displayConceptOrPhenotypeError = function (message) {
			vm.conceptOrPhenotypeErrorMsg = message;
		};

		function displayGetPhenotypeError(message) {
			vm.getPhenotypeErrorMsg = message;
		}

		function displaySaveError(message) {
			vm.saveErrorMsg = message;
		}

		function routeChange(event, toState, toParams, fromState, fromParams) {
			if (!event.currentScope.thresholdForm || !event.currentScope.thresholdForm.$dirty) {
				return;
			}
			event.preventDefault();
			if (vm.id) {
				$uibModal.open({
					templateUrl: 'cancelEditModal.html',
					controller: 'phenotypes.threshold.CancelEditModalCtrl',
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
					controller: 'phenotypes.threshold.CancelCreateModalCtrl',
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