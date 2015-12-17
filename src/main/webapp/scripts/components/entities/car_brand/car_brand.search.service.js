'use strict';

angular.module('taxitraderApp')
    .factory('Car_brandSearch', function ($resource) {
        return $resource('api/_search/car_brands/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
