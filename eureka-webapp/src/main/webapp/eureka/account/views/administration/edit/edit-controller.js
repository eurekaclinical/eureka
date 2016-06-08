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
        var role = {};
        vm.role = role;

        if ($stateParams.id) {
            AccountService.getUserById($stateParams.id).then(function(data) {
                vm.user = data;
                angular.forEach(data.roles, function(value) {
                    if(value === 1){
                        vm.role.researcher = true;  
                    }
                    if(value === 2){
                        vm.role.admin = true;
                    }
                });

            }, displayError); 

        }

        function updateUser(editAdmin){
            var roleArray = [];
            if(!_.isEmpty(editAdmin.role)){
                angular.forEach(editAdmin.role, function(value, key) {
                    if(key === 'researcher' && value === true){
                        roleArray.push(1);
                    }
                    if(key === 'admin' && value === true){
                        roleArray.push(2);
                    }
                });
            }
            editAdmin.user.roles = [];
            editAdmin.user.roles = roleArray;

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