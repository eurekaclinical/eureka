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
        let { apiEndpoint } = appProperties;
        return ({
            changePassword: changePassword,
            getUserById: getUserById

        });

        function changePassword(passwordObject) {
            return $http.post(apiEndpoint + '/users/passwordchangerequest', passwordObject)
            .then(handleSuccess, handleError);
        }

        function getUserById(id) {
            return $http.get(apiEndpoint + '/users/byid/'+id)
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