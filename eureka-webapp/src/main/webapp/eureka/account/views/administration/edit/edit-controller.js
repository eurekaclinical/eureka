(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.administration.controller:EditCtrl
     * @description
     * This is the main controller for editng a user account in the administration section of the application.
     * @requires account.AccountService
     */

    angular
        .module('eureka.account')
        .controller('account.administration.EditCtrl', EditCtrl);
        
    EditCtrl.$inject = ['AccountService', '$stateParams'];
    
    function EditCtrl(AccountService, $stateParams) {
        var vm = this;

        if ($stateParams.id) {
            console.log('we are editing now');
            AccountService.getUserById($stateParams.id).then(function(data) {
                vm.user = data;

            }, displayError); 

        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function success(users) {
            vm.userslist = users;
        }
    }
})();