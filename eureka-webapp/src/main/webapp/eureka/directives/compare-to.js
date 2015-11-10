(function() {
    'use strict';

    /**
     * @ngdoc directive
     * @name eureka.directive:compareTo
     * @element *
     * @function
     * @description
     * compareTo directive
     */
    angular
        .module('eureka')
        .directive('compareTo', compareTo);

    function compareTo() {
        return {
            require: 'ngModel',
            scope: {
                otherModelValue: '=compareTo'
            },
            link: function(scope, element, attributes, ngModel) {

                ngModel.$validators.compareTo = function(modelValue) {
                    return modelValue === scope.otherModelValue;
                };

                scope.$watch('otherModelValue', function() {
                    ngModel.$validate();
                });
            }
        };
    }

}());