(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.phenotypes.PhenotypeService
     * @description
     * This service provides an API to interact with the REST endpoint for phenotypes.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.phenotypes')
        .factory('PhenotypeService', PhenotypeService);

    PhenotypeService.$inject = ['$http', '$q', 'appProperties'];

    function PhenotypeService($http, $q, appProperties) {
        let { dataEndpoint } = appProperties;
        return ({
            getSummarizedUserElements: getSummarizedUserElements,
            getPhenotypeMessages: getPhenotypeMessages,
            removePhenotype: removePhenotype,
            getPhenotype: getPhenotype
        });

        function getSummarizedUserElements() {
            return $http.get(dataEndpoint + '/phenotypes?summarize=true')
                .then(handleSuccess, handleError);
        }

        function getPhenotypeMessages(){
            return {
                'CATEGORIZATION': {
                    'displayName': 'Categorization',
                    'description': `For defining a significant category of codes or clinical events or observations.`
                },
                'TEMPORAL': {
                    'displayName': 'Temporal',
                    'description': `For defining a disease, finding or patient care process to be reflected by codes
                    ,clinical events and/or observations in a specified frequency, sequential or other temporal 
                    patterns.`
                },
                'SEQUENCE': {
                    'displayName': 'Sequence',
                    'description': `For defining a disease, finding or patient care process to be reflected by codes,
                    clinical events and/or observations in a specified sequential temporal pattern.`
                },
                'FREQUENCY': {
                    'displayName': 'Frequency',
                    'description': `For defining a disease, finding or patient care process to be reflected by codes,
                    clinical events and/or observations in a specified frequency.`
                },
                'VALUE_THRESHOLD': {
                    'displayName': 'Value Threshold',
                    'description': `For defining clinically significant thresholds on the value of an observation.`
                }
            };
        }

        function removePhenotype(id) {

         return $http['delete'](dataEndpoint+'/phenotypes/'+id)
                .then(handleSuccess, handleError);

        }

        function getPhenotype(key) {
            return $http.get(dataEndpoint+'/phenotypes/' + key+'?summarize=true')
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