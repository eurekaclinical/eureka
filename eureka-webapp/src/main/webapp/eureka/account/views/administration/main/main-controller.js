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
        
    MainCtrl.$inject = ['AccountService', 'users'];
    
    function MainCtrl(AccountService, users) {
        var vm = this;

        getUsers();

        function getUsers() {
            users.getUsers().then(function (data) {
                vm.list = data;
            }, displayError);
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function success(users) {
            vm.userslist = users;
        }

        users.getUsers().then(success);
    }
})();