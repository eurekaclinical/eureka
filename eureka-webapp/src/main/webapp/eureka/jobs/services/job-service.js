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
            fileUpload: fileUpload,
            getJobs: getJobs,
            getJob: getJob,
            getLatestJobs: getLatestJobs,
            getDestinations: getDestinations,
            getSourceConfigs: getSourceConfigs
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

        function fileUpload(sourceId) {

            return $http.post(dataEndpoint+'/file/upload' + sourceId)
                .then(handleSuccess, handleError);

        }

        function getJobs() {

            return $http.get(dataEndpoint+'/jobs')
                .then(handleSuccess, handleError);

        }

        function getJob(id) {

            return $http.get(dataEndpoint+'/job/' + id)
                .then(handleSuccess, handleError);

        }
        
        //get a list of existing jobs, with latest one as the first one
        function getLatestJobs(){
            
            return $http.get(dataEndpoint+'/jobs/latest')
                .then(handleSuccess, handleError);
        }

        //get a list of destinations
        function getDestinations(){
            
            return $http.get(dataEndpoint+'/destinations')
                .then(handleSuccess, handleError);
            
        }

        //get a list of sourceConfigs
        function getSourceConfigs(){

            return $http.get(dataEndpoint+'/sourceconfig')
                .then(handleSuccess, handleError);
            
        }
    }

}());