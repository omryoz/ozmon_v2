'use strict';

angular.module('taxitraderApp')
    .factory('Car_modelSearch', function ($resource) {
        return $resource('api/_search/car_models/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
