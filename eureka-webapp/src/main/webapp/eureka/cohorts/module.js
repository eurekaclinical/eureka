(function(){
	'use strict';

	angular.module('cohorts',[]);

    angular.module('cohorts').config(cohortsConfig);

    cohortsConfig.$inject = ['$stateProvider'];

	function cohortsConfig($stateProvider) {

        $stateProvider
        	.state('cohorts', {
                url: '/cohorts',
                templateUrl: 'eureka/cohorts/views/main/main.html',
                controller: 'cohorts.MainCtrl',
                controllerAs: 'cohorts'
            })
            .state('newCohort', {
                url: '/cohorts/new',
                templateUrl: 'eureka/cohorts/views/new/new.html',
                controller: 'cohorts.NewCtrl',
                controllerAs: 'newCohort'
            })
            .state('editCohort', {
                url: '/cohorts/:key',
                templateUrl: 'eureka/cohorts/views/edit/edit.html',
                controller: 'cohorts.EditCtrl',
                controllerAs: 'editCohort'
            });

	}


}());