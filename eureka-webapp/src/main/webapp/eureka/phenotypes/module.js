(function () {
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
				})
				.state('createCATEGORIZATION', {
					url: '/phenotypes/categorization/new',
					templateUrl: 'eureka/phenotypes/views/categorization/edit.html',
					controller: 'phenotypes.categorization.EditCtrl',
					controllerAs: 'editPhenotype'
				})
				.state('createSEQUENCE', {
					url: '/phenotypes/sequence/new',
					templateUrl: 'eureka/phenotypes/views/sequence/edit.html',
					controller: 'phenotypes.sequence.EditCtrl',
					controllerAs: 'editSequencePhenotype'
				})
				.state('createFREQUENCY', {
					url: '/phenotypes/frequency/new',
					templateUrl: 'eureka/phenotypes/views/frequency/edit.html',
					controller: 'phenotypes.frequency.EditCtrl',
					controllerAs: 'editFrequencyPhenotype'
				})
				.state('createTHRESHOLD', {
					url: '/phenotypes/threshold/new',
					templateUrl: 'eureka/phenotypes/views/threshold/edit.html',
					controller: 'phenotypes.threshold.EditCtrl',
					controllerAs: 'editThresholdPhenotype'
				})
				.state('editCATEGORIZATION', {
					url: '/phenotypes/categorization/:key',
					templateUrl: 'eureka/phenotypes/views/categorization/edit.html',
					controller: 'phenotypes.categorization.EditCtrl',
					controllerAs: 'editPhenotype'
				})
				.state('editSEQUENCE', {
					url: '/phenotypes/sequence/:key',
					templateUrl: 'eureka/phenotypes/views/sequence/edit.html',
					controller: 'phenotypes.sequence.EditCtrl',
					controllerAs: 'editSequencePhenotype'
				})
				.state('editFREQUENCY', {
					url: '/phenotypes/frequency/:key',
					templateUrl: 'eureka/phenotypes/views/frequency/edit.html',
					controller: 'phenotypes.frequency.EditCtrl',
					controllerAs: 'editFrequencyPhenotype'
				})
				.state('editTHRESHOLD', {
					url: '/phenotypes/threshold/:key',
					templateUrl: 'eureka/phenotypes/views/threshold/edit.html',
					controller: 'phenotypes.threshold.EditCtrl',
					controllerAs: 'editThresholdPhenotype'
				});

	}

}());