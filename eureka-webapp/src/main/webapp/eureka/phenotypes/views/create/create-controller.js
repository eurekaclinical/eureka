(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.phenotypes.controller:CreateCtrl
     * @description
     * This is the create controller for the phenotypes section of the application.
     * @requires $scope
     * @requires $location
     * @requires eureka.phenotypes.PhenotypeService
     */

    angular
        .module('eureka.phenotypes')
        .controller('phenotypes.CreateCtrl', CreateCtrl);

    CreateCtrl.$inject = ['$stateParams', 'PhenotypeService', 'NgTableParams', 'dragAndDropService', 'TreeService', 'users', '$state'];

    function CreateCtrl($stateParams, PhenotypeService, NgTableParams, dragAndDropService, TreeService, users, $state) {

        // global items
        let vm = this;
        let currentUser;
        let createPhenotype = PhenotypeService.createPhenotype;

        // doing this on every page to make sure the user is authenticated if he/she refresh.  Will need to be changed but for now no time to refactor JS
        users.getUser().then(function(user) {
            currentUser = user;
        });
        vm.showPhenotypes = false;
        vm.type = _.startCase($stateParams.type);
        // Tree component
        vm.memberList = [];
        vm.myList = [];

        // Categorization
        vm.currentObject = {};
        vm.currentObject.displayName = '';
        vm.currentObject.description = '';
        vm.currentObject.children = [];

        // Frequency
        vm.frequencyObject = {};
        vm.frequencyObject.phenotype = {};
        vm.frequencyObject.phenotype.hasDuration = false;
        vm.frequencyObject.phenotype.minDuration = '';
        vm.frequencyObject.phenotype.minDurationUnits = '1';
        vm.frequencyObject.phenotype.maxDuration = '';
        vm.frequencyObject.phenotype.maxDurationUnits = '1';
        vm.frequencyObject.phenotype.hasPropertyConstraint = false;
        vm.frequencyObject.phenotype.property = 'patientId';
        vm.frequencyObject.phenotype.propertyValue = '';
        vm.frequencyObject.isWithin = false;
        vm.frequencyObject.withinAtLeast = '';
        vm.frequencyObject.frequencyType = '1';
        vm.frequencyObject.withinAtLeastUnits = '1';
        vm.frequencyObject.withinAtMost = '';
        vm.frequencyObject.withinAtMostUnits = '1';

        // Sequence
        vm.seqObject = {};
        vm.seqObject.primaryPhenotype = {};
        vm.seqObject.relatedPhenotypes = [];
        vm.seqObject.primaryPhenotype.hasDuration = false;
        vm.seqObject.primaryPhenotype.minDuration = '';
        vm.seqObject.primaryPhenotype.minDurationUnits = '1';
        vm.seqObject.primaryPhenotype.maxDuration = '';
        vm.seqObject.primaryPhenotype.maxDurationUnits = '1';
        vm.seqObject.primaryPhenotype.hasPropertyConstraint = false;
        vm.seqObject.primaryPhenotype.property = 'patientId';
        vm.seqObject.primaryPhenotype.propertyValue = '';
        // vm.seqObject.relatedPhenotypes = [];
        vm.seqMainPhenotype = [];
        vm.setCurrentSequence = 0;
        vm.currentPanel = '';
        //for relational phenotypes
        vm.isRelationalPhenotype = false;
        vm.phenotypeProperties = [];
        vm.releatedTemporalArray = [];

        // Threshold
        vm.thresholdObject = {};
        vm.thresholdObject.thresholdsOperator = '1';
        vm.thresholdObject.valueThresholds = [];
        vm.thresholdCompValues = [{ value: '1', display: '=' }, { value: '2', display: 'not=' }, { value: '3', display: '>' }, { value: '4', display: '>=' }];
        vm.thresholdtypes = [{ value: '1', display: 'any' }, { value: '2', display: 'all' }];
        vm.thresholdRelationTypes = [{ value: '1', display: 'before' }, { value: '2', display: 'after' }, { value: '2', display: 'around' }];

        // Show hide tree on right side
        vm.viewObject = {};
        vm.viewObject.showTree = false

        // More global items
        vm.timeUnits = [{ name: 'minutes', value: '3' }, { name: 'hours', value: '2' }, { name: 'days', value: '1' }];
        vm.thresholdUnits = ['at least', 'first'];
        vm.sequenceRelations = [{ name: 'after', value: '2' }, { name: 'before', value: '1' }];


        switch ($stateParams.type) {
            case 'categorization':
                vm.description = 'This category data element may be used wherever its member data elements are accepted.';
                break;
            case 'sequence':
                vm.description = 'Computes intervals with the same start and stop time as the Main data element below when ' +
                    'the temporal relationships below are satisfied.';
                break;
            case 'frequency':
                vm.description = 'Computes an interval over the temporal extent of the intervals contributing to the ' +
                    'specified frequency count below.';
                break;
            case 'threshold':
                vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
                break;
        }

        vm.openMenu = function($mdOpenMenu, evt) {
            $mdOpenMenu(evt);
        };

        // Categorization
        vm.addCategorizationPhenotype = function() {
            vm.viewObject.showTree = true;
        };

        vm.createCategorization = function() {
            var children = [];
            for (var i = 0; i < vm.myList.length; i++) {
                var childrenObject = {};
                childrenObject.phenotypeKey = vm.myList[i].key;
                childrenObject.type = vm.myList[i].type;

                vm.currentObject.children.push(childrenObject);
            }
            vm.currentObject.type = 'CATEGORIZATION';
            vm.currentObject.categoricalType = null;
            vm.currentObject.userId = currentUser.info.id;
            // PhenotypeService.createPhenotype(vm.currentObject);
            createPhenotype(vm.currentObject).then(data => {
                // if successful we prob need to redirect back to the main table
                console.log('we made it back', data);
                $state.transitionTo('phenotypes');
            }, displayError);

        };


        // Frequency
        vm.addFrequencyPhenotype = function() {
            vm.viewObject.showTree = true;
        };

        vm.createFrequency = function() {
            vm.frequencyObject.type = 'FREQUENCY';
            vm.frequencyObject.isConsecutive = false;
            vm.frequencyObject.phenotype.phenotypeKey = vm.myList[0].key;

            if (vm.frequencyThreshold === 'at least') {
                vm.frequencyObject['atLeast'] = vm.frequencyThresholdValue;
            } else {
                vm.frequencyObject['first'] = vm.frequencyThresholdValue;
            }
            vm.frequencyObject.userId = currentUser.info.id;
            console.log(JSON.stringify(vm.frequencyObject));

            createPhenotype(vm.frequencyObject).then(data => {
                $state.transitionTo('phenotypes');
            }, displayError);
        };


        // Threshold
        vm.createThreshold = function() {
            // This type is wrong.  Needs to be changed
            vm.thresholdObject.type = 'VALUE_THRESHOLD';
            vm.thresholdObject.userId = currentUser.info.id;
            // We need to loop through the objects and build the correct item to pass back
            for (var i = 0; i < vm.thresholdObject.valueThresholds.length; i++) {


                delete vm.thresholdObject.valueThresholds[i].$$hashKey;

                let currentItem = vm.thresholdObject.valueThresholds[i].phenotype.relatedPhenotypeKey;


                delete vm.thresholdObject.valueThresholds[i].relatedPhenotypeKey;

                let myNewObject = {};
                myNewObject.phenotypeKey = currentItem;
                myNewObject.type = 'SYSTEM';
                vm.thresholdObject.valueThresholds[i].relatedPhenotypes.push(myNewObject);

            }

            console.log(JSON.stringify(vm.thresholdObject));
            //var testObj = { 'primaryPhenotype': { 'hasDuration': true, 'minDuration': 1, 'minDurationUnits': '1', 'maxDuration': 1, 'maxDurationUnits': '1', 'hasPropertyConstraint': true, 'property': 'patientId', 'propertyValue': null, 'phenotypeKey': 'Patient' }, 'relatedPhenotypes': [{ 'phenotypeField': { 'phenotypeKey': 'Patient', 'hasDuration': true, 'minDuration': 2, 'minDurationUnits': '1', 'maxDuration': 2, 'maxDurationUnits': '1', 'hasPropertyConstraint': true, 'property': 'patientId' }, 'relationOperator': '2', 'sequentialPhenotype': 'Patient', 'sequentialPhenotypeSource': '1', 'relationMinCount': 2, 'relationMinUnits': '1', 'relationMaxCount': 4, 'relationMaxUnits': '1' }], 'displayName': 'ONETIME0987', 'description': 'test', 'type': 'SEQUENCE', 'userId': 1 };
            createPhenotype(vm.thresholdObject).then(data => {
                $state.transitionTo('phenotypes');
            }, displayError);

        };

        vm.addPhenotypeToThreshold = function(indexPanel, isRelational) {
            vm.currentPanel = indexPanel;
            vm.viewObject.showTree = true;
            if (isRelational === undefined) {
                // do nothing
            } else {
                // it is a relational phenotype.
                vm.isRelationalPhenotype = true;
            }

        };

        vm.addNewThreshold = function() {
            let openObject = {};
            let relatedObject = {};
            openObject.phenotype = {};

            openObject.relatedPhenotypes = [];
            vm.thresholdObject.valueThresholds.push(openObject);
        };

        function addMembersThreshold() {
            // adding related items
            vm.thresholdObject.valueThresholds[vm.currentPanel - 1].phenotype.phenotypeKey = vm.currentMemeberList[0].key;
            vm.currentPanel = '';
        }

        // private function to add threshold related phenotypes
        function addRelationalThreshold() {
            // if it is primary phenotype do this
            //else it is a related item
            vm.thresholdObject.valueThresholds[vm.currentPanel - 1].phenotype.type = 'SYSTEM';
            vm.thresholdObject.valueThresholds[vm.currentPanel - 1].phenotype.relatedPhenotypeKey = vm.currentMemeberList[0].key;
            vm.currentPanel = '';
        }

        // Sequence
        vm.createSequence = function() {
            vm.seqObject.type = 'SEQUENCE';
            vm.seqObject.userId = currentUser.info.id;
            if (vm.seqObject.relatedPhenotypes.length > 0) {
                delete vm.seqObject.relatedPhenotypes[0].$$hashKey;
                delete vm.seqObject.relatedPhenotypes[0].phenotypeField.properties;
            }

            console.log(JSON.stringify(vm.seqObject));
            //var testObj = { 'primaryPhenotype': { 'hasDuration': true, 'minDuration': 1, 'minDurationUnits': '1', 'maxDuration': 1, 'maxDurationUnits': '1', 'hasPropertyConstraint': true, 'property': 'patientId', 'propertyValue': null, 'phenotypeKey': 'Patient' }, 'relatedPhenotypes': [{ 'phenotypeField': { 'phenotypeKey': 'Patient', 'hasDuration': true, 'minDuration': 2, 'minDurationUnits': '1', 'maxDuration': 2, 'maxDurationUnits': '1', 'hasPropertyConstraint': true, 'property': 'patientId' }, 'relationOperator': '2', 'sequentialPhenotype': 'Patient', 'sequentialPhenotypeSource': '1', 'relationMinCount': 2, 'relationMinUnits': '1', 'relationMaxCount': 4, 'relationMaxUnits': '1' }], 'displayName': 'ONETIME0987', 'description': 'test', 'type': 'SEQUENCE', 'userId': 1 };
            createPhenotype(vm.seqObject).then(data => {
                $state.transitionTo('phenotypes');
            }, displayError);

        };

        vm.addMainPhenotype = function() {
            vm.currentPanel = 'primaryPhenotype';
            vm.viewObject.showTree = true;
            vm.showPhenotypes = true;
        };

        vm.addPhenotypeToRelated = function(indexPanel) {
            vm.currentPanel = indexPanel;
            vm.viewObject.showTree = true;
        };

        // add related phenotypes
        vm.addRelatedPhenotypes = function() {
            let openObject = {};
            openObject.phenotypeField = {};
            openObject.phenotypeField.properties = [];
            vm.seqObject.relatedPhenotypes.push(openObject);

        };

        // private function to add sequence related phenotypes
        function addNewMembersRelated() {
            // if it is primary phenotype do this
            if (vm.currentPanel === 'primaryPhenotype') {
                console.log(vm.currentPanel);
                vm.phenotypeProperties = [];
                vm.phenotypeProperties = vm.currentMemeberList[0].properties;
                vm.seqObject.primaryPhenotype.phenotypeKey = vm.currentMemeberList[0].key;
                vm.releatedTemporalArray.push(vm.currentMemeberList[0].key);
            } else {
                //else it is a related item
                vm.seqObject.relatedPhenotypes[vm.currentPanel - 1].phenotypeField.properties = vm.currentMemeberList[0].properties;
                vm.seqObject.relatedPhenotypes[vm.currentPanel - 1].phenotypeField.phenotypeKey = vm.currentMemeberList[0].key;
                vm.releatedTemporalArray.push(vm.currentMemeberList[0].key);
            }
            vm.currentPanel = '';

        }

        // Important to add items to the list from tree
        vm.addMembers = function() {
            if (vm.currentMemeberList.length > 0) {

                if (vm.type === 'Sequence') {
                    addNewMembersRelated();
                } else if (vm.type === 'Frequency') {
                    vm.myList = [];
                    for (var i = 0; i < vm.currentMemeberList.length; i++) {
                        vm.myList.push(vm.currentMemeberList[i]);
                    }
                    // now add the properties to the dropdown
                    vm.phenotypeProperties = vm.myList[0].properties;

                } else if (vm.type === 'Threshold') {
                    if (vm.isRelationalPhenotype === false) {
                        addMembersThreshold();
                    } else {
                        addRelationalThreshold();
                    }
                } else {

                    for (var i = 0; i < vm.currentMemeberList.length; i++) {
                        vm.myList.push(vm.currentMemeberList[i]);
                    }
                }

                vm.currentMemeberList = [];
                vm.viewObject.showTree = false;

            }
        };

        // End of logic for creting Phenotypes

        // Start Logic for Table.

        vm.breadCrumbs = [{ key: 'root', displayName: 'root' }];

        let currentNodes = [];

        getMemberList();
        TreeService.getTreeRoot().then(function(data) {
            // NG Table instantiates
            vm.tableParams = new NgTableParams({}, { dataset: data });
            callUserRoot();
        }, displayError);

        function init() {
            // clearNodes();
        }

        function callUserRoot() {
            TreeService.getUserListRoot().then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                vm.tableParamsUser = new NgTableParams({}, { dataset: data.data });


            }, displayError);
        }

        vm.selectNode = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            if (hasChildren !== false) {
                TreeService.getTreeNode(currentNode.key).then(function(data) {

                    if (hasChildren) {
                        vm.breadCrumbs.push({ key: currentNode.key, displayName: currentNode.displayName, parent: currentNode.parent });
                    }
                    vm.tableParams = new NgTableParams({}, { dataset: data.children });

                }, displayError);
            } else {
                //do nothing it is a document
            }
        };

        vm.addNode = function(node) {
            console.log(vm.currentMemeberList + '  1');
            console.log(currentNodes + '@@');
            if (node) {

                setNodes(node);
            }
            getMemberList();
        };

        vm.addUserNode = function(node) {
            /* After changes add user node will need to be refactored will take care of later in week.
            if (node) {
                node.displayName = node.text;
                dragAndDropService.setNodes(node);
            }
            getMemberList() */
        };
        vm.removeNode = function(node) {
            for (var i = 0; i < vm.currentMemeberList.length; i++) {
                // we will need to look for both key and name.  name is key in update, but key is present on create.
                if (node.key === undefined) {
                    if (vm.currentMemeberList[i].name === node.name) {
                        vm.currentMemeberList.splice(i, 1);
                        break;
                    }
                } else {
                    if (vm.currentMemeberList[i].key === node.key) {
                        vm.currentMemeberList.splice(i, 1);
                        break;
                    }
                }

            }
        };
        vm.setBreadCrumbs = function(node) {
            var currentNode = node;
            var hasChildren = node.parent;
            var pos = 0;
            var returnedData = [];

            TreeService.getTreeNode(currentNode.key).then(function(data) {
                //  vm.treeData = data;

                //vm.props = data;
                //copyData = data;
                // NG Table
                if (data.hasOwnProperty('parent') && data['parent']) {
                    returnedData = data.children;
                } else {
                    returnedData = data;
                }
                if (hasChildren) {
                    vm.breadCrumbs.push({ key: currentNode.key, displayName: currentNode.displayName });

                }

                vm.tableParams = new NgTableParams({}, { dataset: returnedData });

                pos = vm.breadCrumbs.map(function(e) { return e.key; }).indexOf(currentNode.key);
                vm.breadCrumbs.length = pos + 1;

            }, displayError);

        };

        function getMemberList() {
            vm.currentMemeberList = getNodes();
        }

        function setNodes(obj, arg2) {
            currentNodes = vm.currentMemeberList;
            if (arg2 == undefined || arg2 === null) {
                if (obj !== undefined) {
                    let currentList = [];
                    currentList = currentNodes;
                    let isDuplicate = false;
                    //lets do it the long way first then we will refactor.  Lets see if there are duplicates JS
                    if (currentList.length < 1) {
                        currentNodes.push(obj);
                    } else {
                        for (var i = 0; i < currentList.length; i++) {
                            if (currentList[i].hasOwnProperty('name')) {
                                if (currentList[i].name === obj.key) {
                                    isDuplicate = true;
                                    break;
                                }

                            } else if (currentList[i].hasOwnProperty('key')) {
                                if (currentList[i].key === obj.key) {
                                    isDuplicate = true;
                                    break;
                                }

                            }


                        }
                        if (isDuplicate !== true) {
                            currentNodes.push(obj);
                        }

                    }
                }
            } else {
                currentNodes = obj;
            }
        }


        function getNodes() {
            return currentNodes;
        }

        function clearNodes() {
            currentNodes = [];
        }

        //end of the table  **************

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
}());