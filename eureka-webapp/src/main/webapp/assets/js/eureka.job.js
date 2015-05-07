// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.job = new function () {
	var self = this;

	self.currentElement = null;

	self.setup = function (systemTreeElem, userTreeElem, treeCssUrl, uploadFormElem, earliestDateElem, latestDateElem, datePickerCssUrl, statusElem, searchModalElem,
	searchValidationModalElem,searchNoResultsModalElem,searchUpdateDivElem) {
		// Initialize widgets
		eureka.tree.setupSystemTree(systemTreeElem, treeCssUrl, searchModalElem, self.dropFinishCallback,searchValidationModalElem,searchNoResultsModalElem,searchUpdateDivElem);
		eureka.tree.setupUserTree(userTreeElem, treeCssUrl, self.dropFinishCallback);
		self.setupDatePicker(earliestDateElem);
		self.setupDatePicker(latestDateElem);
		self.setupDatePickerCss(datePickerCssUrl);

		// Create event handlers.
		var $uploadForm = $(uploadFormElem);
		$uploadForm.find('select[name="source"]').change(
			function () {
				var sourceId = $(this).find(":selected").val();
				self.updateInputFields(sourceId);
			}
		);

		$('input').change(
			function () {
				self.updateSubmitButtonStatus();
			});

		$uploadForm.submit(function () {
			var $dataElement = $uploadForm.find('ul[data-type="main"]').find('li').first();
			$("input[name='dateRangeDataElementKey']").val($dataElement.data('key'));
			return self.save($uploadForm);
		});

		self.setInitialStatus();

		function poll() {
			$.ajax({
				url: "jobpoll",
				success: function (data) {
					if (data) {
						$(statusElem).text(data.status);
						$('#sourceConfig').text(data.sourceConfigId);
						$('#destinationConfig').text(data.destinationId);
						$('#startedDate').text(data.startedDateFormatted);
						$('#finishedDate').text(data.finishedDateFormatted);
						$('#messages').text(data.mostRecentMessage);
						$('form#uploadForm').data('job-running', data.jobSubmitted);
						self.updateSubmitButtonStatus();
					}
				},
				error: function () {
					$(statusElem).text("Job status unavailable");
					$('#startedDate').empty();
					$('#finishedDate').empty();
					$('#messages').empty();
				},
				dataType: "json"
			});
		}

		poll();

		(function pollStatus() {
			setTimeout(function () {
				poll();
				pollStatus();
			}, 5000);
		})();

	};

	self.dropFinishCallback = function (data) {
		var target = data.e.currentTarget;
		var textContent = data.o[0].children[1].childNodes[1].textContent;

		if (self.currentElement != data.o[0].id) {
			var infoLabel = $(target).find('div.label-info');
			infoLabel.hide();

			var sortable = $(target).find('ul.sortable');
			var newItem = $('<li></li>')
				.attr("data-space", $(data.o[0]).data("space"))
				.attr("data-desc", textContent)
				.attr("data-type", $(data.o[0]).data("type"))
				.attr("data-subtype", $(data.o[0]).data("subtype") || '')
				.attr('data-key', $(data.o[0]).data("proposition") || $(data.o[0]).data('key'));


			//this loop is executed only during replacement of a system element when droptype==single. In all other
			// cases(adding element to multiple droptype lists, adding a new element to an empty list) the else
			// statement is executed.
			if ($(sortable).data('drop-type') === 'single' && $(sortable).find('li').length > 0) {
				var dialog = $('#deleteModal');
				$(dialog).find('#deleteContent').html('Are you sure you want to remove data element ' + $toRemove.text() + '?');
				$(dialog).find('#confirmButton').one('click', function (/*e*/) {
					$(sortable).empty();
					$(dialog).modal('toggle');
					self.addNewItemToList(data, sortable, newItem);
					self.currentElement = data.o[0].id;
				});
				$(dialog).modal("toggle");
			}
			else {
				self.currentElement = data.o[0].id;
				self.addNewItemToList(data, sortable, newItem);
			}
		}
		self.updateSubmitButtonStatus();
	};

	self.addNewItemToList = function (data, sortable, newItem) {
		var target = $(sortable).parent();
		var X = $("<span></span>", {
			'class': 'glyphicon glyphicon-remove delete-icon',
			'data-action': 'remove'
		});
		self.attachDeleteAction(X);

		newItem.append(X);
		newItem.append(data.o[0].children[1].childNodes[1].textContent);
		sortable.append(newItem);
	};

	self.attachDeleteAction = function (elem) {
		$(elem).each(function (i, item) {
			$(item).click(function () {
				var $toRemove = $(item).closest('li');
				var $sortable = $toRemove.closest('ul.sortable');
				var dialog = $('#deleteModal');
				$(dialog).find('#deleteContent').html('Are you sure you want to remove data element ' + $toRemove.text() + '?');
				$(dialog).find('#confirmButton').one('click', function (e) {
					$sortable.empty();
					self.currentElement = null;
					self.updateSubmitButtonStatus();
					$sortable.siblings('.label-info').show();
					$(dialog).modal('toggle');
				});
				$(dialog).modal("toggle");
			});
		});
	};

	self.setupDatePickerCss = function (cssUrl) {
		$('<link />')
			.prop('rel', 'stylesheet')
			.prop('type', 'text/css')
			.prop('href', cssUrl)
			.appendTo('head');
	};

	self.setupDatePicker = function (pickerElem) {
		$(pickerElem).datetimepicker({
			'pickTime': false,
			'showToday': true,
			'useCurrent': false
		});
		$(pickerElem).on('dp.change', function () {
			$(pickerElem).datetimepicker('hide');
			$(pickerElem).blur();
		});
	};

	self.setInitialStatus = function () {
		var sourceId = $("form#uploadForm").find('select[name="source"]').val();
		self.updateInputFields(sourceId);
	};

	self.updateInputFields = function (sourceId) {
		$(".uploads").each(function (i, r) {
			if ($(r).data("source-id") === sourceId) {
				$(r).find("input").prop('disabled', false);
				$(r).show();
			} else {
				$(r).find("input").prop('disabled', true);
				$(r).hide();
			}
		});
		self.updateSubmitButtonStatus();
	};

	self.updateSubmitButtonStatus = function () {
		var doDisable = false;
		if ($('form#uploadForm').data('job-running') || self.submittingJob) {
			doDisable = true;
		} else {
			$('input').each(function () {
				if (!$(this).prop('disabled') && $(this).data('required') && !$(this).val()) {
					doDisable = true;
				}
			});
		}

		// enable this if we want to disallow job submission without a data element selected
		// doDisable = doDisable || (self.currentElement == null);

		$('#startButton').prop('disabled', doDisable);
	};
	
	self.postFormData = function (postData) {
		$.ajax({
			type: "POST",
			url: 'upload',
			data: postData,
			contentType: false,
			processData: false,
			cache: false,
			mimeType: "multipart/form-data",
			success: function (jqXHR) {
				self.onSuccess();
			},
			error: function (jqXHR, statusCode, errorThrown) {
				self.onError(JSON.parse(jqXHR.responseText).message);
			}
		});
	}
	
	self.onSuccess = function () {
		$('form#uploadForm').data('job-running', true);
		self.submittingJob = false;
		self.updateSubmitButtonStatus();
	}
	
	self.onError = function (errorThrown) {
		self.showErrorSubmittingJobDialog(errorThrown);
		self.submittingJob = false;
		$('form#uploadForm').data('job-running', false);
		self.updateSubmitButtonStatus();
	}
	
	self.showErrorSubmittingJobDialog = function (errorThrown) {
		var content = 'Error submitting job: ' + errorThrown;
		$('#errorModal').find('#errorContent').html(content);
		$('#errorModal').modal('show');
	}
	
	self.save = function (uploadFormElem) {
		self.submittingJob = true;
		self.updateSubmitButtonStatus();
		var jobSpec = {
			sourceConfigId: uploadFormElem.find('select[name="source"]').find(":selected").val(),
			destinationId: uploadFormElem.find('select[name="destination"]').find(":selected").val(),
			dateRangeDataElementKey: uploadFormElem.find('input[name="dateRangeDataElementKey"]').val(),
			earliestDate: uploadFormElem.find('input[name="earliestDate"]').val(),
			earliestDateSide: uploadFormElem.find('select[name="dateRangeEarliestDateSide"]').find(":selected").val(),
			latestDate: uploadFormElem.find('input[name="latestDate"]').val(),
			latestDateSide: uploadFormElem.find('select[name="dateRangeLatestDateSide"]').find(":selected").val(),
			updateData: uploadFormElem.find('input[name="updateData"]:checked').val() === 'true' ? true : false,
			prompts: {
				id: uploadFormElem.find('select[name="source"]').find(":selected").val(),
				dataSourceBackends: []
			}
		};
		var prompts = null;
		uploadFormElem.find('.uploads[data-source-id="' + jobSpec.sourceConfigId + '"]').each(function() {
			prompts = $(this);
		});
		if (prompts) {
			$(prompts).find(".section").each(function() {
				var section = {
					id: $(this).data('section-id'),
					options: []
				};
				$(prompts).find(".uploader").each(function() {
					var optionName = $(this).data("option-name");
					$(this).find("input[type!='file']").each(function() {
						section.options.push({
							type: 'DEFAULT',
							name: optionName,
							value: $(this).val()
						});
					});
					$(this).find("input[type='file']").each(function() {
						section.options.push({
							type: 'FILE',
							name: optionName,
							value: $(this)[0].files[0].name
						});
					});
				});
				jobSpec.prompts.dataSourceBackends.push(section);
			});
		}
		if (window.FormData) {
			var formData = new FormData();
			$(prompts).find(".uploader").each(function() {
				var optionName = $(this).data("option-name");
				$(this).find("input[type='file']").each(function() {
					formData.append(optionName, $(this)[0].files[0], $(this)[0].files[0].name);
				});
			});
			formData.append("jobSpec", JSON.stringify(jobSpec));
			this.postFormData(formData);
			return false;
		} else {
			var iframeId = 'iframe' + (new Date().getTime());
			var iframe = $('<iframe src="javascript:false;" name="'+iframeId+'" />');
			iframe.hide();
			var theForm = $('#uploadForm');
			var theJobSpec = $('input[name="jobSpec"]');
			theJobSpec.val(JSON.stringify(jobSpec));
            theForm.attr("target", iframeId);
			iframe.appendTo('body');
			iframe.load(function(e) {
				var data = JSON.parse($(iframe[0].contentDocument.body).text());
				if (data.errorThrown) {
					self.onError(data.message);
				} else {
					self.onSuccess();
				}
				iframe.remove();
			});
			return true;
		}
	};
};