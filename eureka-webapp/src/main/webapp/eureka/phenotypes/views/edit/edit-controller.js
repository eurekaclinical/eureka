(function() {
    'use strict';

    /**
     * @ngdoc controller
     * @name eureka.phenotypes.controller:EditCtrl
     * @description
     * This is the edit controller for the phenotypes section of the application.
     * @requires $scope
     * @requires $location
     * @requires eureka.phenotypes.PhenotypeService
     */

    angular
        .module('eureka.phenotypes')
        .controller('phenotypes.EditCtrl', EditCtrl);

    EditCtrl.$inject = ['$stateParams', 'PhenotypeService', 'dragAndDropService', 'TreeService', 'users', '$state', 'NgTableParams', ];


    function EditCtrl($stateParams, PhenotypeService, dragAndDropService, TreeService, users, $state, NgTableParams) {

        let vm = this;
        let currentUser;
        let currentList = [];
        let currentType = '';
        let updatePhenotype = PhenotypeService.updatePhenotype;

        // Tree component
        vm.memberList = [];
        vm.myList = [];
        // Show hide tree on right side
        vm.viewObject = {};
        vm.viewObject.showTree = false

        // doing this on every page to make sure the user is authenticated if he/she refresh.  Will need to be changed but for now no time to refactor JS
        users.getUser().then(function(user) {
            currentUser = user;
        });

        vm.currentObject = {};
        vm.memberList = [];
        vm.type = _.startCase($stateParams.type);
        vm.timeUnits = ['minutes', 'hours', 'days'];

        //frequency
        vm.frequencyObject = {};
        vm.frequencyObject.phenotype = {};
        vm.timeUnits = [{ name: 'minutes', value: 3 }, { name: 'hours', value: 2 }, { name: 'days', value: 1 }];
        vm.thresholdUnits = ['at least', 'first'];

        //Sequence
        vm.seqObject = {};

        vm.thresholdObject = {};

        if ($stateParams.id) {
            PhenotypeService.getPhenotype($stateParams.id).then(function(data) {
                if (data.type === "FREQUENCY") {
                    vm.frequencyObject = data;
                    currentType = vm.frequencyObject.type;
                    setType();
                    // have to set the member list for the drop area

                    var myChildren = {};
                    myChildren.key = data.phenotype.phenotypeKey;
                    myChildren.displayName = data.phenotype.phenotypeKey;
                    myChildren.type = data.phenotype.type;
                    currentList.push(myChildren);

                    //dragAndDropService.setNodes(currentList);
                    vm.memberList = currentList;
                    console.log(vm.currentObject);

                } else if (data.type === "CATEGORIZATION") {

                    vm.currentObject = data;
                    currentType = vm.currentObject.type;
                    setType();
                    // have to set the member list for the drop area
                    for (var i = 0; i < data.children.length; i++) {
                        var myChildren = {};
                        myChildren.key = data.children[i].phenotypeKey;
                        myChildren.displayName = data.children[i].phenotypeKey;
                        myChildren.type = data.children[i].type;
                        currentList.push(myChildren);
                    }
                    //dragAndDropService.setNodes(currentList);
                    vm.memberList = currentList;
                    console.log(vm.currentObject);

                } else if (data.type === "SEQUENCE") {

                    vm.seqObject = data;
                    currentType = vm.seqObject.type;
                    setType();


                } else if (data.type === "VALUE_THRESHOLD") {

                    vm.thresholdObject = data;
                    currentType = vm.thresholdObject.type;
                    setType();


                }

            }, displayError);

        }

        vm.addFrequencyPhenotype = function() {
            vm.viewObject.showTree = true;
        }
        vm.addCategorizationPhenotype = function() {
            vm.viewObject.showTree = true;
        }
        vm.addMainPhenotype = function() {
            vm.currentPanel = 'primaryPhenotype';
            vm.viewObject.showTree = true;
            vm.showPhenotypes = true;
        }
        vm.addPhenotypeToThreshold = function(indexPanel, isRelational) {
            vm.currentPanel = indexPanel
            vm.viewObject.showTree = true;
            if (isRelational === undefined) {
                // do nothing
            } else {
                // it is a relational phenotype.
                vm.isRelationalPhenotype = true;
            }

        }

        // vm.memberList = [];

        function setType() {
            switch (currentType) {
                case 'CATEGORIZATION':
                    vm.description = 'This category data element may be used wherever its member data elements are accepted.';
                    vm.name = 'Categorization';
                    vm.type = 'CATEGORIZATION';
                    break;
                case 'SEQUENCE':
                    vm.description = 'Computes intervals with the same start and stop time as the Main data element below when ' +
                        'the temporal relationships below are satisfied.';
                    vm.name = 'Sequence';
                    vm.type = 'SEQUENCE';
                    break;
                case 'FREQUENCY':
                    vm.description = 'Computes an interval over the temporal extent of the intervals contributing to the ' +
                        'specified frequency count below.';
                    vm.name = 'Frequency';
                    vm.type = 'FREQUENCY';
                    break;
                case 'VALUE_THRESHOLD':
                    vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
                    vm.name = 'Value Threshold';
                    vm.type = 'Threshold';
                    break;
            }
        }
        vm.openMenu = function($mdOpenMenu, evt) {
            $mdOpenMenu(evt);
        };

        vm.updateCategorization = function() {

            var updateObject = {};
            updateObject.type = vm.currentObject.type;
            updateObject.displayName = vm.currentObject.displayName;
            updateObject.description = vm.currentObject.description;
            updateObject.categoricalType = vm.currentObject.categoricalType;
            updateObject.id = vm.currentObject.id;
            updateObject.userId = currentUser.info.id;
            updateObject.children = [];

            // {"type":"CATEGORIZATION","displayName":"ONEMORETIME901","description":"description","categoricalType":"CONSTANT","children":[{"phenotypeKey":"Patient","type":"SYSTEM"}],"id":13}

            var children = [];
            for (var i = 0; i < vm.memberList.length; i++) {
                var childrenObject = {};
                // if (vm.memberList[i].key === undefined) {
                childrenObject.phenotypeKey = vm.memberList[i].key;
                childrenObject.type = vm.memberList[i].type;
                children.push(childrenObject)
                    // }

            }
            updateObject.children = children;
            // vm.currentObject.type = "CATEGORIZATION";
            //vm.currentObject.categoricalType = null;
            vm.currentObject.userId = 1;
            console.log('######    ' + vm.currentObject);

            PhenotypeService.updatePhenotype(updateObject);
            updatePhenotype(updateObject).then(data => {
                console.log('we made it back', data);
                $state.transitionTo('phenotypes');
            }, displayError);

        }

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
        }

        vm.addNode = function(node) {
            console.log(vm.currentMemeberList + '  1');
            console.log(currentNodes + '@@');
            if (node) {

                setNodes(node)
            }
            getMemberList()
        }

        vm.addUserNode = function(node) {
            /* After changes add user node will need to be refactored will take care of later in week.
            if (node) {
                node.displayName = node.text;
                dragAndDropService.setNodes(node);
            }
            getMemberList() */
        }
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
        }
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

        }

        function getMemberList() {
            vm.currentMemeberList = getNodes()
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