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
        
    MainCtrl.$inject = ['users'];
    
    function MainCtrl(users) {
        var vm = this;
        vm.userslist = [];

        vm.filter = {
            options: {
                debounce: 500
            }
        };

        vm.selected = [];

        vm.query = {
            filter: '',
            order: 'name',
            limit: 5,
            page: 1
        };

        function success(users) {
            vm.userslist = users;
        }

        users.getUsers().then(success, displayError);

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        vm.removeFilter = function () {
            vm.filter.show = false;
            vm.query.filter = '';

            if(vm.filter.form.$dirty) {
                vm.filter.form.$setPristine();
            }
        };

        // in the future we may see a few built in alternate headers but in the mean time
        // you can implement your own search header and do something like
        vm.search = function (predicate) {
            vm.filter = predicate;
            vm.deferred = users.getUsers(vm.query).then(success, displayError);
        };

        vm.onOrderChange = function () {
            return users.getUsers(vm.query);
        };

        vm.onPaginationChange = function () {
            return users.getUsers(vm.query);
        };

    }
})();