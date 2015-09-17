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

    CohortService.$inject = ['$http', '$q'];

    function CohortService($http, $q) {

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
            return $http.get('/eureka-services/api/protected/destinations?type=' + type)
                .then(handleSuccess, handleError);

        }

        function removeCohort(key) {

            return $http['delete']('/eureka-webapp/proxy-resource/destinations/' + key)
                .then(handleSuccess, handleError);

        }

        function getSystemElement(key) {

            return $http.get('/eureka-services/api/protected/systemelement/' + key + '?summary=true')
                .then(handleSuccess, handleError);

        }
        function getCohort(cohortId) {

            return $http.get('/eureka-services/api/protected/destinations/' + cohortId)
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
                var promise = $http.get('/eureka-services/api/protected/systemelement/' + cohort + '?summary=true');
                promises.push(promise);

            });

            return $q.all(promises);

        }

        function createCohort(cohort){
            /*This is what the data looks like being sent to server.  Does not look valid
            {"id":null,"type":"COHORT","ownerUserId":1,"name":"NameTest","description":"NameDescription",
            "dataElementFields":null, "cohort":{"id":null,"node":{"id":null,"start":null,"finish":null,
            "type":"Literal","name":"\\ACT\\Medications\\"}},"read":false,"write":false,"execute":false,
            "created_at":null,"updated_at":null,"links":null}
            */

            console.log(cohort);
            //lets build out a test obj
            let cohortTemplateObj = {'id': null, 'type': 'COHORT', 'ownerUserId': 1, 
            'name':'', 'description': '', 'dataElementFields': null, 
            'cohort':{'id': null, 'node':{'id': null, 'start': null, 'finish': null, 'type': 'Literal', 'name': ''}},
            'read': false, 'write': false, 'execute': false, 'created_at': null, 'updated_at': null, 'links': null};
            //after building out test obj lets add some data from the first item in the array
            if(cohort){
                cohortTemplateObj.id = cohort.name +'id';
                cohortTemplateObj.name = cohort.name;
                cohortTemplateObj.description = cohort.description;
                cohortTemplateObj.cohort.node.name = cohort.memberList[0].text;
                cohortTemplateObj.cohort.node.id = cohort.memberList[0].id;
            }

            return $http.post('/eureka-webapp/proxy-resource/destinations/',cohortTemplateObj)
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