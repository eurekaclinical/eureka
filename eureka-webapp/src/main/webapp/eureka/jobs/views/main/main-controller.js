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

    MainCtrl.$inject = ['JobService', '$interval'];

    function MainCtrl(JobService, $interval) {
        var vm = this;
        vm.radioData = 1;
        
        vm.earliestDatePopup = {
			opened: false
		};

        vm.openEarliestDatePopup = function() {
			vm.earliestDatePopup.opened = true;
		};

        vm.latestDatePopup = {
			opened: false
		};

        vm.openLatestDatePopup = function() {
			vm.latestDatePopup.opened = true;
		};
        
        function displayError(msg) {
            console.log(msg);
            vm.errorMsg = msg;
        }
        function success(msg) {
            console.log(msg);
            vm.successMsg = msg;
        }    

        function getLatestJobs() {
			JobService.getLatestJobs()
					.then(function(data){
				vm.jobsLatest = data;
				vm.jobLatest = vm.jobsLatest[0];
                vm.jobId = vm.jobLatest.id;
				vm.jobInfoSourceConfig = vm.jobLatest.sourceConfigId;
				vm.jobInfoDestination = vm.jobLatest.destinationId;
				vm.jobInfoStatus = vm.jobLatest.status;
				vm.jobInfoStartTimestamp = vm.jobLatest.startTimestamp;
				vm.jobInfoFinishTimestamp = vm.jobLatest.finishTimestamp;
                vm.jobLinks = vm.jobLatest.status === 'COMPLETED' ? vm.jobLatest.links : [];
				vm.jobStatisticsSupported = vm.jobLatest.status === 'COMPLETED' ? vm.jobLatest.getStatisticsSupported : false;
			});
        }

        getLatestJobs();
        let stopTime = $interval(function () {
            getLatestJobs();
        }, 5000);
        angular.element('#jobInformation').on('$destroy', function () {
            $interval.cancel(stopTime);
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