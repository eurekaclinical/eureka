(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.jobs.JobService
     * @description
     * This service provides an API to interact with the REST endpoint for jobs.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.jobs')
        .factory('JobService', JobService);

    JobService.$inject = ['$http', '$q', 'appProperties'];

    function JobService($http, $q, appProperties) {

        let { dataEndpoint } = appProperties;
        return ({

        });

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