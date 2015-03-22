'use strict';

angular.module('healthsenseApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


