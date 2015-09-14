/*angular.module('eurekaApp').controller(
    "RegisterController",['$scope', 'RegisterService', '$location',
    function( $scope, RegisterService, $location) {

        $scope.addNewAccount = function (newAccount) {
            RegisterService.addNewAccount(newAccount).then(handleSuccess,displayError);
        };

        function handleSuccess(data) {
            $location.path("/register_info");
        }

        function displayError(msg) {
            $scope.errorMsg = msg;
        }

    }]
); */

(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('RegisterController', RegisterController);
        
    RegisterController.$inject = ['RegisterService', '$location'];
    
    function RegisterController(RegisterService, $location) {
        var vm = this;
        vm.addNewAccount = addNewAccount;

        function addNewAccount(newAccount) {
            RegisterService.addNewAccount(newAccount).then(handleSuccess,displayError);
        }

        function handleSuccess(data) {
            $location.path("/register_info");
        }

        function displayError(msg) {
            $scope.errorMsg = msg;
        }
     }
})();