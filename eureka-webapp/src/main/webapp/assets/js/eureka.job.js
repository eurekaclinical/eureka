// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.job = new function () {
	var self = this;

	self.currentElement = null;

	self.setup = function (systemTreeElem, userTreeElem, treeCssUrl, uploadFormElem, earliestDateElem, latestDateElem, datePickerCssUrl, statusElem, searchModalElem) {
		// Initialize widgets
		eureka.tree.setupSystemTree(systemTreeElem, treeCssUrl, searchModalElem, self.dropFinishCallback);
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

		$('input:file').change(
			function () {
				self.updateSubmitButtonStatus();
			});

		var submitted = false;
		$uploadForm.submit(function () {
			$uploadForm.data('job-running', true);
			var $dataElement = $uploadForm.find('ul[data-type="main"]').find('li').first();
			$("input[name='dateRangeDataElementKey']").val($dataElement.data('key'));
			self.updateSubmitButtonStatus();
			submitted = true;
		});

		var running = self.setInitialStatus();

		function poll() {
			if (!submitted) {
				var jobId = $('form#uploadForm').data('jobid');
				$.ajax({
					url: "jobpoll" + (jobId != null ? "?jobId=" + jobId : ""),
					success: function (data) {
						if (!submitted) {
							if (data) {
								$(statusElem).text(data.status);
								$('#startedDate').text(data.startedDateFormatted);
								$('#finishedDate').text(data.finishedDateFormatted);
								$('#messages').text(data.mostRecentMessage);
								if (running && !data.jobSubmitted) {
									self.onFinish();
									running = false;
								}
							}
						}
					},
					error: function (/*xhr*/) {
						if (!submitted) {
							$(statusElem).text("Job status unavailable");
							$('#startedDate').empty();
							$('#finishedDate').empty();
							$('#messages').empty();
						}
					},
					dataType: "json"
				});
			}
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
		var running = false;
		if ($('form#uploadForm').data('job-running')) {
			running = true;
		} else {
			//$('#jobUpload').hide();
		}
		var sourceId = $("form#uploadForm").find('select[name="source"]').val();
		self.updateInputFields(sourceId);
		return running;
	};

	self.onFinish = function () {
		$('#uploadForm').prop('disabled', false);
		//$('#jobUpload').hide();
		$('form#uploadForm').data('job-running', false);
		self.updateSubmitButtonStatus();
	};

	self.updateInputFields = function (sourceId) {
		$(".uploads").each(function (i, r) {
			if ($(r).attr('id') === 'uploads' + sourceId) {
				$(r).find("input[type='file']").prop('disabled', false);
				$(r).show();
			} else {
				$(r).find("input[type='file']").prop('disabled', true);
				$(r).hide();
			}
		});
		self.updateSubmitButtonStatus();
	};

	self.updateSubmitButtonStatus = function () {
		var doDisable = false;
		if ($('form#uploadForm').data('job-running')) {
			doDisable = true;
		} else {
			$('input[type="file"]').each(function () {
				if (!$(this).prop('disabled') && $(this).data('required') && !$(this).val()) {
					doDisable = true;
				}
			});
		}

		// enable this if we want to disallow job submission without a data element selected
		// doDisable = doDisable || (self.currentElement == null);

		//$('input:submit').prop('disabled', doDisable);
	};
};