(function() {
    'use strict';

    describe('PhenotypeService', function() {

        var PhenotypeService, $httpBackend;

        beforeEach(function() {
            module('eureka.phenotypes');
        });
        
        // Setup the mock service in an anonymous module.
        beforeEach(module(function ($provide) {
            $provide.value('appProperties',{
                    dataEndpoint: 'proxy-resource',
                    filterEndpoint: 'protected/jstree3_searchsystemlist'
            });
        }));
        
        beforeEach(inject(function(_PhenotypeService_, _$httpBackend_) {
            PhenotypeService = _PhenotypeService_;
            $httpBackend = _$httpBackend_;
        }));



        it('should be defined', function() {
            expect(PhenotypeService).toBeDefined();
        });

        describe('getSummarizedUserElements', function() {

            var userElements;

            beforeEach(function () {
                userElements = {
                    blah: 'test'
                };
                $httpBackend.expectGET('proxy-resource/phenotypes?summarize=true')
                    .respond(userElements);
            });

            it('should get the user elements', function() {
                PhenotypeService.getSummarizedUserElements().then(function(res) {
                    expect(res).toEqual(userElements);
                });
                $httpBackend.flush();
            });

        });

    });
}());