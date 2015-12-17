'use strict';

angular.module('taxitraderApp')
    .controller('Car_brandDetailController', function ($scope, $rootScope, $stateParams, entity, Car_brand) {
        $scope.car_brand = entity;
        $scope.load = function (id) {
            Car_brand.get({id: id}, function(result) {
                $scope.car_brand = result;
            });
        };
        var unsubscribe = $rootScope.$on('taxitraderApp:car_brandUpdate', function(event, result) {
            $scope.car_brand = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
