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

        return ({
            getTreeRoot: getTreeRoot,
            getTreeNode: getTreeNode,
            getTreeNodes: getTreeNodes
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
				let postBody = 'key=' + key;
                return $http.post(dataEndpoint + '/concepts', postBody)
					.then(
						function(response) {
							if (response.data.length > 0) {
								return response.data[0];
							} else {
								return $q.reject('Not found');
							}
						}, 
						handleError);
            }
        }

        function getTreeNodes(keys, summarize) {
            if (summarize === undefined) {
                summarize = false;
            }
		    let postBody = 'summarize=' + summarize;
			for (let i = 0; i < keys.length; i++) {
				postBody += '&';
				postBody += 'key=' + keys[i];
			}
			return $http.post(dataEndpoint + '/concepts', postBody)
				.then(
					function(response) {
						return response.data;
					}, 
					handleError);
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