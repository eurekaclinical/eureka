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

    EditCtrl.$inject = ['CohortService', 'TreeService', '$stateParams', '$state', '$scope', '$uibModal'];
    AddCriteriaModalCtrl.$inject = ['$uibModalInstance', 'criteria'];
    DeleteCriterionModalCtrl.$inject = ['$uibModalInstance', 'criterionName'];
    CancelCreateModalCtrl.$inject = ['$uibModalInstance'];
    CancelEditModalCtrl.$inject = ['$uibModalInstance', 'cohortName'];

    function EditCtrl(CohortService, TreeService, $stateParams, $state, $scope, $uibModal) {
        let vm = this;
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

        let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);

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
                          if (editMember.key === vm.criteria[j].name) {
                              found = true;
                              break;
                          }
                      }
                      if (!found) {
                          vm.criteria.push({
						      name: editMember.key,
                              displayName: editMember.displayName,
						  });
                      }
                  }
                  vm.memberList = [];
                },
                function () {
                    vm.memberList = [];
                }
            );
        };

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
		};

        vm.submitCohortForm = function () {
            let cohortObject = {};
            cohortObject.name = vm.name;
            cohortObject.description = vm.description;
            cohortObject.memberList = vm.criteria;
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
            //Triggers $stateChangeStart event, so we get a confirm modal.
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
            let keys = [];
            for (let i = 0; i < fullResults.length; i++) {
                keys.push(fullResults[i].name);
            }

            let phenotypeKeys = [];
            let conceptKeys = [];
            for (let i = 0; i < keys.length; i++) {
                let key = keys[i];
                if (key.startsWith('USER:')) {
                    phenotypeKeys.push(key);
                } else {
                    conceptKeys.push(key);
                }
            }
            vm.criteria = [];
            if (conceptKeys.length > 0) {
				TreeService.getTreeNodes(conceptKeys).then(function(concepts) {
                    let keyToDisplayName = {};
                    for (let i = 0; i < conceptKeys.length; i++) {
						for (let j = 0; j < concepts.length; j++) {
							if (concepts[j].key === conceptKeys[i]) {
								keyToDisplayName[keys[i]] = concepts[j].displayName;
								break;
							}
						}
					}
					for (var i = 0; i < conceptKeys.length; i++) {
						let dn = keyToDisplayName[conceptKeys[i]];
						if (!dn) {
                            vm.criteria.push({
								name: conceptKeys[i],
								displayName: conceptKeys[i]
							});
							displayError('Unknown concept id ' + conceptKeys[i]);
						} else {
							vm.criteria.push({
								name: conceptKeys[i],
								displayName: dn
							});
                        }
					}
                    if (phenotypeKeys.length > 0) {
                        for (let i = 0; i < phenotypeKeys.length; i++) {
						    TreeService.getPhenotype(phenotypeKeys[i]).then(function(phenotype) {
								vm.criteria.push({name: phenotype.key, displayName: phenotype.displayName});
							}, function(msg) {
                                vm.criteria.push({
									name: phenotypeKeys[i],
									displayName: phenotypeKeys[i]
								});
								displayError('Unknown phenotype id ' + phenotypeKeys[i]);
                            });
                        }
					}
				}, displayError);
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

    function AddCriteriaModalCtrl($uibModalInstance, criteria) {
        var mo = this;
        mo.criteria = criteria;
        mo.ok = function () {
            $uibModalInstance.close();
        };
        mo.cancel = function () {
            $uibModalInstance.dismiss('cancel');
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