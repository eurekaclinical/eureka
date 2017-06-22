(function () {


	/**
	 * @ngdoc controller
	 * @name eureka.phenotypes.controller:MainCtrl
	 * @description
	 * This is the main controller for the phenotypes section of the application.
	 * @requires $scope
	 * @requires $location
	 * @requires eureka.phenotypes.PhenotypeService
	 */

	angular
			.module('eureka.phenotypes')
			.controller('phenotypes.ModalCtrl', ModalCtrl)
			.controller('phenotypes.MainCtrl', MainCtrl);



	ModalCtrl.$inject = ['$uibModalInstance', 'displayName'];
	MainCtrl.$inject = ['$state', 'PhenotypeService', 'NgTableParams', '$uibModal'];

	function ModalCtrl($uibModalInstance, displayName) {
		var mo = this;
		mo.displayName = displayName;
		mo.ok = function () {
			$uibModalInstance.close();
		};

		mo.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};

	}

	function MainCtrl($state, PhenotypeService, NgTableParams, $uibModal) {

		var vm = this;
		var copyData = [];

		vm.currentSelectedItem = {};
		vm.selected = [];

		function success(data) {
			vm.props = data;
			copyData = data;
			// NG Table
			vm.tableParams = new NgTableParams({}, {dataset: copyData});
		}

		function displayDeleteError(msg) {
			vm.deleteErrorMsg = msg;
		}

		function displayLoadError(msg) {
			vm.loadErrorMsg = msg;
		}

		vm.deletePhenotype = function (phenotypeToDelete) {

			$uibModal.open({
				templateUrl: 'myModal.html',
				controller: 'phenotypes.ModalCtrl',
				controllerAs: 'mo',
				resolve: {
					displayName: function () {
						return phenotypeToDelete.displayName;
					}
				}
			}).result.then(
					function () {
						removePhenotype(phenotypeToDelete);
					},
					function (arg) {
						console.log(arg);
					}
			);
		};

		function deleteSuccess() {
			vm.tableParams.filter({});
			for (var i = 0; i < vm.props.length; i++) {
				if (vm.props[i].displayName === vm.currentSelectedItem.displayName) {
					vm.props.splice(i, 1);
					break;
				}
			}
			vm.tableParams.reload();
		}

		function removePhenotype(phenotype) {
			vm.currentSelectedItem = phenotype;
			vm.deferred = PhenotypeService.removePhenotype(phenotype.id).then(deleteSuccess, displayDeleteError);
		}

		PhenotypeService.getPhenotypeRoot().then(success, displayLoadError);

		vm.messages = PhenotypeService.getPhenotypeMessages();
		vm.messagesByName = {};
		angular.forEach(vm.messages, function(message) {
			vm.messagesByName[message.name] = message;
		});
	}
})();
