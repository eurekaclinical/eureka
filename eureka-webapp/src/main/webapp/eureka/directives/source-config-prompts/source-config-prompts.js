(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('sourceConfigPrompts', SourceConfigPrompts);

    function SourceConfigPrompts() {
        return {
            restrict: 'AE',
            scope: {
                sourceConfig: '=',
				prompts: '=',
				fileUploadError: '&?',
				fileUploadSuccess: '&?'
            },
            bindToController: true,
            replace: false,
            templateUrl: 'eureka/directives/source-config-prompts/source-config-prompts.html',
            controller: 'SourceConfigPromptsCtrl',
            controllerAs: 'vm'
        };
    }
}());