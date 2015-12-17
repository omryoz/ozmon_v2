 'use strict';

angular.module('taxitraderApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-taxitraderApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-taxitraderApp-params')});
                }
                return response;
            }
        };
    });
