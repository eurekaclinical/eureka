(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.cohorts.CohortService
     * @description
     * This service provides an API to interact with the REST endpoint for cohorts.
     * @requires $http
     * @requires $q
     */

    angular
        .module('eureka.cohorts')
        .factory('CohortService', CohortService);

    CohortService.$inject = ['$http', '$q', 'appProperties'];

    function CohortService($http, $q, appProperties) {

        let { dataEndpoint } = appProperties;
        return ({
            getCohorts: getCohorts,
            getCohort: getCohort,
            getSystemElement: getSystemElement,
            getPhenotypes: getPhenotypes,
            removeCohort: removeCohort,
            createCohort: createCohort
        });

        function getCohorts() {

            var type = 'COHORT';
            return $http.get(dataEndpoint+'/destinations?type=' + type)
                .then(handleSuccess, handleError);

        }

        function removeCohort(key) {

            return $http['delete'](dataEndpoint+'/destinations/' + key)
                .then(handleSuccess, handleError);

        }

        function getSystemElement(key) {

            return $http.get(dataEndpoint+'/concepts/' + key + '?summary=true')
                .then(handleSuccess, handleError);

        }
        function getCohort(cohortId) {
            return $http.get(dataEndpoint+'/destinations/' + cohortId)
                .then(handleSuccess, handleError);

        }
        function getPhenotypes(cohort) {


            var cohorts = [];

            function traverse(node) {

                if (node.left_node !== undefined) {
                    traverse(node.left_node);
                }

                if (node.name !== undefined) {
                    cohorts.push(node.name);
                }

                if (node.right_node !== undefined) {
                    traverse(node.right_node);
                }
            }

            traverse(cohort.node);

            var promises = [];
            angular.forEach(cohorts, function(cohort){
                var promise = $http.get(dataEndpoint+'/concepts/' + cohort + '?summary=true');
                promises.push(promise);

            });

            return $q.all(promises);

        }

        function createCohort(cohort){
            /*This is what the data looks like being sent to server.  Does not look valid
            {"id":null,"type":"COHORT","ownerUserId":1,"name":"NameTest","description":"NameDescription",
            "phenotypeFields":null, "cohort":{"id":null,"node":{"id":null,"start":null,"finish":null,
            "type":"Literal","name":"\\ACT\\Medications\\"}},"read":false,"write":false,"execute":false,
            "created_at":null,"updated_at":null,"links":null}
            */
            let newCohort = {
                id: null,
                type: 'COHORT',
                ownerUserId: 1,
                phenotypeFields: null,
                cohort: {
                    id: null
                },
                read: false,
                write: false,
                execute: false,
                created_at: null,
                updated_at: null,
                links: null
            };
            let phenotypes = cohort.memberList;
            let node = {id: null, start: null, finish: null, type: 'Literal'};
            if (phenotypes.length === 1) {
                node.name = phenotypes[0].id;
            } else if (phenotypes.length > 1) {
                let first = true;
                let prev = null;
                for (var i = phenotypes.length - 1; i >= 0; i--) {
                    var literal = {id: null, start: null, finish: null, type: 'Literal'};
                    literal.name = phenotypes[i].id;
                    if (first) {
                        first = false;
                        prev = literal;
                    } else {
                        var binaryOperator = {id: null, type: 'BinaryOperator', op: 'OR'};
                        binaryOperator.left_node = literal;
                        binaryOperator.right_node = prev;
                        prev = binaryOperator;
                    }
                }
                node = prev;
            } else {
                node = null;
            }
            newCohort.name = cohort.name;
            newCohort.description = cohort.description;
            newCohort.cohort.id = null;
            newCohort.cohort.node = node;
            return $http.post(dataEndpoint+'/destinations/', newCohort)
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