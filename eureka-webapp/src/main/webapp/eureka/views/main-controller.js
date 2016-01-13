var eurekaModule = angular.module('eureka');

eurekaModule.controller('NewsAndFundingController',['$scope', '$sce', 'NewsAndFunding', 
    function($scope,$sce,NewsAndFunding){
            var service = {versionHistory:[],supportedBy:[]};
                NewsAndFunding.versionHistoryAsync().then(function(d) {                  
                    $scope.versionHistory = d.data.versionHistory;
                });
                                
                NewsAndFunding.supportedByAsync().then(function(d) {
                    var data = d.data;          
                    var result = data.supportedBy.slice(0, -1).join('; ');
                    if (data.supportedBy.length > 1) {
                            result += '; and ';
                    }
                    result += data.supportedBy[data.supportedBy.length - 1];
                    $scope.supportedByHTML = $sce.trustAsHtml(result);
                });
        
}]);