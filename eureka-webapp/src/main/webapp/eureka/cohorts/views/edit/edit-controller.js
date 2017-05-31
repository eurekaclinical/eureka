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
        .controller('cohorts.AddCriteriaModalCtrl', AddCriteriaModalCtrl)
        .controller('cohorts.DeleteCriterionModalCtrl', DeleteCriterionModalCtrl)
        .controller('cohorts.CancelCreateModalCtrl', CancelCreateModalCtrl)
        .controller('cohorts.CancelEditModalCtrl', CancelEditModalCtrl);

    EditCtrl.$inject = ['CohortService', '$stateParams', '$state', '$scope', '$rootScope', '$uibModal'];
    AddCriteriaModalCtrl.$inject = ['$uibModalInstance', 'criteria'];
    DeleteCriterionModalCtrl.$inject = ['$uibModalInstance', 'criterionName'];
    CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
    CancelEditModalCtrl.$inject = ['$uibModalInstance', 'cohortName'];

    function EditCtrl(CohortService, $stateParams, $state, $scope, $rootScope, $uibModal) {
        let vm = this;
        let userId = $rootScope.user.info.id;
        vm.nowEditing = $stateParams.key;

        if (vm.nowEditing) {
            CohortService.getCohort(vm.nowEditing).then(function(data) {
                vm.name = data.name;
                vm.description = data.description;
                vm.id = data.id;
                traverseNodes(data.cohort.node);
            }, displayError);
        }
        vm.criteria = [];
        vm.memberList = [];

        let onRouteChangeOff = $rootScope.$on('$locationChangeStart', routeChange);

        vm.addCriteria = function () {
            $uibModal.open({
                templateUrl: 'addCriteriaModal.html',
                controller: 'cohorts.AddCriteriaModalCtrl',
                controllerAs: 'mo',
                resolve: {
                    criteria: function () {
                        return vm.memberList;
                    }
                }
            }).result.then(
                function () {
                  for (let i = 0; i < vm.memberList.length; i++) {
                      let editMember = vm.memberList[i];
                      let found = false;
                      for (let j = 0; j < vm.criteria.length; j++) {
                          if (editMember.key === vm.criteria[j].key) {
                              found = true;
                              break;
                          }
                      }
                      if (!found) {
                          vm.criteria.push(editMember);
                      }
                  }
                  vm.memberList = [];
                },
                function () {
                }
            )
        }

		vm.removeCriterion = function(criterion) {
			$uibModal.open({
                templateUrl: 'deleteCriterionModal.html',
                controller: 'cohorts.DeleteCriterionModalCtrl',
                controllerAs: 'mo',
                resolve: {
                    criterionName: function () {
                        return criterion.displayName;
                    }
                }
            }).result.then(
                function () {
                    let indexOfCriterion = vm.criteria.indexOf(criterion);
                    if (indexOfCriterion > -1) {
                        vm.criteria.splice(indexOfCriterion, 1);
                    }
                },
                function (arg) {
                }
            );
		}

        vm.submitCohortForm = function () {
            let cohortObject = {};
            cohortObject.name = vm.name;
            cohortObject.description = vm.description;
            cohortObject.memberList = vm.criteria;
			if (vm.nowEditing) {
                cohortObject.id = vm.id;
                CohortService.updateCohort(cohortObject).then(data => {
					$state.transitionTo('cohorts');
				}, displayError);
            } else {
				CohortService.createCohort(cohortObject).then(data => {
					$state.transitionTo('cohorts');
				}, displayError);
            }

        };

        vm.cancelCohortForm = function() {
            if (!$scope.patCohortForm || !$scope.patCohortForm.$dirty) {
                $state.transitionTo('cohorts');
            } else if (vm.id) {
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
					data => {
						$state.transitionTo('cohorts');
					},
					function (arg) {
					}
				);
            } else {
                $uibModal.open({
					templateUrl: 'cancelCreateCohortModal.html',
					controller: 'cohorts.CancelCreateModalCtrl',
					controllerAs: 'mo'
				}).result.then(
					data => {
						$state.transitionTo('cohorts');
					},
					function (arg) {
					}
				);
            }
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        // This function takes the saved nodes and goes through the tree and plucks all with a valid id. Then adds to criteria which populates the dropzone
        function traverseNodes(data) {
            const reducer = (results, node) => {
                //         console.log(results);
                //console.log(node, !_.isEmpty(node.id), node.id, _.isEmpty(node.id));
                node.id && results.push(node);
                _.without([node.left_node, node.right_node], undefined).reduce(reducer, results);
                return results;
            }
            const fullResults = [data].reduce(reducer, []);
            console.log(fullResults + '%%%%%%%%%')
            for (var i = 0; i < fullResults.length; i++) {
                fullResults[i].displayName = fullResults[i].name
            }
            vm.criteria = fullResults;
        }

        function routeChange(event, newUrl, oldUrl) {
            if (!$scope.patCohortForm || !$scope.patCohortForm.$dirty) return;
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
                        $location.path($location.url(newUrl).hash());
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
                        $location.path($location.url(newUrl).hash());
                    },
					function () {}
				);
            }
        }
    }

    function AddCriteriaModalCtrl($uibModalInstance, criteria) {
        var mo = this;
        mo.criteria = criteria;
        mo.ok = function () {
            $uibModalInstance.close();
        };
    }

    function DeleteCriterionModalCtrl($uibModalInstance, criterionName) {
        var mo = this;
        mo.criterionName = criterionName;
        mo.ok = function () {
            $uibModalInstance.close();
        };

        mo.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

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