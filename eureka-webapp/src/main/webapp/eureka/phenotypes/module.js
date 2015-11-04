(function() {
  'use strict';

  /**
   * @ngdoc overview
   * @name eureka.phenotypes
   * @description
   * The module for the phenotypes section of the Eureka application.
   */
  angular.module('eureka.phenotypes', ['ui.router']);

  angular.module('eureka.phenotypes').config(phenotypesConfig);

  phenotypesConfig.$inject = ['$stateProvider'];

	function phenotypesConfig($stateProvider) {

    $stateProvider
    	.state('phenotypes', {
        url: '/phenotypes',
        templateUrl: 'eureka/phenotypes/views/main/main.html',
        controller: 'phenotypes.MainCtrl',
        controllerAs: 'phenotypes'
      });

	}

}());
