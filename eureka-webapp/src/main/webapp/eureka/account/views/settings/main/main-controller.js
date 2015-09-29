(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.account.settings.controller:MainCtrl
     * @description
     * This is the main controller for the account settings section of the application.
     * @requires account.AccountService
     */

    angular
        .module('eureka.account')
        .controller('account.settings.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['AccountService', 'users', '$mdDialog'];
    
    function MainCtrl(AccountService, users, $mdDialog) {
        var vm = this;
        vm.resetPassword = resetPassword;

        getCurrentUser();

        function resetPassword(ev) {
            $mdDialog.show({
                controller: ChangePasswordController,
                templateUrl: 'eureka/account/views/settings/main/modal.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose:true
            })
            .then(function(answer) {
                console.log('*** Something happended');
            }, function() {
                console.log('dialog cancelded');
            });
        }

        function getCurrentUser() {
            users.getUser().then(function (data) {
                vm.currentUser = data;
            }, displayError);
        }

        function ChangePasswordController($scope, $mdDialog) {
            function hide() {
                $mdDialog.hide();
            }
            function cancel() {
                $mdDialog.cancel();
            }
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
})();