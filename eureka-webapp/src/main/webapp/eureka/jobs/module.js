(function(){
	'use strict';

    /**
     * @ngdoc overview
     * @name eureka.jobs
     * @description
     * The module for the jobs section of the Eureka application.
     */
	angular.module('eureka.jobs', []);

    angular.module('eureka.cohorts').config(jobsConfig);

    jobsConfig.$inject = ['$stateProvider'];

	function jobsConfig($stateProvider) {

        $stateProvider
            .state('jobs', {
                url: '/jobs',
                templateUrl: 'eureka/jobs/views/main/main.html',
                controller: 'jobs.MainCtrl',
                controllerAs: 'jobs'
            });
	}

}());