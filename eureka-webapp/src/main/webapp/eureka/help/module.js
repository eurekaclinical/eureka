(function(){
    'use strict';

    /**
     * @ngdoc overview
     * @name eureka.help
     * @description
     * The module for the cohorts section of the Eureka application.
     */
    angular.module('eureka.help', []);

    angular.module('eureka.help').config(helpConfig);

    helpConfig.$inject = ['$stateProvider'];

    function helpConfig($stateProvider) {

        $stateProvider
            .state('help', {
                url: '/help',
                templateUrl: 'eureka/help/views/main/main.html'
            });

    }


}());