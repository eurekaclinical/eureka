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
        vm.radioData = 1;
        
        vm.earliestDate = earliestDate;
        vm.latestDate = latestDate;
        
        function displayError(msg) {
            console.log(msg);
            vm.errorMsg = msg;
        }
        function success(msg) {
            console.log(msg);
            vm.successMsg = msg;
        }    

        JobService.getLatestJobs()
                .then(function(data){
            
            vm.jobsLatest = data;
            
            vm.jobLatest = vm.jobsLatest[0];
            
            vm.jobInfoSourceConfig = vm.jobLatest.sourceConfigId;
            
            vm.jobInfoDestination = vm.jobLatest.destinationId;
            
            vm.jobInfoStatus = vm.jobLatest.status;
            
            vm.jobInfoStartTimestamp = vm.jobLatest.startTimestamp;
            
            vm.jobInfoFinishTimestamp = vm.jobLatest.finishTimeStamp;
        });          
        
        JobService.getDestinations()
                .then(function(data){
            
            vm.destinations = data;
            
            vm.jobDestination = vm.destinations[0];
        }); 
        
        
        JobService.getSourceConfigs()
                .then(function(data){
            
            vm.sourceConfigs = data;
            
            vm.jobSourceConfig = vm.sourceConfigs[0];
        });      
        
        function earliestDate(ed){
            console.log(ed);
        }
        
        function latestDate(ld){
            console.log(ld);
        }
    }
})();