(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('IdleTimeoutCtrl', IdleTimeoutCtrl);

    IdleTimeoutCtrl.$inject = ['SessionService'];

    function IdleTimeoutCtrl(SessionService) {
		let vm = this;

        vm.getSessionProperties = function() {
            return SessionService.getSessionProperties();
        };
    }
}());
