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
			.controller('phenotypes.sequence.EditCtrl', EditCtrl)
			.controller('phenotypes.sequence.CancelCreateModalCtrl', CancelCreateModalCtrl)
			.controller('phenotypes.sequence.CancelEditModalCtrl', CancelEditModalCtrl);

	EditCtrl.$inject = ['PhenotypeService', '$stateParams', '$state', '$rootScope', '$scope', '$uibModal'];
	CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
	CancelEditModalCtrl.$inject = ['$uibModalInstance', 'displayName'];

	function EditCtrl(PhenotypeService, $stateParams, $state, $rootScope, $scope, $uibModal) {
		let vm = this;
		vm.nowEditing = $stateParams.key;
		vm.sequenceObject = {};
		vm.allPhenotypes = [];
		vm.sequentialPhenotypes = [];

		vm.setTimeUnitDefaultForRelated = function (phenotype) {
			angular.forEach(vm.timeUnitOptions, function (timeUnitOption) {
				if (timeUnitOption.default) {
					if (phenotype.phenotypeField) {
						if (!phenotype.phenotypeField.minDurationUnits) {
							phenotype.phenotypeField.minDurationUnits = timeUnitOption.id;
						}
						if (!phenotype.phenotypeField.maxDurationUnits) {
							phenotype.phenotypeField.maxDurationUnits = timeUnitOption.id;
						}
					}
					if (!phenotype.relationMinUnits) {
						phenotype.relationMinUnits = timeUnitOption.id;
					}
					if (!phenotype.relationMaxUnits) {
						phenotype.relationMaxUnits = timeUnitOption.id;
					}
				}
			});
		};

		vm.setTimeUnitDefaultForPrimary = function () {
			let phenotype = vm.sequenceObject.primaryPhenotype;
			if (phenotype && phenotype.phenotypeField) {
				angular.forEach(vm.timeUnitOptions, function (timeUnitOption) {
					if (timeUnitOption.default) {
						if (!phenotype.phenotypeField.minDurationUnits) {
							phenotype.phenotypeField.minDurationUnits = timeUnitOption.id;
						}
						if (!phenotype.phenotypeField.maxDurationUnits) {
							phenotype.phenotypeField.maxDurationUnits = timeUnitOption.id;
						}
					}
				});
			}
		};

		PhenotypeService.getTimeUnits()
				.then(function (data) {
					vm.timeUnitOptions = data;
					vm.setTimeUnitDefaultForPrimary();
					if (vm.sequenceObject.relatedPhenotypes) {
						for (let i = 0; i < vm.sequenceObject.relatedPhenotypes.length; i++) {
							vm.setTimeUnitDefaultForRelated(vm.sequenceObject.relatedPhenotypes[i]);
						}
					}
				}, function (msg) {
					vm.timeUnitsErrorMsg = msg;
				});

		vm.setRelationOperatorDefault = function (relatedPhenotype) {
			angular.forEach(vm.sequenceRelationOptions, function (relationOperatorOption) {
				if (relationOperatorOption.default) {
					if (!relatedPhenotype.relationOperator) {
						relatedPhenotype.relationOperator = relationOperatorOption.id;
					}
				}
			});
		};

		PhenotypeService.getRelationOperators()
				.then(function (data) {
					vm.sequenceRelationOptions = data;
					angular.forEach(vm.sequenceObject.relatedPhenotypes, function (relatedPhenotype) {
						vm.setRelationOperatorDefault(relatedPhenotype);
					});

				}, function (msg) {
					vm.relationOperatorsErrorMsg = msg;
				});

		vm.addRelated = function () {
			if (!vm.sequenceObject.relatedPhenotypes) {
				vm.sequenceObject.relatedPhenotypes = [];
			}
			vm.sequenceObject.relatedPhenotypes.push({});
			if (!vm.relatedPhenotypes) {
				vm.relatedPhenotypes = [];
			}
		};

		vm.removeRelated = function (index) {
			vm.sequenceObject.relatedPhenotypes.splice(index, 1);
			if (vm.relatedPhenotypes && index < vm.relatedPhenotypes.length) {
				vm.relatedPhenotypes.splice(index, 1);
			}
		};

		if (vm.nowEditing !== undefined) {
			PhenotypeService.getPhenotype(vm.nowEditing).then(function (data) {
				vm.sequenceObject = data;
				let primaryPhenotype = vm.sequenceObject.primaryPhenotype;
				if (primaryPhenotype) {
					if (!primaryPhenotype.hasDuration) {
						primaryPhenotype.minDuration = null;
						primaryPhenotype.maxDuration = null;
					}
				}
				if (vm.sequenceObject.relatedPhenotypes) {
					for (let i = 0; i < vm.sequenceObject.relatedPhenotypes.length; i++) {
						let relatedPhenotype = vm.sequenceObject.relatedPhenotypes[i];
						if (relatedPhenotype.phenotypeField && !relatedPhenotype.phenotypeField.hasPropertyConstraint) {
							relatedPhenotype.phenotypeField.propertyValue = null;
						}
					}
					vm.relatedPhenotypes = [];
					for (let i = 0; i < vm.sequenceObject.relatedPhenotypes.length; i++) {
						let relatedPhenotype = vm.sequenceObject.relatedPhenotypes[i];
						if (relatedPhenotype) {
							vm.relatedPhenotypes.push({
								name: relatedPhenotype.phenotypeField.phenotypeKey,
								displayName: relatedPhenotype.phenotypeField.phenotypeDisplayName,
								type: relatedPhenotype.phenotypeField.type
							});
						}
					}
				}

				vm.setTimeUnitDefaultForPrimary();
			}, displayGetPhenotypeError);
		} else {
			vm.sequenceObject.type = 'SEQUENCE';
			vm.sequenceObject.userId = $rootScope.user.info.id;
			vm.sequenceObject.primaryPhenotype = {};
			vm.setTimeUnitDefaultForPrimary();
		}

		let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

		vm.save = function () {
			let primaryPhenotype = vm.sequenceObject.primaryPhenotype;
			if (primaryPhenotype) {
				primaryPhenotype.phenotypeKey = vm.primaryPhenotype.name;
				primaryPhenotype.phenotypeDisplayName = vm.primaryPhenotype.displayName;
				primaryPhenotype.type = vm.primaryPhenotype.type;
				if (primaryPhenotype.minDuration || primaryPhenotype.maxDuration) {
					primaryPhenotype.hasDuration = true;
				} else {
					primaryPhenotype.hasDuration = false;
				}
				if (primaryPhenotype.propertyValue) {
					primaryPhenotype.hasPropertyConstraint = true;
				} else {
					primaryPhenotype.hasPropertyConstraint = false;
				}
			}
			if (vm.sequenceObject.relatedPhenotypes) {
				for (let i = 0; i < vm.sequenceObject.relatedPhenotypes.length; i++) {
					let relatedPhenotype = vm.sequenceObject.relatedPhenotypes[i];
					if (relatedPhenotype.phenotypeField) {
						if (vm.relatedPhenotypes[i]) {
							relatedPhenotype.phenotypeField.phenotypeKey = vm.relatedPhenotypes[i].name;
							relatedPhenotype.phenotypeField.phenotypeDisplayName = vm.relatedPhenotypes[i].displayName;
							relatedPhenotype.phenotypeField.type = vm.relatedPhenotypes[i].type;
						}
						if (relatedPhenotype.phenotypeField.minDuration || relatedPhenotype.phenotypeField.maxDuration) {
							relatedPhenotype.phenotypeField.hasDuration = true;
						} else {
							relatedPhenotype.phenotypeField.hasDuration = false;
						}
						if (relatedPhenotype.phenotypeField.propertyValue) {
							relatedPhenotype.phenotypeField.hasPropertyConstraint = true;
						} else {
							relatedPhenotype.phenotypeField.hasPropertyConstraint = false;
						}
					}
					if (vm.sequentialPhenotypes[i]) {
						relatedPhenotype.sequentialPhenotype = vm.sequentialPhenotypes[i].name;
						relatedPhenotype.sequentialPhenotypeSource = vm.sequentialPhenotypes[i].source;
					}
				}
			}
			if (vm.nowEditing) {
				PhenotypeService.updatePhenotype(vm.sequenceObject).then(
						function () {
							onRouteChangeOff();
							returnToPhenotypesPage();
						}, displaySaveError);
			} else {
				PhenotypeService.createPhenotype(vm.sequenceObject).then(
						function () {
							onRouteChangeOff();
							returnToPhenotypesPage();
						}, displaySaveError);
			}
		};

		$scope.$watch(function () {
			return vm.primaryPhenotype;
		}, function (newValue, oldValue) {
			if (newValue) {
				vm.allPhenotypes[0] = {
					source: 1,
					name: newValue.name,
					displayName: newValue.displayName + ' (Primary)'
				};
			} else {
				vm.allPhenotypes[0] = null;
			}
			refreshSequentialPhenotypes();
		});

		$scope.$watchCollection(function () {
			return vm.relatedPhenotypes;
		}, function (newValue, oldValue) {
			if (newValue) {
				vm.allPhenotypes.length = 1;
				for (let i = 0; i < newValue.length; i++) {
					let v = newValue[i];
					if (v) {
						vm.allPhenotypes[i + 1] = {
							source: i + 2,
							name: v.name,
							displayName: sequentialPhenotypeDisplayName(i + 2)
						};
					} else {
						vm.allPhenotypes[i + 1] = null;
					}
				}
				refreshSequentialPhenotypes();
			}
		});

		vm.cancel = function () {
			returnToPhenotypesPage();
		};

		function refreshSequentialPhenotypes() {
			if (vm.relatedPhenotypes) {
				vm.sequentialPhenotypes = [];
				for (let i = 0; i < vm.sequenceObject.relatedPhenotypes.length; i++) {
					let relPhenotype = vm.sequenceObject.relatedPhenotypes[i];
					if (relPhenotype && relPhenotype.sequentialPhenotypeSource) {
						vm.sequentialPhenotypes[i] = vm.allPhenotypes[relPhenotype.sequentialPhenotypeSource - 1];
					} else {
						vm.sequentialPhenotypes[i] = vm.allPhenotypes[0];
					}
				}
			}
		}

		function sequentialPhenotypeDisplayName(sequentialPhenotypeSource) {
			if (sequentialPhenotypeSource === 1) {
				if (vm.primaryPhenotype) {
					return vm.primaryPhenotype.displayName + ' (Primary)';
				} else {
					if (vm.sequenceObject && vm.sequenceObject.primaryPhenotype) {
						return vm.sequenceObject.primaryPhenotype.phenotypeKey + ' (Primary)';
					} else {
						return '(Primary)';
					}
				}
			} else {
				if (vm.relatedPhenotypes && sequentialPhenotypeSource <= vm.relatedPhenotypes.length + 1) {
					return vm.relatedPhenotypes[sequentialPhenotypeSource - 2].displayName + ' (Related ' + (sequentialPhenotypeSource - 1) + ')';
				} else {
					if (vm.sequenceObject && vm.sequenceObject.relatedPhenotypes && sequentialPhenotypeSource <= vm.sequenceObject.relatedPhenotypes.length + 1) {
						return vm.sequenceObject.relatedPhenotypes[sequentialPhenotypeSource - 2].phenotypeKey + ' (Related ' + (sequentialPhenotypeSource - 1) + ')';
					} else {
						return '(Related ' + (sequentialPhenotypeSource - 1) + ')';
					}

				}
			}
		}

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
			if (!event.currentScope.sequenceForm || !event.currentScope.sequenceForm.$dirty) {
				return;
			}
			event.preventDefault();
			if (vm.id) {
				$uibModal.open({
					templateUrl: 'cancelEditModal.html',
					controller: 'phenotypes.sequence.CancelEditModalCtrl',
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
					controller: 'phenotypes.sequence.CancelCreateModalCtrl',
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