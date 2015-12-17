'use strict';

angular.module('taxitraderApp')
	.controller('Car_brandDeleteController', function($scope, $uibModalInstance, entity, Car_brand) {

        $scope.car_brand = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Car_brand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
