(function() {
    'use strict';

    angular
        .module('eureka')
        .directive('tableDirectiveTwo', TableDirectiveTwo);

    function TableDirectiveTwo() {
        return {
            restrict: 'E',
            scope: true,
            templateUrl: 'eureka/directives/table-directive-two/table-directive-two.html',
            scope: {
                options : '=',
            },
            link: function(scope, element, attrs) {
              
            scope.gridOptions = {
            enableSorting: true,
            paginationPageSizes: [10, 20, 30],
            paginationPageSize: 10,
            columnDefs: [
                { name: ' ',  field: 'edit',
                cellTemplate: '<a href="${editUrl}" title="Edit">'+
                '<span class="glyphicon glyphicon-pencil edit-icon" title="Edit"></span></a> '+
                '<span class="glyphicon glyphicon-remove delete-icon" title="Delete"></span>'
                },
                { name:'Name', field: 'name' },
                { name:'Descripton', field: 'description' },
                { name:'Type', field: 'type'},
                { name:'Created', field: 'created_at', enableCellEdit:false, cellFilter: 'date:"longDate"'}
            ],
            data:  [
            {
                'name': 'Vital1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Vital2',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Vital3',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test2',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Patient Test',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Updated Patient',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Procedure Codes',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Procedure 12',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'ICD9',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'ICD10',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test ICD10',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test ICD9',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data Test',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            },
            {
                'name': 'Test1',
                'description': 'Description Data',
                'type': 'COHORT',
                'created_at': 1486327831997
            }
            ]
        };
            }
        };
    }
}());