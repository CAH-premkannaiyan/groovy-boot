'use strict';

angular.module('healthsenseApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
