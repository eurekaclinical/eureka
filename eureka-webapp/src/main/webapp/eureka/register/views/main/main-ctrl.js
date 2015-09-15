(function() {
    'use strict';

    angular
        .module('register')
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