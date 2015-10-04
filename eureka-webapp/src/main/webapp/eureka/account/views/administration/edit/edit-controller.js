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
        
    EditCtrl.$inject = ['AccountService', '$stateParams', '$state'];
    
    function EditCtrl(AccountService, $stateParams, $state) {
        var vm = this;
        vm.updateUser = updateUser;

        if ($stateParams.id) {
            AccountService.getUserById($stateParams.id).then(function(data) {
                vm.user = data;

            }, displayError); 

        }

        function updateUser(editAdmin){

            AccountService.updateUser(editAdmin.user).then(function(data) {
                $state.transitionTo('accountAdministration');

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