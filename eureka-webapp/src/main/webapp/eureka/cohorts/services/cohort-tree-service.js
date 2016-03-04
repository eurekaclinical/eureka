(function(){
  'use strict';

  angular
    .module('eureka.cohorts')
    .factory('CohortTreeService', CohortTreeService);

  CohortTreeService.$inject = ['$http', '$q', 'appProperties'];

  function CohortTreeService($http, $q, appProperties){

    let { dataEndpoint } = appProperties;

    return {
      getTreeData: getTreeData
    };

    function getTreeData(id) {
      return $http
        .get(`${dataEndpoint}/concepts/`, {
          cache: true,
          params: {
            key: id
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
