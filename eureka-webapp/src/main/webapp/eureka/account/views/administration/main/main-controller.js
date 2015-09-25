(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.administration.controller:MainCtrl
     * @description
     * This is the main controller for the account administration section of the application.
     * @requires account.AccountService
     */

    angular
        .module('eureka.account')
        .controller('account.administration.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['AccountService'];
    
    function MainCtrl(AccountService) {
        var vm = this;
    }
})();