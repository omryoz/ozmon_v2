'use strict';

angular.module('taxitraderApp')
    .factory('Car_model', function ($resource, DateUtils) {
        return $resource('api/car_models/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
