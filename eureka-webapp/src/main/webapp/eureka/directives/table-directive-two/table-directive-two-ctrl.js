(function() {
    'use strict';

    angular
        .module('eureka')
        .controller('TableDirectiveCtrl', TableDirectiveCtrl);

    TableDirectiveCtrl.$inject = ['CohortTreeService', 'CohortFilterService'];

    function TableDirectiveCtrl(CohortTreeService, CohortFilterService) {
        let vm = this;


    }
}());
