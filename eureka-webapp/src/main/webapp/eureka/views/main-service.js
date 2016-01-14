var eurekaModule = angular.module('eureka');

eurekaModule.service('NewsAndFunding',['$http', function($http){
            return {
                    versionHistoryAsync:function() {
                        return $http.get('assets/data/version_history.json');
                    },
                    supportedByAsync:function() { 
                        return $http.get('assets/data/supported_by.json');
                    }
            };
                
}]);


