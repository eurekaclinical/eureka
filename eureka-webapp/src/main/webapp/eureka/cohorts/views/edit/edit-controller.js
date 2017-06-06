(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:EditCtrl
     * @description
     * This is the edit controller for the cohorts section of the application.
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.EditCtrl', EditCtrl)
        .controller('cohorts.CancelCreateModalCtrl', CancelCreateModalCtrl)
        .controller('cohorts.CancelEditModalCtrl', CancelEditModalCtrl);

    EditCtrl.$inject = ['CohortService', '$stateParams', '$state', '$scope', '$uibModal'];
    CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
    CancelEditModalCtrl.$inject = ['$uibModalInstance', 'cohortName'];

    function EditCtrl(CohortService, $stateParams, $state, $scope, $uibModal) {
        let vm = this;
        vm.nowEditing = $stateParams.key;
		vm.treeMultiDropZoneItems = [];
		vm.treeMultiDropZoneInitialKeys = [];

        if (vm.nowEditing) {
            CohortService.getCohort(vm.nowEditing).then(function(data) {
                vm.name = data.name;
                vm.description = data.description;
                vm.id = data.id;
                traverseNodes(data.cohort.node);
            }, displayError);
        }

        let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

        vm.submitCohortForm = function () {
            let cohortObject = {};
            cohortObject.name = vm.name;
            cohortObject.description = vm.description;
            cohortObject.memberList = vm.treeMultiDropZoneItems;
			if (vm.nowEditing) {
                cohortObject.id = vm.id;
                CohortService.updateCohort(cohortObject).then(function() {
                    onRouteChangeOff();
					$state.transitionTo('cohorts');
				}, displayError);
            } else {
				CohortService.createCohort(cohortObject).then(function() {
                    onRouteChangeOff();
					$state.transitionTo('cohorts');
				}, displayError);
            }

        };

        vm.cancelCohortForm = function() {
            $state.transitionTo('cohorts');
        };

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        // This function takes the saved nodes and goes through the tree and plucks all with a valid id. Then adds to criteria which populates the dropzone
        function traverseNodes(data) {
            const reducer = (results, node) => {
                node.id && results.push(node);
                _.without([node.left_node, node.right_node], undefined).reduce(reducer, results);
                return results;
            };
            const fullResults = [data].reduce(reducer, []);
            for (let i = 0; i < fullResults.length; i++) {
                vm.treeMultiDropZoneInitialKeys.push(fullResults[i].name);
            }
        }

        function routeChange(event, toState, toParams, fromState, fromParams) {
            if (!event.currentScope.patCohortForm || !event.currentScope.patCohortForm.$dirty) {
				return;
			}
            event.preventDefault();
            if (vm.id) {
                $uibModal.open({
					templateUrl: 'cancelEditCohortModal.html',
					controller: 'cohorts.CancelEditModalCtrl',
					controllerAs: 'mo',
					resolve: {
						cohortName: function () {
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
					templateUrl: 'cancelCreateCohortModal.html',
					controller: 'cohorts.CancelCreateModalCtrl',
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

    function CancelEditModalCtrl($uibModalInstance, cohortName) {
        var mo = this;
        mo.cohortName = cohortName;
        mo.ok = function () {
            $uibModalInstance.close();
        };

        mo.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }

}());