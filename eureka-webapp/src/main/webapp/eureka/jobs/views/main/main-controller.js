(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.jobs.controller:MainCtrl
     * @description
     * This is the main controller for the jobs section of the application.
     * @requires jobs.JobService
     */

    angular
        .module('eureka.jobs')
        .controller('jobs.MainCtrl', MainCtrl);
        
    MainCtrl.$inject = ['JobService'];
    
    function MainCtrl(JobService) {
        var vm = this;

        function displayError(msg) {
            vm.errorMsg = msg;
        }

        function success(cohorts) {
            vm.cohortsList = cohorts;
        }
        /*
        function getJobs(){

        }

        function uploadfile(){
            
        } */


    }
})();