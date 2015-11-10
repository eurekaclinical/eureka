// get the namespace, or declare it here
window.eureka = (typeof window.eureka == "undefined" || !window.eureka ) ? {} : window.eureka;

// add in the namespace
window.eureka.editor = new function () {

    var self = this;

    self.propId = null;
    self.propType = null;
    self.propSubType = null;
    self.droppedElements = {};

    self.setup = function (propId, userTreeElem, containerElem, saveButtonElem) {
        self.propId = propId;

        if (propId != null && propId != '') {
            $('.label-info').hide();
        }

        $(saveButtonElem).on('click', function () {
            self.save($(containerElem));
        });

        eureka.tree.setupUserTree(userTreeElem, self.dropFinishCallback);
    };

    self.attachClearModalHandlers = function () {
        var deleteModal = $('#deleteModal');
        if (deleteModal) {
            var $deleteButton = $(deleteModal).find('#deleteButton');
            $(deleteModal).on('hidden.bs.modal', function(e) {
                $deleteButton.off('click');
            });
        }
        var replaceModal = $('#replaceModal');
        if (replaceModal) {
            var $replaceButton = $(replaceModal).find('#replaceButton');
            $(replaceModal).on('hidden.bs.modal', function(e) {
                $replaceButton.off('click');
            });
        }
    };


    self.collectDataElements = function ($dataElementsFromDropBox) {
        var childElements = new Array();

        $dataElementsFromDropBox.each(function (i, p) {
            childElements.push(self.collectDataElement(p));
        });

        return childElements;
    };



    self.post = function (postData) {
        var user = null;
        $.ajax({
            url: '/eureka-services/api/protected/users/me',
            async: false,
            dataType: 'json',
            success: function(data) {
                user = data;
            },
            error: function (data, statusCode, errorThrown) {
                var content = 'Error while saving ' + postData.displayName + '. ' + data.responseText + '. Status Code: ' + statusCode;
                $('#errorModal').find('#errorContent').html(content);
                $('#errorModal').modal('show');
                if (errorThrown != null) {
                    console.log(errorThrown);
                }
            }
        });

        if (user != null) {

            var cohortDestination = {};
            var type = "";
            if (postData.id != null) {
                cohortDestination.id = postData.id;
                type = "PUT";
            }
            else {
                cohortDestination.id = null;
                type = "POST";
            }

            cohortDestination.type = 'COHORT';
            cohortDestination.ownerUserId = user.id;
            cohortDestination.name = postData['name'];
            cohortDestination.description = postData['description'];
            cohortDestination.dataElementFields = null;
            cohortDestination.cohort = self.toCohort(postData.phenotypes);

            cohortDestination.read = false;
            cohortDestination.write = false;
            cohortDestination.execute = false;

            cohortDestination.created_at = null;
            cohortDestination.updated_at = null;

            cohortDestination.links = null;
            console.log(JSON.stringify(cohortDestination));



            $.ajax({
                type: type,
                //url: 'savecohort',
                url: '/eureka-webapp/proxy-resource/destinations',
                data: JSON.stringify(cohortDestination),
                //data: JSON.stringify(postData),
                success: function (data) {
                    //window.location.href = '/protected/cohort_home'
                    window.location.href = '#/cohort_home'
                },
                error: function (data, statusCode, errorThrown) {
                    var content = 'Error while saving ' + postData.displayName + '. ' + data.responseText + '. Status Code: ' + statusCode;
                    $('#errorModal').find('#errorContent').html(content);
                    $('#errorModal').modal('show');
                    if (errorThrown != null) {
                        console.log(errorThrown);
                    }
                }
            });


        }
    }

    self.toCohort = function (phenotypes) {
        var cohort = {id: null};
        var node = {id: null, start: null, finish: null, type: 'Literal'};
        if (phenotypes.length == 1) {
            node.name = phenotypes[0].dataElementKey;
        } else if (phenotypes.length > 1) {
            first = true;
            prev = null;
            for (var i = phenotypes.length - 1; i >= 0; i--) {
                var literal = {id: null, start: null, finish: null, type: 'Literal'};
                literal.name = phenotypes[i].dataElementKey;
                if (first) {
                    first = false;
                    prev = literal;
                } else {
                    var binaryOperator = {id: null, type: 'BinaryOperator', op: 'OR'};
                    binaryOperator.left_node = literal;
                    binaryOperator.right_node = prev;
                    prev = binaryOperator;
                }
            }
            node = prev;
        } else {
            node = null;
        }
        cohort.node = node;
        return cohort;

    }



    self.collectDataElement = function (dataElementFromDropBox) {
        var system = $(dataElementFromDropBox).data('space') === 'system'
            || $(dataElementFromDropBox).data('space') === 'SYSTEM';
        var child = {
            'dataElementKey': $(dataElementFromDropBox).data('key')
        };

        if (system) {
            child['type'] = 'SYSTEM';
        } else {
            child['type'] = $(dataElementFromDropBox).data('type');
        }
        return child;
    };

    self.saveCohort = function (elem) {
        var $memberDataElements = $(elem).find('ul.sortable').find('li');
        var childElements = self.collectDataElements($memberDataElements);

        var categorization = {
            'name': $('input#patCohortDefName').val(),
            'description': $('textarea#patCohortDescription').val(),
            'phenotypes': childElements
        }

        if (self.propId) {
            categorization.id = self.propId;
        }

        self.post(categorization);
    };

    self.validateCohort = function (elem) {
        var name = $('input#patCohortDefName').val()
        return name != null && name.length > 0;
    }

    self.save = function (containerElem) {
        if (self.validateCohort(containerElem)) {
            self.saveCohort(containerElem);
        } else {
            var content = 'Please ensure that the cohort name is filled out.';
            $('#errorModal').find('#errorContent').html(content);
            $('#errorModal').modal('show');
        }
    };

};

