eurekaApp.factory(
    "CohortService", ['$http', '$q',
    function( $http, $q ) {

        return ({
            getCohorts: getCohorts,
            getCohort: getCohort,
            getSystemElement: getSystemElement,
            getPhenotypes: getPhenotypes,
            removeCohort: removeCohort
        });

        function getCohorts() {

            var type = "COHORT";
            return $http.get("/eureka-services/api/protected/destinations?type=" + type)
                .then(handleSuccess, handleError);

        }

        function removeCohort(key) {

            return $http['delete']("/eureka-webapp/proxy-resource/destinations/" + key)
                .then(handleSuccess, handleError);

        }

        function getSystemElement(key) {

            return $http.get("/eureka-services/api/protected/systemelement/" + key + "?summarize=true")
                .then(handleSuccess, handleError);

        }
        function getCohort(cohortId) {

            return $http.get("/eureka-services/api/protected/destinations/" + cohortId)
                .then(handleSuccess, handleError);

        }
        function getPhenotypes(cohort) {


            var cohorts = new Array();

            function traverse(node) {

                if (node.left_node != undefined) {
                    traverse(node.left_node);
                }

                if (node.name != undefined) {
                    cohorts.push(node.name);
                }

                if (node.right_node != undefined) {
                    traverse(node.right_node);
                }
            }

            traverse(cohort.node);
            console.log(cohorts);

            var promises = [];
            angular.forEach(cohorts, function(cohort){
                var promise = $http.get("/eureka-services/api/protected/systemelement/" + cohort + "?summarize=true");
                promises.push(promise);

            });

            return $q.all(promises);

        }
        function handleSuccess(response) {
            return response.data;
        }
        function handleError(response) {
            if (!angular.isObject(response.data) && !response.data) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data));
        }
    }]
);