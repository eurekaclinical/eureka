(function(){
	'use strict';

    /**
     * @ngdoc overview
     * @name eureka.cohorts
     * @description
     * The module for the cohorts section of the Eureka application.
     */
	angular.module('eureka.cohorts', []);

  angular.module('eureka.cohorts').config(cohortsConfig);

  cohortsConfig.$inject = ['$stateProvider'];

	function cohortsConfig($stateProvider) {

    $stateProvider
    	.state('cohorts', {
        url: '/cohorts',
        templateUrl: 'eureka/cohorts/views/main/main.html',
        controller: 'cohorts.MainCtrl',
        controllerAs: 'cohorts'
      })
      .state('testCohorts', {
        url: '/test/cohorts',
        templateUrl: 'eureka/cohorts/views/test/test.html',
        controller: 'cohorts.TestCtrl',
        controllerAs: 'cohorts'
      })
      .state('newCohort', {
        url: '/cohorts/new',
        templateUrl: 'eureka/cohorts/views/edit/edit.html',
        controller: 'cohorts.EditCtrl',
        controllerAs: 'editCohort'
      })
      .state('editCohort', {
        url: '/cohorts/:key',
        templateUrl: 'eureka/cohorts/views/edit/edit.html',
        controller: 'cohorts.EditCtrl',
        controllerAs: 'editCohort'
      });

	}


}());
