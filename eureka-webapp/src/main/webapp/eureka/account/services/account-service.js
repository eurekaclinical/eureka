(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.account.AccountService
     * @description
     * This service provides an API to interact with the REST endpoint for account.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.account')
        .factory('AccountService', AccountService);

    AccountService.$inject = ['$http', '$q'];

    function AccountService($http, $q) {}

}());