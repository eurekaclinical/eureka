describe('EditorService', function() {

    var EditorService;

    beforeEach(function() {
        module('eureka');
    });

    beforeEach(inject(function(_EditorService_) {
        EditorService = _EditorService_;
    }));

    it('should be defined', function() {
        expect(EditorService).toBeDefined();
    });
});