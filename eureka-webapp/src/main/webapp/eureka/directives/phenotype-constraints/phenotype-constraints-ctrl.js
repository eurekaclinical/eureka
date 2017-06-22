(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('PhenotypeConstraintsCtrl', PhenotypeConstraintsCtrl);

	PhenotypeConstraintsCtrl.$inject = ['PhenotypeService', 'TreeService', '$scope'];

	function PhenotypeConstraintsCtrl(PhenotypeService, TreeService, $scope) {
		let vm = this;

		PhenotypeService.getTimeUnits()
				.then(function (data) {
					vm.minDurationUnitOptions = data;
					vm.maxDurationUnitOptions = data;
					setMinDurationUnits();
					setMaxDurationUnits();
				}, function (msg) {
					if (vm.getTimeUnitsError) {
						vm.onTimeUnitsError({message: msg});
					}
				});

		vm.conceptOrPhenotypeErr = function (message) {
			if (vm.conceptOrPhenotypeError) {
				vm.conceptOrPhenotypeError({message: message});
			}
		};

		$scope.$watch(function () {
			return vm.minDurationUnits;
		}, function (newValue, oldValue) {
			if (!newValue) {
				setMinDurationUnits();
			}
		});

		$scope.$watch(function () {
			return vm.maxDurationUnits;
		}, function (newValue, oldValue) {
			if (!newValue) {
				setMaxDurationUnits();
			}
		});

		$scope.$watch(function () {
			return vm.conceptOrPhenotype;
		}, function (newValue, oldValue) {
			if (newValue && newValue.name) {
				TreeService.getTreeNode(newValue.name).then(function (data) {
					vm.conceptOrPhenotypeProperties = [];
					angular.forEach(data.properties, function (propertyName) {
						vm.conceptOrPhenotypeProperties.push({name: propertyName});
					});
					if (!vm.propertyName && vm.conceptOrPhenotypeProperties.length > 0) {
						vm.propertyName = vm.conceptOrPhenotypeProperties[0].name;
					}
				}, function () {

				});
			}
		});

		function setMinDurationUnits() {
			if (vm.minDurationUnitOptions) {
				angular.forEach(vm.minDurationUnitOptions, function (timeunitOption) {
					if (timeunitOption.default) {
						if (!vm.minDurationUnits) {
							vm.minDurationUnits = timeunitOption.id;
						}
						if (!vm.maxDurationUnits) {
							vm.maxDurationUnits = timeunitOption.id;
						}
					}
				});
			}
		}

		function setMaxDurationUnits() {
			if (vm.maxDurationUnitOptions) {
				angular.forEach(vm.maxDurationUnitOptions, function (timeunitOption) {
					if (timeunitOption.default) {
						if (!vm.maxDurationUnits) {
							vm.maxDurationUnits = timeunitOption.id;
						}
					}
				});
			}
		}
	}
}());