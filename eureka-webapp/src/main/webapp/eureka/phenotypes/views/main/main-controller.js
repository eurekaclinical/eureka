(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.phenotypes.controller:MainCtrl
     * @description
     * This is the main controller for the phenotypes section of the application.
     * @requires $scope
     * @requires $location
     * @requires eureka.phenotypes.PhenotypeService
     */

    angular
        .module('eureka.phenotypes')
        .controller('phenotypes.MainCtrl');

    MainCtrl.$inject = ['$scope', 'PhenotypeService', '$location'];

    function MainCtrl($scope, PhenotypeService, $location) {

        var vm = this;
        var messages =  PhenotypeService.getPhenotypeMessages();
        vm.messages = messages;

        PhenotypeService.getSummarizedUserElements().then(function(data) {
            vm.props = data;


        }, displayError);

        function displayError(msg) {
            vm.errorMsg = msg;
        }


        /*

        passwordChange.error.internalServerError=Error while changing password.
        deleteDataElement.error.internalServerError=Error trying to delete data element.
        There is a problem with Eureka!.
        registerUserServlet.fullName={0} {1}
        registerUserServlet.error.unspecified=Please contact {0} for assistance.
        registerUserServlet.error.localAccountConflict=An account with your email address already exists.

        */

    }
}());