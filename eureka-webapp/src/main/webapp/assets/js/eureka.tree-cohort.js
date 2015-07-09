// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.tree = new function () {
    var self = this;

    var initData = null;

    self.setupUserTree = function (userTreeElem, dropFinishCallback) {
        $(userTreeElem).jstree({
            "core": {
                "data": {
                    "url": "protected/userproplist?key=root",
                    "dataType": 'json',
                },

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
            "core": {
                "data": {
                    "url": "protected/systemlist",
                    "dataType": 'json',
                    "data": function (n) {
                        return {
                            key: n.id === "#" ? "root" : n.id
                        };
                    },

                },

            },

            'themes': {
                'name': 'default',
                'theme': 'default',
                'url': treeCssUrl
            },
            "plugins": [ "themes", "json_data", "ui", "crrm", "dnd", "search" ]
        });

        $(document).on('dnd_stop.vakata', function (e, data) {
            eureka.editor.dropFinishCallback(data);

        });

        $(document).on('dnd_move.vakata', function (e, data) {
            var t = $(data.event.target);
            if(!t.closest('.jstree').length) {
                if (t.closest('.tree-drop').length) {
                    data.helper.find('.jstree-icon').removeClass('jstree-er').addClass('jstree-ok');
                }
                else {
                    data.helper.find('.jstree-icon').removeClass('jstree-ok').addClass('jstree-er');
                }
            }


        });



        $(systemTreeElem).before(
            $('<form id="search">' +
                '<span></span>' +
                '<div class="input-group"><input id="searchText" class="form-control" type="text" /><div class="input-group-btn"><input id="searchTree" class="btn btn-default" type=submit value="search" /><input class="btn btn-default" type="reset" value="X" /></div></div>' +
                '</form>').
                bind({
                    reset: function(evt){
                        $('#systemTree').jstree('clear_search');
                        $(systemTreeElem).jstree(true).settings.core.data = initData;
                        $(systemTreeElem).jstree(true).refresh();
                        $('#search span').html('');
                    },

                    submit: function(evt){
                        $('#systemTree').jstree('clear_search');
                        var searchvalue = $('#search input[type="text"]').val();
                        initData = $(systemTreeElem).jstree(true).settings.core.data;
                        if(searchvalue!='' && searchvalue.length>=4) {
                            $('#systemTree').hide();
                            $('#userTree').hide();
                            $elem = $(searchUpdateDivElem);
                            $elem.text("Search is in progress. Please wait...");
                            $elem.show();


                            $(systemTreeElem).jstree("destroy");
                            $(systemTreeElem).jstree({
                                'core' : {
                                    'data': {
                                        'url': function (node) {
                                            return node.id === '#' ?
                                            "protected/jstree3_searchsystemlist?str="+searchvalue :
                                                "protected/systemlist";

                                        },
                                        "dataType": 'json',
                                        'data': function (node) {
                                            return {
                                                //'id': node.id
                                                key: node.id === "#" ? "root" : node.id

                                            };
                                        }
                                    }
                                },
                                'themes': {
                                    'name': 'default',
                                    'theme': 'default',
                                    'url': treeCssUrl
                                },
                                "plugins": [ "themes", "json_data", "ui", "crrm", "dnd", "search" ]
                            }).bind('loaded.jstree', function (e, data) {

                                if (data.instance._cnt == 0) {
                                    console.log("empty");
                                    $elem = $(searchNoResultsModalElem);
                                    $elem.find('#searchContent').html("There are no entries in our database that matched your search criteria.");
                                    $elem.modal("toggle");

                                    $elem.hide();
                                    $('#systemTree').jstree('clear_search');
                                    $(systemTreeElem).jstree(true).settings.core.data = initData;
                                    $(systemTreeElem).jstree(true).refresh();
                                    $('#searchText').val('');

                                    $('#systemTree').show();
                                    $('#userTree').show();

                                    $elem = $(searchUpdateDivElem);
                                    $elem.hide();

                                }
                                else if (data.instance._cnt > 200) {
                                    $elem = $(searchModalElem);
                                    $elem.find('#searchContent').html("The number of search results exceeded the  "+
                                        " maximum limit and all results might not be displayed."+
                                        " Please give a more specific search query to see all results.");
                                    $elem.modal("toggle");

                                    $elem.hide();
                                    $('#systemTree').jstree('clear_search');
                                    $(systemTreeElem).jstree(true).settings.core.data = initData;
                                    $(systemTreeElem).jstree(true).refresh();
                                    $('#searchText').val('');

                                    $('#systemTree').show();
                                    $('#userTree').show();

                                    $elem = $(searchUpdateDivElem);
                                    $elem.hide();

                                }

                            });


                            $elem.hide();
                            $('#systemTree').show();
                            $('#userTree').show();


                        } else if(searchvalue.length<4){
                            $elem = $(searchValidationModalElem);
                            $elem.find('#searchContent').html("Please enter a search value with length greater than 3.");
                            $elem.modal("toggle");
                            $('#systemTree').show();
                            $('#userTree').show();

                        }
                        return false;
                    }


                })
        );
    };
};

