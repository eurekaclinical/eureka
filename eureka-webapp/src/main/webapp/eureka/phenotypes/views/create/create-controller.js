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

        let vm = this;
        let currentUser;
        let createPhenotype = PhenotypeService.createPhenotype;
        // doing this on every page to make sure the user is authenticated if he/she refresh.  Will need to be changed but for now no time to refactor JS
        users.getUser().then(function(user) {
            currentUser = user;
        });

        vm.type = _.startCase($stateParams.type);
        // for tree component
        vm.memberList = [];

        vm.currentObject = {};
        vm.currentObject.displayName = '';
        vm.currentObject.description = '';
        vm.currentObject.children = [];

        //frequency
        vm.frequencyObject = {};
        vm.frequencyObject.phenotype = {};
        vm.frequencyObject.phenotype.hasDuration = false;
        vm.frequencyObject.phenotype.minDuration = '';
        vm.frequencyObject.phenotype.minDurationUnits = "1";
        vm.frequencyObject.phenotype.maxDuration = '';
        vm.frequencyObject.phenotype.maxDurationUnits = "1";
        vm.frequencyObject.phenotype.hasPropertyConstraint = false;
        vm.frequencyObject.phenotype.property = "patientId";
        vm.frequencyObject.phenotype.propertyValue = '';
        vm.frequencyObject.isWithin = false;
        vm.frequencyObject.withinAtLeast = "";
        vm.frequencyObject.frequencyType = "1";
        vm.frequencyObject.withinAtLeastUnits = "1";
        vm.frequencyObject.withinAtMost = "";
        vm.frequencyObject.withinAtMostUnits = "1";
        vm.timeUnits = [{ name: 'minutes', value: "3" }, { name: 'hours', value: "2" }, { name: 'days', value: "1" }];
        vm.thresholdUnits = ['at least', 'first'];


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
            case 'value-threshold':
                vm.description = 'Computes intervals corresponding to when the specified thresholds below are present.';
                break;
        }

        vm.openMenu = function($mdOpenMenu, evt) {
            $mdOpenMenu(evt);
        };

        vm.createCategorization = function() {
            var children = [];
            for (var i = 0; i < vm.memberList.length; i++) {
                var childrenObject = {};
                childrenObject.phenotypeKey = vm.memberList[i].key;
                childrenObject.type = vm.memberList[i].type;

                vm.currentObject.children.push(childrenObject)
            }
            vm.currentObject.type = "CATEGORIZATION";
            vm.currentObject.categoricalType = null;
            vm.currentObject.userId = currentUser.info.id;
            // PhenotypeService.createPhenotype(vm.currentObject);
            createPhenotype(vm.currentObject).then(data => {
                // if successful we prob need to redirect back to the main table
                console.log('we made it back', data);
                $state.transitionTo('phenotypes');
            }, displayError);

        }

        vm.createFrequency = function() {

            //{"type":"FREQUENCY","frequencyType":"1","displayName":"Name","description":"Description","atLeast":"1","isConsecutive":false,
            //"phenotype":{"phenotypeKey":"Patient","hasDuration":true,"minDuration":"2","minDurationUnits":"1","maxDuration":"2","maxDurationUnits":"1",
            //"hasPropertyConstraint":true,"property":"patientId","propertyValue":"test "},"isWithin":true,"withinAtLeast":"2","withinAtLeastUnits":"1","withinAtMost":"2","withinAtMostUnits":"1"} 
            vm.frequencyObject.type = "FREQUENCY";
            vm.frequencyObject.isConsecutive = false;
            vm.frequencyObject.phenotype.phenotypeKey = vm.memberList[0].key;

            if (vm.frequencyThreshold === 'at least') {
                vm.frequencyObject['atLeast'] = vm.frequencyThresholdValue;
            } else {
                vm.frequencyObject['first'] = vm.frequencyThresholdValue;
            }
            vm.frequencyObject.userId = currentUser.info.id;
            console.log(JSON.stringify(vm.frequencyObject));
            PhenotypeService.createPhenotype(vm.frequencyObject);
        }

        function displayError(msg) {
            vm.errorMsg = msg;
        }
    }
}());