(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.administration.controller:PasswordCtrl
     * @description
     * This is the main controller for the password expiration section of the application.
     * @requires
     */

    angular
        .module('eureka.account')
        .controller('account.administration.PasswordCtrl', PasswordCtrl);
        
    PasswordCtrl.$inject = ['users'];
    
    function PasswordCtrl() {
        var vm = this;

        function displayError(msg) {
            vm.errorMsg = msg;
        }

    }
})();