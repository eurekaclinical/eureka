
eurekaApp.factory(
    "RegisterService", ['$http', '$q',
    function( $http, $q ) {

        return ({
            addNewAccount: addNewAccount
        });

        function addNewAccount(newAccount) {
            newAccount.username = newAccount.email;
            newAccount.fullName = newAccount.firstName + " " + newAccount.lastName;
            newAccount.type = "LOCAL";
            newAccount.loginType = "INTERNAL";
            return $http.post("/eureka-services/api/userrequest/new", newAccount)
                .then(handleSuccess, handleError);
        }

        function handleSuccess(response) {
            return response.data;
        }

        function handleError(response) {
            if (!angular.isObject(response.data) && !response.data) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data));
        }
    }]

);