(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.settings.controller:MainCtrl
     * @description
     * This is the main controller for the account settings section of the application.
     * @requires account.AccountService
     */

    angular
        .module('eureka.account')
        .controller('account.settings.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['AccountService'];
    
    function MainCtrl(AccountService) {
        var vm = this;
    }
})();