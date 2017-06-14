(function () {
	'use strict';

	angular
			.module('eureka')
			.controller('SourceConfigPromptsCtrl', SourceConfigPromptsCtrl);

	SourceConfigPromptsCtrl.$inject = ['$scope'];

	function SourceConfigPromptsCtrl($scope) {
		let vm = this;

		extractPrompts();

		function extractPrompts() {
			vm.prompts = [];
			if (vm.sourceConfig) {
				let i = 0;
				angular.forEach(vm.sourceConfig.dataSourceBackends, function (dsb) {
					angular.forEach(dsb.options, function (anOption) {
						if (anOption.prompt) {
							let prompt = {
								'option': anOption,
								'dataSourceBackend': dsb,
								'sourceConfig': vm.sourceConfig
								//We'll also get a fileInput property for file inputs, and possibly a value property from the form
							};
							if (anOption.type === 'FILE') {
								prompt.onUploadError = function (file, message) {
									if (vm.fileUploadError) {
										vm.fileUploadError({
											file: file.name,
											message: message}
										);
									}
								};
								prompt.onUploadSuccess = function (file) {
									if (vm.fileUploadSuccess) {
										vm.fileUploadSuccess({file: file.name});
									}
								};
							}
							vm.prompts.push(prompt);
							i++;
						}
					}
					);
				});
			}
		}

		$scope.$watch(function () {
			return vm.sourceConfig;
		}, function (newValue, oldValue) {
			extractPrompts();
		});
	}
}());