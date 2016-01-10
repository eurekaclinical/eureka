var eurekaModule = angular.module('eureka');

eurekaModule.controller('NewsAndFundingController',['$scope', '$sce', function($scope,$sce){
            var service = {versionHistory:[],supportedBy:[]};
            $.getJSON('assets/data/version_history.json', function(data) {
                     service.versionHistory.push.apply(service.versionHistory,data.versionHistory);
                           $scope.versionHistory = service.versionHistory; 
            });
                     
            $.getJSON('assets/data/supported_by.json', function(data) {
                    var result = data.supportedBy.slice(0, -1).join("; ");
                    if (data.supportedBy.length > 1) {
                            result += "; and ";
                    }
                    result += data.supportedBy[data.supportedBy.length - 1];
                    $scope.supportedBy = result;
                    $scope.supportedByHTML = $sce.trustAsHtml(result);
            });
            
        
}]);