(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('GlobalGridCtrl', GlobalGridCtrl);

    GlobalGridCtrl.$inject = ['CohortTreeService', 'CohortFilterService'];

    function GlobalGridCtrl(CohortTreeService, CohortFilterService) {
        let vm = this;


    }
}());
