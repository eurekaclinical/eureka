(function() {
    'use strict';

    angular.module('eureka', ['ui.router', 'angularValidator']);

    function eurekaRun($rootScope, appProperties, users) {
       $rootScope.app = appProperties;
       $rootScope.user = {
           isActivated: false
       };
       $rootScope.conceptionYear = '2012';
       $rootScope.currentYear = new Date().getFullYear();
       users.getUser().then(function(user) {
           $rootScope.user = user;
       });
    }

    function eurekaConfig($stateProvider, $urlRouterProvider){

        $urlRouterProvider.otherwise('/index');

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: 'eureka/views/main.html',
                controller: 'MainController'
            })
            .state('register', {
                url: '/register',
                templateUrl: 'eureka/views/register/register.html',
                controller: 'RegisterController'
            })
            .state('registerInfo', {
                url: '/register_info',
                templateUrl: 'eureka/views/register/registration_info.html',
                controller: 'RegisterController'
            })
            .state('cohortHome', {
                url: '/cohort_home',
                templateUrl: 'eureka/cohorts/views/main/cohort_home.html',
                controller: 'MainController',
                controllerAs: 'cohortController'
            })
            .state('newCohort', {
                url: '/edit_cohort',
                templateUrl: 'eureka/views/cohorts/edit_cohort.html',
                controller: 'CohortEditController',
                controllerAs: 'cohortEditController'
            })
            .state('cohortEditKey', {
                url: '/edit_cohort/:key',
                templateUrl: 'eureka/views/cohorts/edit_cohort_key.html',
                controller: 'CohortEditController',
                controllerAs: 'cohortEditController'
            });
    }

    eurekaRun.$inject = ['$rootScope', 'appProperties', 'users'];
    eurekaConfig.$inject = ['$stateProvider', '$urlRouterProvider'];

    angular.module('eureka').run(eurekaRun);
    angular.module('eureka').config(eurekaConfig);

}());
