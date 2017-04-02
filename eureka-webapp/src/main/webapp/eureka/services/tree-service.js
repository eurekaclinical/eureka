/* globals self */
(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.TreeService
     * @description
     * This will provide all services for tree component
     */

    angular
        .module('eureka')
        .factory('TreeService', TreeService);

    TreeService.$inject = ['$http', '$q', 'appProperties'];

    function TreeService($http, $q, appProperties) {
        let { dataEndpoint } = appProperties;
        let { dataProtectedEndPoint } = appProperties;

        return ({
            removePhenotype: removePhenotype,
            getPhenotype: getPhenotype,
            getTreeRoot: getTreeRoot,
            getTreeNode: getTreeNode,
            getUserListRoot: getUserListRoot
        });

        function getTreeRoot() {
            return $http.get(dataEndpoint + '/concepts')
                .then(handleSuccess, handleError)
                // https://localhost:8443/eureka-webapp/protected/systemlist?key=root
                //https://localhost:8443/eureka-webapp/proxy-resource/systemlist?key=root
        }

        function getTreeNode(key) {
            if (key === 'root') {
                return $http.get(dataEndpoint + '/concepts/')
                    .then(handleSuccess, handleError)
            } else {
                return $http.get(dataEndpoint + '/concepts/' + key)
                    .then(handleSuccess, handleError)
                    // https://localhost:8443/eureka-webapp/protected/systemlist?key=root
                    //https://localhost:8443/eureka-webapp/proxy-resource/systemlist?key=root
            }
        }

        function getUserListRoot() {
            return $http.get(dataProtectedEndPoint + '/userproplist?key=root')
                //https://localhost:8443/eureka-webapp/protected/systemlist?key=root
        }

        function removePhenotype(id) {

            return $http['delete'](dataEndpoint + '/phenotypes/' + id)
                .then(handleSuccess, handleError);

        }

        function getPhenotype(key) {
            return $http.get(dataEndpoint + '/phenotypes/' + key + '?summarize=true')
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