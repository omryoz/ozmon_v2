'use strict';

angular.module('taxitraderApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


