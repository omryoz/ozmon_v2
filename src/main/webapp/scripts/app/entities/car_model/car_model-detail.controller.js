'use strict';

angular.module('taxitraderApp')
    .controller('Car_modelDetailController', function ($scope, $rootScope, $stateParams, entity, Car_model, Car_brand) {
        $scope.car_model = entity;
        $scope.load = function (id) {
            Car_model.get({id: id}, function(result) {
                $scope.car_model = result;
            });
        };
        var unsubscribe = $rootScope.$on('taxitraderApp:car_modelUpdate', function(event, result) {
            $scope.car_model = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
