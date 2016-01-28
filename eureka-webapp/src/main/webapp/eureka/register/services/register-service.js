(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.register.RegisterService
     * @description
     * This service provides an API to interact with the REST endpoint for registration.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.register')
        .factory('RegisterService', RegisterService);

    RegisterService.$inject = ['$http', '$q'];

    function RegisterService($http, $q) {
        let { dataEndpoint } = appProperties;
        return ({
            addNewAccount: addNewAccount
        });

        function addNewAccount(newAccount) {
            newAccount.username = newAccount.email;
            newAccount.fullName = newAccount.firstName + ' ' + newAccount.lastName;
            newAccount.type = 'LOCAL';
            newAccount.loginType = 'INTERNAL';
            return $http.post(dataEndpoint + '/userrequests', newAccount)
                .then(handleSuccess, handleError);
        }

        function handleSuccess(response) {
            return response.data;
        }

        function handleError(response) {
            if (!angular.isObject(response.data) && !response.data) {
                return ($q.reject('An unknown error occurred.'));
            }
            return ($q.reject(response.data));
        }

    }
}());