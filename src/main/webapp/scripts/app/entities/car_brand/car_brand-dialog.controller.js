'use strict';

angular.module('taxitraderApp').controller('Car_brandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Car_brand',
        function($scope, $stateParams, $uibModalInstance, entity, Car_brand) {

        $scope.car_brand = entity;
        $scope.load = function(id) {
            Car_brand.get({id : id}, function(result) {
                $scope.car_brand = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('taxitraderApp:car_brandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.car_brand.id != null) {
                Car_brand.update($scope.car_brand, onSaveSuccess, onSaveError);
            } else {
                Car_brand.save($scope.car_brand, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
