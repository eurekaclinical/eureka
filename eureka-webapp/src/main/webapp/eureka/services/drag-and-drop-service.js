/* globals self */
(function() {
    'use strict';

    /**
     * @ngdoc service
     * @name eureka.dragAndDropService
     * @description
     * This is the list drag and drop service.
     */

    angular
        .module('eureka')
        .factory('dragAndDropService', dragAndDropService);

    dragAndDropService.$inject = [];

    function dragAndDropService() {
        let currentNodes = [];
        return ({
            setNodes: setNodes,
            getNodes: getNodes


        });



        function getNodes() {

            return currentNodes;
        }

        function setNodes(obj) {

            if (obj) {
                currentNodes.push(obj);
            }

        }


    }

}());