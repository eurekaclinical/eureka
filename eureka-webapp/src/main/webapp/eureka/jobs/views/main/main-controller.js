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

    MainCtrl.$inject = ['JobService', '$interval', '$scope'];

    function MainCtrl(JobService, $interval, $scope) {
        var vm = this;
        vm.radioData = 1;
		vm.treeMultiDropZoneItems = [];
        
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

        function displayJobStatusError(msg) {
            vm.jobStatusErrorMsg = msg;
        }

		function displayJobSubmitError(msg) {
            vm.jobSubmitErrorMsg = msg;
        }

        function displayLoadError(msg) {
            vm.loadErrorMsg = msg;
        }
		
		function displayTreeMultiDropZoneItemsError(msg) {
			vm.treeMultiDropZoneItemsErrorMsg = msg;
		}

        function getLatestJobs() {
			JobService.getLatestJobs()
					.then(function(data){
                vm.jobStatusErrorMsg = null;
				vm.jobsLatest = data;
                if (vm.jobsLatest && vm.jobsLatest.length > 0) {
					vm.jobLatest = vm.jobsLatest[0];
					vm.jobId = vm.jobLatest.id;
					vm.jobInfoSourceConfig = vm.jobLatest.sourceConfigId;
					vm.jobInfoDestination = vm.jobLatest.destinationId;
					vm.jobInfoStatus = vm.jobLatest.status;
					vm.jobInfoStartTimestamp = vm.jobLatest.startTimestamp;
					vm.jobInfoFinishTimestamp = vm.jobLatest.finishTimestamp;
					vm.jobLinks = vm.jobLatest.status === 'COMPLETED' ? vm.jobLatest.links : [];
					vm.jobStatisticsSupported = vm.jobLatest.status === 'COMPLETED' ? vm.jobLatest.getStatisticsSupported : false;
                }
			}, displayJobStatusError);
        }

        getLatestJobs();
        let stopTime = $interval(function () {
            getLatestJobs();
        }, 5000);
        let onRouteChangeOff = $scope.$on('$stateChangeStart', routeChange);
        
        JobService.getDestinations()
                .then(function(data){
            
            vm.destinations = data;
            
            vm.jobDestination = vm.destinations[0];
        }, displayLoadError); 
        
        
        JobService.getSourceConfigs()
                .then(function(data){
            
            vm.sourceConfigs = data;
            
            vm.jobSourceConfig = vm.sourceConfigs[0];
        }, displayLoadError);
 
        function routeChange(event, toState, toParams, fromState, fromParams) {
            $interval.cancel(stopTime);
            onRouteChangeOff();
        }
        
        function earliestDate(ed){
            console.log(ed);
        }
        
        function latestDate(ld){
            console.log(ld);
        }
    }
})();