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

    AccountService.$inject = ['$http', '$q', 'appProperties'];

    function AccountService($http, $q, appProperties) {
        let { dataEndpoint } = appProperties;
        return ({
            changePassword: changePassword,
            getUserById: getUserById,
            updateUser: updateUser,
            passwordExpiration: passwordExpiration

        });

        function changePassword(passwordObject) {
            return $http.post(dataEndpoint+'/users/passwordchange', passwordObject)
            .then(handleSuccess, handleError);
        }

        function passwordExpiration(passwordObj) {
            return $http.post(
                '/eureka-webapp/protected/passwordexpiration?firstLogin=true&redirectURL=/eureka-angular/',
                passwordObj)
            .then(handleSuccess, handleError);
        }

        function getUserById(id) {
            return $http.get(dataEndpoint+'/users/'+id)
            .then(handleSuccess, handleError);
        }

        function updateUser(userObject) {
            return $http.put(dataEndpoint + '/users/', userObject)
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