(function() {
    'use strict';

    /**
     * @ngdoc overview
     * @name index
     * @description
     *
     * # Eureka Documentation
     *
     * Do you need help understanding the project structure or what services and directives you have available to you
     * in the Eureka Angular application? You've come to the right place!
     *
     * ## How to use this documentation
     *
     * This code-base is self-documenting; this documentation is automatically generated from comments left throughout
     * the code. If there is a problem with it, it is because the comments in the code were not updated.
     *
     * This project is broken down into several modules. Each module represents a section of the application. Inside
     * each module you will find views, directives, and services specific to that module. This allows you to bite off
     * code changes in smaller pieces and keeps everything organized in a sane manner.
     */

    /**
     * @ngdoc overview
     * @name eureka
     * @description
     * The main module for the Eureka Angular app.
     * @requires ui.router
     * @requires angularValidator
     * @requires cohorts
     * @requires phenotypes
     * @requires register
     */
    angular.module('eureka', [
        'ui.router',
        'angularValidator',
        'eureka.cohorts', 'eureka.phenotypes', 'eureka.register']);

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
