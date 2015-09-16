(function(){
	'us strict';

	angular
			.module('eureka.cohorts')
			.factory('CohortTreeService', CohortTreeService);

	CohortTreeService.$inject = ['$http', '$q'];

	function CohortTreeService($http, $q){

		return ({
            getTreeData: getTreeData,
        });

		function getTreeData(id) {

            return $http.get('/eureka-webapp/protected/systemlist?key='+id)
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


