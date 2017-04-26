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

    EditCtrl.$inject = ['$stateParams', 'PhenotypeService', 'dragAndDropService', 'TreeService', 'users', '$state'];


    function EditCtrl($stateParams, PhenotypeService, dragAndDropService, TreeService, users, $state) {

        let vm = this;
        let currentUser;
        let currentList = [];
        let currentType = '';
        let updatePhenotype = PhenotypeService.updatePhenotype;

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


                }

            }, displayError);

        }

        // vm.memberList = [];

        function setType() {
            switch (currentType) {
                case 'CATEGORIZATION':
                    vm.description = 'This category data element may be used wherever its member data elements are accepted.';
                    vm.name = 'Categorization';
                    break;
                case 'SEQUENCE':
                    vm.description = 'Computes intervals with the same start and stop time as the Main data element below when ' +
                        'the temporal relationships below are satisfied.';
                    vm.name = 'Sequence';
                    break;
                case 'FREQUENCY':
                    vm.description = 'Computes an interval over the temporal extent of the intervals contributing to the ' +
                        'specified frequency count below.';
                    vm.name = 'Frequency';
                    break;
                case 'VALUE_THRESHOLD':
                    vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
                    vm.name = 'Value Threshold';
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

        function displayError(msg) {
            vm.errorMsg = msg;
        }

    }
}());