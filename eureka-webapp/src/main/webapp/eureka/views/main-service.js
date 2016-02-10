var eurekaModule = angular.module('eureka');

eurekaModule.service('NewsAndFundingService',['$http', function($http){
        return {
                getVersionHistoryAsync:function() {
                    return $http.get('assets/data/version_history.json');
                },
                getSupportedByAsync:function() { 
                    return $http.get('assets/data/supported_by.json');
                }
        };     
}]);


