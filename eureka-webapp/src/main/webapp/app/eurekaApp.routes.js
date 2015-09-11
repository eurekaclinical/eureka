angular.module('eurekaApp').config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/index');

    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'app/views/main.html',
            controller: 'MainController'
        })
        .state('register', {
            url: '/register',
            templateUrl: 'app/views/register/register.html',
            controller: 'RegisterController'
        })
        .state('registerInfo', {
            url: '/register_info',
            templateUrl: 'app/views/register/registration_info.html',
            controller: 'RegisterController'
        })
        .state('cohortHome', {
            url: '/cohort_home',
            templateUrl: 'app/views/cohorts/cohort_home.html',
            controller: 'CohortController',
            controllerAs: 'cohortController'
        })
        .state('newCohort', {
            url: '/edit_cohort',
            templateUrl: 'app/views/cohorts/edit_cohort.html',
            controller: 'CohortEditController',
            controllerAs: 'cohortEditController'
        })
        .state('cohortEditKey', {
            url: '/edit_cohort/:key',
            templateUrl: 'app/views/cohorts/edit_cohort_key.html',
            controller: 'CohortEditController',
            controllerAs: 'cohortEditController'
        });

}]);