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
     * @requires ui.tree
     * @requires angularValidator
     * @requires cohorts
     * @requires phenotypes
     * @requires register
     */
    angular.module('eureka', [
        'ui.router',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker',
        'ui.tree',
        'angularValidator',
        'ngMessages',
        'ui.grid',
        'ui.grid.pagination',
        'ngTable',
        'eureka.cohorts',
        'eureka.phenotypes',
        'eureka.help',
        'eureka.jobs',
        'flow']);

    angular.module('eureka').run(eurekaRun);
    angular.module('eureka').config(eurekaConfig);

    eurekaRun.$inject = ['$rootScope', 'AppPropertiesService', 'appProperties', 'users'];
    eurekaConfig.$inject = ['$stateProvider', '$urlRouterProvider', '$httpProvider'];

    function eurekaRun($rootScope, AppPropertiesService, appProperties, users) {
      
        $rootScope.app=appProperties;
        
        AppPropertiesService.getAppProperties()
                    .then(function(response) {
            $rootScope.modes = response.data.appPropertiesModes;
            $rootScope.links = response.data.appPropertiesLinks;
            $rootScope.registration = response.data.appPropertiesRegistration;
        });
        
       $rootScope.userVerficationPerformed = false;
 
       $rootScope.inceptionYear = '2012';
       $rootScope.currentYear = new Date().getFullYear();
       users.getUser().then(function(user) {
           $rootScope.user = user;
           $rootScope.userVerficationPerformed = true;
       });  
    }

    function eurekaConfig($stateProvider, $urlRouterProvider, $httpProvider){

        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};    
        }    
        $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache, no-store, must-revalidate';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
        
        $urlRouterProvider.otherwise('/index');

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: 'eureka/views/main/main.html'
            });
    }

}());
