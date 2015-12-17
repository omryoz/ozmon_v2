'use strict';

angular.module('taxitraderApp').controller('Car_modelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Car_model', 'Car_brand',
        function($scope, $stateParams, $uibModalInstance, entity, Car_model, Car_brand) {

        $scope.car_model = entity;
        $scope.car_brands = Car_brand.query();
        $scope.load = function(id) {
            Car_model.get({id : id}, function(result) {
                $scope.car_model = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('taxitraderApp:car_modelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.car_model.id != null) {
                Car_model.update($scope.car_model, onSaveSuccess, onSaveError);
            } else {
                Car_model.save($scope.car_model, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
