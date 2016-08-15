(function() {

    'use strict';
    
    var eurekaModule = angular.module('eureka');

    eurekaModule.service('AppPropertiesService',['$http', function($http){
        return {
                    getAppProperties:function() {
                        return $http.get('proxy-resource/appproperties');
            }
        };
    }]);

    var appProperties = {
        dataEndpoint: 'https://localhost:8443/user-services/api/protected',
        filterEndpoint: 'protected/jstree3_searchsystemlist'
    };

    /**
     * @ngdoc object
     * @name eureka.appProperties
     * @description
     * Simple configuration object that stores application properties.
     */

    angular.module('eureka')
        .value('appProperties', appProperties);

}());