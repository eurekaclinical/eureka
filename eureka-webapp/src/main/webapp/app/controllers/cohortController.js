
eurekaApp.controller(
    "CohortController",['$scope', 'CohortService', '$location', '$route',
    function( $scope, CohortService, $location, $route) {

        var vm = this;

        var getCohorts = function() {
            CohortService.getCohorts().then(function (data) {
                vm.cohorts = data;
            }, displayError);

        };
        getCohorts();

        $scope.remove = function (key) {
            CohortService.removeCohort(key);
            for (var i = 0; i < vm.cohorts.length; i++) {
                if (vm.cohorts[i].name == key) {
                    vm.cohorts.splice(i, 1);
                    break;
                }
            }

        };

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }]
);