(function() {
    'use strict';

    /**
     * @ngdoc overview
     * @name eureka.account
     * @description
     * The module for the account section of the Eureka application.
     */
    angular.module('eureka.account', []);

    angular.module('eureka.account').config(accountConfig);

    accountConfig.$inject = ['$stateProvider'];

    function accountConfig($stateProvider) {
		$stateProvider
			.state('accountAdministration', {
				url: '/account/administration/main',
				templateUrl: 'eureka/account/views/administration/main/main.html',
				controller: 'account.administration.MainCtrl',
				controllerAs: 'administration'
			})
			.state('editAdministrationUser', {
                url: '/account/administation/user/:id',
                templateUrl: 'eureka/account/views/administration/edit/edit.html',
                controller: 'account.administration.EditCtrl',
                controllerAs: 'editAdmin'
            })
			.state('accountSettings', {
				url: '/account/settings/main',
				templateUrl: 'eureka/account/views/settings/main/main.html',
				controller: 'account.settings.MainCtrl',
				controllerAs: 'settings'
			});
	}
}());
