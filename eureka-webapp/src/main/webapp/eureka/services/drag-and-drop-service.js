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
            getNodes: getNodes,
            clearNodes: clearNodes


        });

        function getNodes() {

            return currentNodes;
        }

        function setNodes(obj) {
            let currentList = currentNodes;
            let isDuplicate = false;
            //lets do it the long way first then we will refactor.  Lets see if there are duplicates JS
            if (currentList.length < 1) {
                currentNodes.push(obj);
            } else {
                for (var i = 0; i < currentList.length; i++) {
                    if (currentList[i].displayName === obj.displayName) {
                        isDuplicate = true;
                        break;
                    }

                }
                if (isDuplicate !== true) {
                    currentNodes.push(obj);
                }

            }


        }

        function clearNodes() {
            currentNodes = [];
        }


    }

}());