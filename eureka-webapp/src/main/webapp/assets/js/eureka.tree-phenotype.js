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

    self.createData = function(systemElement) {
        /*
         JsonTreeData d = new JsonTreeData();
         d.setState("closed");
         d.setData(this.getDisplayName(element));
         d.setKeyVal("id", element.getKey());

         String properties = StringUtils.join(element.getProperties(), ",");
         d.setKeyVal("data-properties", properties);
         d.setKeyVal("data-key", element.getKey());
         d.setKeyVal("data-space", "system");
         d.setKeyVal("data-type", element.getSystemType().toString());
         d.setKeyVal("data-proposition", element.getKey());

         return d;
         */
        var jsonData = new Object();
        jsonData.state = "closed";
        jsonData.data = "Test";
        jsonData.id = "1";
        jsonData["data-key"] = "1";
        jsonData["data-space"] = "1";
        jsonData["data-type"] = "1";
        jsonData["data-proposition"] = "1";

        return jsonData;
    }

    self.setupSystemTree = function (systemTreeElem, treeCssUrl, searchModalElem, dropFinishCallback,searchValidationModalElem,searchNoResultsModalElem,searchUpdateDivElem) {
        $(systemTreeElem).jstree({
            //"core": {
            //    "data": {
            //        "url": "protected/systemlist",
            //        "dataType": 'json',
            //        //"url": "/eureka-services/api/protected/systemelement",
            //        "data": function (n) {
            //            return {
            //                key: n.id === "#" ? "root" : n.id
            //            };
            //        },
            //    },
            //
            //},

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
                            "str": n
                        };
                    },
                    success: function (data) {
                        console.log(data);
                        $elem = $(searchUpdateDivElem);
                        $elem.hide();
                        $('#systemTree').show();
                        $('#userTree').show();
                        if(data.length==1){
                            /*var dialog = $('<div></div>');
                             $(dialog).dialog({
                             'title': 'No Search Results',
                             'modal': true,
                             'resizable': false,
                             'buttons': {
                             "OK": function() {
                             $(this).dialog("close");
                             $(this).remove();
                             }
                             }
                             });
                             $(dialog).html("There are no entries in our database that matched your search criteria.");
                             $(dialog).dialog("open");   */
                            $elem = $(searchNoResultsModalElem);
                            $elem.find('#searchContent').html("There are no entries in our database that matched your search criteria.");
                            $elem.modal("toggle");
                        }
                        if (data != null && data.length > 200) {
                            $elem = $(searchModalElem);
                            $elem.find('#searchContent').html("The number of search results exceeded the  "+
                                " maximum limit and all results might not be displayed."+
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
                        $('#systemTree').jstree('clear_search');
                        var searchvalue = $('#search input[type="text"]').val();
                        if(searchvalue!='' && searchvalue.length>=4) {
                            $('#systemTree').hide();
                            $('#userTree').hide();
                            $elem = $(searchUpdateDivElem);
                            $elem.text("Search is in progress. Please wait...");
                            $elem.show();
                            //$('#searchUpdateDivElem').text("Search is in progress. Please wait...");
                            //$('#searchUpdateDivElem').show();
                            $('#systemTree').jstree('search', searchvalue);
                            $('#search span').html('');
                        } else if(searchvalue.length<4){
                            $elem = $(searchValidationModalElem);
                            $elem.find('#searchContent').html("Please enter a search value with length greater than 3.");
                            $elem.modal("toggle");

                            /*var dialog = $('<div></div>');
                             $(dialog).dialog({
                             'title': 'Search String Validation Failed',
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
                             $(dialog).dialog("open");        */

                            //$('#search span').html('Please enter searchvalue');
                        }
                        return false;
                    }
                })
        );
    };
};

