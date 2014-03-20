// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.tree = new function () {
	var self = this;

	self.setupUserTree = function (userTreeElem, dropFinishCallback) {
		$(userTreeElem).jstree({
			"json_data": {
				"ajax": {
					"url": "userproplist?key=root"
				}
			},
			"dnd": {
				"drop_finish": function (data) {
					dropFinishCallback(data)
				}
			},
			"plugins": [ "themes", "json_data", "ui", "dnd" ]
		});
	};

	self.setupSystemTree = function (systemTreeElem, treeCssUrl, searchModalElem, dropFinishCallback) {
		$(systemTreeElem).jstree({
			"json_data": {
				"ajax": {
					"url": "systemlist",
					"data": function (n) {
						return {
							key: n.attr ? n.attr("data-key") : "root"
						};
					}

				}
			},
			"crrm": {
				// prevent movement and reordering of nodes
				"move": {
					"check_move": function (/*m*/) {
						return false;
					}
				}
			},
			"dnd": {
				"drop_finish": function (data) {
					dropFinishCallback(data)
				},
				"drop_check": function (data) {
					var target = data.r;
					var sortable = $(target).find('ul.sortable');
					var datatype = $(sortable).data("proptype");
					var droppable = false;

					if (datatype == "empty" || datatype == $(data.o).data("type") || $(sortable).data('drop-type') === 'single') {
						droppable = true;
					}
					return droppable;
				}
			},

			"search": {
				"show_only_matches": true,
				"ajax": {
					"url": "searchsystemlist",
					"data": function (n) {
						return {
							searchKey: n
						};
					},
					success: function (data) {
						console.log(data);
						if (data != null && data.length > 200) {
							$elem = $(searchModalElem);
							$elem.find('#searchContent').html("The search is in progress." +
								" The number of search results exceeded the maximum limit and all results might not be displayed." +
								" Please give a more specific search query to see all results.");
							$elem.modal("toggle");
						}
					}
				}
			},
			'themes': {
				'name': 'default',
				'theme': 'default',
				'url': treeCssUrl
			},
			"plugins": [ "themes", "json_data", "ui", "crrm", "dnd", "search" ]
		});

		$(systemTreeElem).before(
			$('<form id="search">' +
				'<span></span>' +
				'<div class="input-group"><input class="form-control" type="text" /><div class="input-group-btn"><input class="btn btn-default" type=submit value="search" /><input class="btn btn-default" type="reset" value="X" /></div></div>' +
//				'<div class="col-xs-6"><input class="form-control" type="text" value="" placeholder="Enter search term"></div>' +
//				'<div class="col-xs-2"><input class="form-control" type="submit" value="search"></div>' +
//				'<div class="col-xs-1"><input class="form-control" type="reset" value="X"></div>' +
				'</form>').
				bind({
					reset: function(evt){
						$('#systemTree').jstree('clear_search');
						$('#search span').html('');
					},
					submit: function(evt){
						var searchvalue = $('#search input[type="text"]').val();
						if(searchvalue!='' && searchvalue.length>=4) {
							$('#systemTree').jstree('search', searchvalue);
							$('#search span').html('');
						} else if(searchvalue.length<4){
							$('#systemTree').jstree('clear_search');
							var dialog = $('<div></div>');
							$(dialog).dialog({
								'title': 'Search update.',
								'modal': true,
								'resizable': false,
								'buttons': {
									"OK": function() {
										$(this).dialog("close");
										$(this).remove();
									}
								}
							});
							$(dialog).html("Please enter a search value with length greater than 3.");
							$(dialog).dialog("open");

							//$('#search span').html('Please enter searchvalue');
						}
						return false;
					}
				})
		);
	};
};
