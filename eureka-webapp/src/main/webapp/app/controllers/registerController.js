eurekaApp.controller(
    "RegisterController",
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
    }
);