'use strict';

describe('Car_brand Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCar_brand;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCar_brand = jasmine.createSpy('MockCar_brand');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Car_brand': MockCar_brand
        };
        createController = function() {
            $injector.get('$controller')("Car_brandDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'taxitraderApp:car_brandUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
