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
                .then(handleSuccess, handleError);
        }

        function getTreeNode(key) {
            if (key === 'root') {
                return $http.get(dataEndpoint + '/concepts/')
                    .then(handleSuccess, handleError);
            } else {
                return $http.post(dataEndpoint + '/concepts/', 'key=' + key)
                    .then(
                        function(response) {
                            if (response.data.length > 0) {
                                return response.data[0];
                            } else {
							    $q.reject('Not found');
							}
                        }, 
                        handleError);
            }
        }

        function getUserListRoot() {
            return $http.get(dataEndpoint + '/phenotypes')
                .then(handleSuccess, handleError);
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
                return ($q.reject(response.statusText));
            }
            return ($q.reject(response.data));
        }


    }

}());