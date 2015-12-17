'use strict';

angular.module('taxitraderApp')
	.controller('Car_modelDeleteController', function($scope, $uibModalInstance, entity, Car_model) {

        $scope.car_model = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Car_model.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
