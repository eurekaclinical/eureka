
eurekaApp.controller(
    "MainController",
    function( $scope, $location) {
        $scope.goto = function(path) {

            $location.path(path);

        }

    }
);