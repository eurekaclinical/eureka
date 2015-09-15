(function() {
    'use strict';

    angular.module('register', []);

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