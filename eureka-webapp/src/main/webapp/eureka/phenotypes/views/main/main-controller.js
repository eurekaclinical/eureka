(function() {
    'use strict';

    angular
        .module('phenotypes')
        .controller('phenotypes.MainCtrl');

    MainCtrl.$inject = ['$scope', 'PhenotypeService', '$location'];

    function MainCtrl($scope, PhenotypeService, $location) {

        var vm = this;
        var messages = {
            'CATEGORIZATION': {
                'displayName': "Category",
                'description': "For defining a significant category of codes or clinical events or observations."
            },
            'TEMPORAL': {
                'displayName': "Temporal",
                'description': "For defining a disease, finding or patient care process to be reflected by codes,clinical events and/or observations in a specified frequency, sequential or other temporal patterns."
            },
            'SEQUENCE': {
                'displayName': "Sequence",
                'description': "For defining a disease, finding or patient care process to be reflected by codes,clinical events and/or observations in a specified sequential temporal pattern."
            },
            'FREQUENCY': {
                'displayName': "Frequency",
                'description': "For defining a disease, finding or patient care process to be reflected by codes,clinical events and/or observations in a specified frequency."
            },
            'VALUE_THRESHOLD': {
                'displayName': "Value Threshold",
                'description': "For defining clinically significant thresholds on the value of an observation."
            }
        };
        vm.messages = messages;

        PhenotypeService.getSummarizedUserElements().then(function(data) {
            vm.props = data;


        }, displayError);

        function displayError(msg) {
            vm.errorMsg = msg;
        }


        /*

        passwordChange.error.internalServerError=Error while changing password.
        deleteDataElement.error.internalServerError=Error trying to delete data element. There is a problem with Eureka!.
        registerUserServlet.fullName={0} {1}
        registerUserServlet.error.unspecified=Please contact {0} for assistance.
        registerUserServlet.error.localAccountConflict=An account with your email address already exists.

        */

    }
}());