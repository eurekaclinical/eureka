(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.cohorts.controller:NewCtrl
     * @description
     * This is the new controller for the cohorts section of the application.
     */

    angular
        .module('eureka.cohorts')
        .controller('cohorts.NewCtrl', NewCtrl)
        .controller('cohorts.AddCriteriaModalCtrl', AddCriteriaModalCtrl)
        .controller('cohorts.DeleteCriterionModalCtrl', DeleteCriterionModalCtrl);

    NewCtrl.$inject = ['CohortService', '$state', '$rootScope', '$uibModal'];
    AddCriteriaModalCtrl.$inject = ['$uibModalInstance', 'criteria'];
    DeleteCriterionModalCtrl.$inject = ['$uibModalInstance', 'criterionName'];

    function NewCtrl(CohortService, $state, $rootScope, $uibModal) {
        let vm = this;
        let userId = $rootScope.user.info.id;

        vm.criteria = [];
        vm.memberList = [];

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
                      let newMember = vm.memberList[i];
                      let found = false;
                      for (let j = 0; j < vm.criteria.length; j++) {
                          if (newMember.key === vm.criteria[j].key) {
                              found = true;
                              break;
                          }
                      }
                      if (!found) {
                          vm.criteria.push(newMember);
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

            CohortService.createCohort(cohortObject).then(data => {
                $state.transitionTo('cohorts');
            }, displayError);

        };

        function displayError(msg) {
            vm.errorMsg = msg;
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

}());