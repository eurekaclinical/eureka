
eurekaApp.controller(
    "CohortController",
    function( $scope, CohortService, $location, $route) {

        var vm = this;

        var getCohorts = function() {
            CohortService.getCohorts().then(function (data) {
                vm.cohorts = data;
            }, displayError);

        };
        getCohorts();

        $scope.remove = function (key) {
            console.log(key);
            CohortService.removeCohort(key);
            for (var i = 0; i < vm.cohorts.length; i++) {
                if (vm.cohorts[i].name == key) {
                    vm.cohorts.splice(i);
                    break;
                }
            }

        };

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
);