(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.administration.controller:PasswordCtrl
     * @description
     * This is the main controller for the password expiration section of the application.
     * @requires account.AccountService
     */

    angular
        .module('eureka.account')
        .controller('account.administration.PasswordCtrl', PasswordCtrl);
        
    PasswordCtrl.$inject = ['AccountService', '$state'];
    
    function PasswordCtrl(AccountService, $state) {
        var vm = this;
        vm.passwordObj = {};
        vm.updatePassword = updatePassword;

        function updatePassword(passwordObj){
            AccountService.passwordExpiration(passwordObj).then(function(data) {
                $state.transitionTo('index');
            }, displayError); 
        }
        function displayError(msg) {
            vm.errorMsg = msg;
        }

    }
})();