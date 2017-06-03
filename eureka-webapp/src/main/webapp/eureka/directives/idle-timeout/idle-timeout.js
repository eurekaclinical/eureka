(function() {
    'use strict';

    /**
     * @ngdoc directive
     * @name eureka.directive:idleTimeout
     * @element *
     * @function
     * @description
     * idleTimeout directive
     */
    angular
        .module('eureka')
        .directive('idleTimeout', idleTimeout);

    function idleTimeout() {
        return {
			restrict: 'A',
            bindToController: true,
            replace: false,
            controller: 'IdleTimeoutCtrl',
            controllerAs: 'vm',
			link: function(scope, element, attrs, ctrl) {
                let sessionProperties = ctrl.getSessionProperties();
				$(element).idleTimeout({
					idleTimeLimit: sessionProperties.maxInactiveInterval - 30, //Time out with 30 seconds to spare to make sure the server session doesn't expire first
					redirectUrl: '/logout',
					alertDisplayLimit: 60, // Display 60 seconds before send of session.
					sessionKeepAliveTimer: sessionProperties.maxInactiveInterval - 15 //Send a keep alive signal with 15 seconds to spare.
				  });
			}
		};
    }

}());