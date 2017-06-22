(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('phenotypeConstraints', PhenotypeConstraints);

    function PhenotypeConstraints() {
        return {
            restrict: 'AE',
            scope: {
				conceptOrPhenotypeKey: '=?',
                conceptOrPhenotype: '=',
				minDuration: '=',
				minDurationUnits: '=',
				maxDuration: '=',
				maxDurationUnits: '=',
				propertyName: '=',
				propertyValue: '=',
				onConceptOrPhenotypeError: '&?',
				onTimeUnitsError: '&?'
            },
            bindToController: true,
            replace: false,
            templateUrl: 'eureka/directives/phenotype-constraints/phenotype-constraints.html',
            controller: 'PhenotypeConstraintsCtrl',
            controllerAs: 'vm'
        };
    }
}());