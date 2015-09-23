(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.register.controller:MainCtrl
     * @description
     * This is the main controller for the register section of the application.
     * @requires $location
     * @requires eureka.register.RegisterService
     */

    angular
        .module('eureka.register')
        .controller('register.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['RegisterService', '$location'];
    
    function MainCtrl(RegisterService, $location) {
        var vm = this;
        vm.addNewAccount = addNewAccount;

        function addNewAccount(newAccount) {
            RegisterService.addNewAccount(newAccount).then(handleSuccess,displayError);
        }

        function handleSuccess(data) {
            vm.showSuccess = true;
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
})();