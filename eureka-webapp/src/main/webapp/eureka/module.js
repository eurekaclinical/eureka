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
     * @requires ngMaterial
     * @requires angularValidator
     * @requires cohorts
     * @requires phenotypes
     * @requires register
     */
    angular.module('eureka', [
        'ui.router',
        'ui.tree',
        'angularValidator',
        'ngMaterial',
        'ngMessages',
        'md.data.table',
        'eureka.cohorts',
        'eureka.phenotypes',
        'eureka.register',
        'eureka.account',
        'eureka.help',
        'eureka.jobs',
        'flow']);

    angular.module('eureka').run(eurekaRun);
    angular.module('eureka').config(eurekaConfig);

    eurekaRun.$inject = ['$rootScope', 'AppPropertiesService', 'appProperties', 'users', '$location'];
    eurekaConfig.$inject = ['$stateProvider', '$urlRouterProvider', '$mdThemingProvider'];

    function eurekaRun($rootScope, AppPropertiesService, appProperties, users, $location) {
      
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

    function eurekaConfig($stateProvider, $urlRouterProvider, $mdThemingProvider){

        $urlRouterProvider.otherwise('/index');

        $stateProvider
            .state('index', {
                url: '/index',
                templateUrl: 'eureka/views/main/main.html'
            });

        
        // Extend the red theme with a few different colors
        var darkBlueMap = $mdThemingProvider.extendPalette('blue', {
            '900': '24497A'
        });
        // Register the new color palette map with the name <code>neonRed</code>
        $mdThemingProvider.definePalette('darkBlue', darkBlueMap);
        // Use that theme for the primary intentions
        $mdThemingProvider.theme('default')
            .primaryPalette('darkBlue', {
                'hue-1': '900' // use shade 900 for the <code>md-hue-1</code> class
            });
    }

}());