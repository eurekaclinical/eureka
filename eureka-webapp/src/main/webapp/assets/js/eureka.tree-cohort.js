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
                    "url": "/eureka-webapp/protected/userproplist?key=root",
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


};

