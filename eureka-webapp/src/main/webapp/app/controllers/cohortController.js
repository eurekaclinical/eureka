
eurekaApp.controller(
    "CohortController",
    function( $scope, CohortService, $location) {

        var vm = this;

        CohortService.getCohorts().then(function(data) {
            vm.cohorts = data;

            /*
            for (var key in vm.cohorts) {
                console.log(key + " " + vm.cohorts[key]);
            }
            */

        }, displayError);

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
);