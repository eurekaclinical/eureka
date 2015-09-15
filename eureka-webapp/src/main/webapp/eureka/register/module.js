(function() {
    'use strict';

    /**
     * @ngdoc overview
     * @name eureka.register
     * @description
     * The module for the register section of the Eureka application.
     */
    angular.module('eureka.register', []);

    angular.module('eureka.register').config(registerConfig);

    registerConfig.$inject = ['$stateProvider'];

    function registerConfig($stateProvider) {

        $stateProvider
            .state('register', {
                url: '/register',
                templateUrl: 'eureka/register/views/main/main.html',
                controller: 'register.MainCtrl',
                controllerAs: 'register'
            });

    }
}());