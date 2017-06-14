(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('PhenotypeConstraintsCtrl', PhenotypeConstraintsCtrl);

	PhenotypeConstraintsCtrl.$inject = ['PhenotypeService', 'TreeService', '$scope'];

	function PhenotypeConstraintsCtrl(PhenotypeService, TreeService, $scope) {
		let vm = this;

		PhenotypeService.getTimeUnits()
				.then(function (response) {
					vm.minDurationUnitOptions = response.data;
					vm.maxDurationUnitOptions = response.data;
					angular.forEach(response.data, function (timeunitOption) {
						if (timeunitOption.default) {
							if (!vm.minDurationUnits) {
								vm.minDurationUnits = timeunitOption;
							}
							if (!vm.maxDurationUnits) {
								vm.maxDurationUnits = timeunitOption;
							}
						}
					});
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
			return vm.conceptOrPhenotype;
		}, function (newValue, oldValue) {
			if (newValue && newValue.name) {
				TreeService.getTreeNode(newValue.name).then(function (response) {
					vm.conceptOrPhenotypeProperties = response.data.properties;
				}, function () {

				});
			}
		});

	}
}());