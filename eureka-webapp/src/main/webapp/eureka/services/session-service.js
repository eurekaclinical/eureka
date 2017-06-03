/* globals self */
(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.SessionService
     * @description
     * This will provide all services for tree component
     */

    angular
        .module('eureka')
        .factory('SessionService', SessionService);

    SessionService.$inject = ['$http', '$q', 'appProperties'];

    function SessionService($http, $q, appProperties) {
        let { dataProtectedEndPoint } = appProperties;

        return ({
            getSessionProperties: getSessionProperties
        });

        function getSessionProperties() {
            return $http.get(dataProtectedEndPoint + '/get-session-properties')
                .then(handleSuccess, handleError);
        }

        function handleSuccess(response) {
            return response.data;
        }

        function handleError(response) {
            if (!angular.isObject(response.data) && !response.data) {
				if (response.statusText) {
                    return ($q.reject(response.statusText));
                } else {
                    return ($q.reject('The server may be down.'));
                }
            }
            return ($q.reject(response.data));
        }


    }

}());
