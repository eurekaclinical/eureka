(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.cohorts.CohortFilterService
     * @description
     * This service provides an API to interact with the REST endpoint for cohorts.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.cohorts')
        .factory('CohortFilterService', CohortFilterService);

    CohortFilterService.$inject = ['$http', '$q', 'appProperties'];

    function CohortFilterService($http, $q, appProperties) {

        return ({
            filterCohorts: filterCohorts
        });

        function filterCohorts(filterString){
            /* filter url looks like this: 
            https://localhost:8443/eureka-webapp/protected/jstree3_searchsystemlist?str=Patient&key=root 
            */
            return $http
                .get(`${appProperties.filterEndpoint}`, {
                    cache: true,
                    params: {
                        key: 'root',
                        str: filterString
                       
                    }
                })
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