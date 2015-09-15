(function() {
    'use strict';

    angular
        .module('cohorts')
        .controller('cohorts.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['CohortService'];
    
    function MainCtrl(CohortService) {
        var vm = this;
        vm.remove = remove;
        getCohorts();

        function getCohorts() {
            CohortService.getCohorts().then(function (data) {
                vm.list = data;
            }, displayError);
        }

        function remove(key) {
            CohortService.removeCohort(key);
            for (var i = 0; i < vm.list.length; i++) {
                if (vm.list[i].name == key) {
                    vm.list.splice(i, 1);
                    break;
                }
            }
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }

     }
})();