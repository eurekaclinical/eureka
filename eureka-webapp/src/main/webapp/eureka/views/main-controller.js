var eurekaModule = angular.module('eureka');

eurekaModule.controller('NewsAndFundingController',['$scope', '$sce', 'NewsAndFundingService', 
        function($scope,$sce,NewsAndFundingService){
                    var service = {versionHistory:[],supportedBy:[]};
                    NewsAndFundingService.getVersionHistoryAsync()
                            .then(function(response){                  
                                    $scope.versionHistory = response.data.versionHistory;
                            });

                    NewsAndFundingService.getSupportedByAsync()
                            .then(function(response) {
                                    var data = response.data;          
                                    var result = data.supportedBy.slice(0, -1).join('; ');
                                    if (data.supportedBy.length > 1) {
                                            result += '; and ';
                                    }
                                    result += data.supportedBy[data.supportedBy.length - 1];
                                    $scope.supportedByHTML = $sce.trustAsHtml(result);
                            });
        }
]);