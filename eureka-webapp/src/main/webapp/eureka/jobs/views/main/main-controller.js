(function () {
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

	MainCtrl.$inject = ['JobService', '$interval', '$scope', 'appProperties'];

	function MainCtrl(JobService, $interval, $scope, appProperties) {
		var vm = this,
				totalFiles,
				filesCompleted;
		vm.radioData = 1;
		vm.treeMultiDropZoneItems = [];
		vm.dataEndpoint = appProperties.dataEndpoint;

		vm.earliestDatePopup = {
			opened: false
		};

		vm.openEarliestDatePopup = function () {
			vm.earliestDatePopup.opened = true;
		};

		vm.latestDatePopup = {
			opened: false
		};

		vm.openLatestDatePopup = function () {
			vm.latestDatePopup.opened = true;
		};

		vm.fileUploadSuccess = function (file) {
			filesCompleted++;
			if (filesCompleted === totalFiles) {
				_submitJob();
			}
		};

		vm.fileUploadError = function (file, message) {
			displayFileUploadError('Failed to upload "' + file + '": ' + message);
		};

		vm.submitJob = function () {
			filesCompleted = 0;
			totalFiles = function () {
				var total = 0;
				if (vm.sourceConfigPrompts) {
					angular.forEach(vm.sourceConfigPrompts, function (prompt) {
						if (prompt.fileInput) {
							total++;
						}
					});
				}
				return total;
			}();

			if (totalFiles > 0) {
				angular.forEach(vm.sourceConfigPrompts, function (prompt) {
					if (prompt.fileInput) {
						let fileInput = prompt.fileInput;
						fileInput.opts.target = vm.dataEndpoint + '/file/upload/' + prompt.sourceConfig.id + '/' + prompt.option.name;
						fileInput.upload();
					}
				});
			} else {
				_submitJob();
			}
		};

		function _submitJob() {
			let jobSpec = {
				sourceConfigId: vm.jobSourceConfig.id,
				destinationId: vm.jobDestination.name,
				dateRangePhenotypeKey: vm.dateRangeDropZoneItem ? vm.dateRangeDropZoneItem.key : null,
				earliestDate: vm.earliestDate,
				earliestDateSize: vm.earliestDateSide,
				latestDate: vm.latestDate,
				latestDateSize: vm.latestDateSide,
				prompts: {
					id: vm.jobSourceConfig.id,
					dataSourceBackends: function () {
						let dataSourceBackends = [];
						let dataSourceBackendCache = {};
						angular.forEach(vm.sourceConfigPrompts, function (item) {
							if (!(item.dataSourceBackend.id in dataSourceBackendCache)) {
								let dataSourceBackend = {
									id: item.dataSourceBackend.id,
									options: []
								};
								dataSourceBackendCache[item.dataSourceBackend.id] = dataSourceBackend;
								dataSourceBackends.push(dataSourceBackend);
							}
							dataSourceBackendCache[item.dataSourceBackend.id].options.push(
									function () {
										if (item.option.type === 'FILE') {
											return {
												type: 'FILE',
												name: item.option.name,
												value: item.fileInput.files.length > 0 ? item.fileInput.files[0].name : null
											};
										} else {
											return {
												type: 'DEFAULT',
												name: item.option.name,
												value: item.option.value
											};
										}
									}());
						});

						return dataSourceBackends;
					}()
				},
				propositionIds: function () {
					return vm.treeMultiDropZoneItems.map(function (item) {
						return item.name;
					});
				}()
			};
			JobService.submitJob(jobSpec).then(
					function () {
					},
					function (msg) {
						displayJobSubmitError(msg);
					});
		}

		function displayFileUploadError(msg) {
			vm.jobSubmitErrorMsg = msg;
		}

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

		function displayDateRangeDropZoneItemError(msg) {
			vm.dateRangeDropZoneItemErrorMsg = msg;
		}

		function getLatestJobs() {
			JobService.getLatestJobs()
					.then(function (data) {
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
				.then(function (data) {

					vm.destinations = data;

					vm.jobDestination = vm.destinations[0];
				}, displayLoadError);


		JobService.getSourceConfigs()
				.then(function (data) {

					vm.sourceConfigs = data;

					vm.jobSourceConfig = vm.sourceConfigs[0];
				}, displayLoadError);

		function routeChange(event, toState, toParams, fromState, fromParams) {
			$interval.cancel(stopTime);
			onRouteChangeOff();
		}

		function earliestDate(ed) {
			console.log(ed);
		}

		function latestDate(ld) {
			console.log(ld);
		}
	}
})();