(function() {
    'use strict';

    describe('PhenotypeService', function() {

        var PhenotypeService;

        beforeEach(function() {
            module('eureka.phenotypes');
        });

        beforeEach(inject(function(_PhenotypeService_) {
            PhenotypeService = _PhenotypeService_;
        }));

        it('should be defined', function() {
            expect(PhenotypeService).toBeDefined();
        });
    });
}());