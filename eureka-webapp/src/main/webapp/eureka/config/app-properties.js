(function() {

    'use strict';

    var appProperties = {
        demoMode: true,
        ephiProhibited: true,
        registrationEnabled: true,
        aiwSiteUrl: 'http://aiw.sourceforge.net',
        organizationName: 'Emory University',
        apiEndpoint: '/eureka-services/api/protected'
    };

    angular.module('eurekaApp').value('appProperties', appProperties);

}());