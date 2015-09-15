(function() {
    'use strict';

    angular.module('eureka', ['ui.router', 'angularValidator', 'cohorts', 'phenotypes', 'register']);

    angular.module('eureka').run(eurekaRun);
    angular.module('eureka').config(eurekaConfig);

    eurekaRun.$inject = ['$rootScope', 'appProperties', 'users'];
    eurekaConfig.$inject = ['$stateProvider', '$urlRouterProvider'];

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
                templateUrl: 'eureka/views/main/main.html'
            });
    }

}());
