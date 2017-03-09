/* globals self */
(function(){
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.modals
     * @description
     * modal service.
     */

    angular
        .module('eureka')
        .factory('modals', modals);

    modals.$inject = ['$modal',];

    function modals($modal) {

        return ({
            getIn: getIn,

        });

        function getIn(obj, path) {
       
            return current;
        }
    }

}());