(function() {

'use strict';

angular
    .module('eureka')
    .factory('users', users);

users.$inject = ['$http', '$q', 'appProperties'];

function users($http, $q, appProperties) {

    let { apiEndpoint } = appProperties;

    return {
        getUser: getCurrentUser,
        getRole: getRole
    };

    function getRole(roleId) {
        return $http.get(apiEndpoint + '/role/' + roleId).then(function(res) {
            return res.data;
        }, function(err) {
            console.error('role retrieval failed:', err);
            return err;
        });
    }

    function getCurrentUser() {
        return $http.get(apiEndpoint + '/users/me',
        {
            transformResponse: function (data) {
                try {
                    let jsonObject = JSON.parse(data); // verify that json is valid
                    return jsonObject;
                }
                catch (e) {
                    console.log('User is not logged in!');
                }
            }
        }).then(function(res) {
            let userInfo = res.data;
            if (!userInfo) {
                return $q.when(null);
            }
            return $q.all(_.map(userInfo.roles, getRole)).then(function(roles) {
                userInfo.roles = roles;
                console.log('user info!', userInfo);
                return new User(userInfo);
            });
        }, function(err) {
            console.error('error getting user:', err);
        });
    }
}

class User {
    constructor(info) {
        this.info = info;
    }

    hasRole(name) {
        let { roles } = this.info;
        return roles.some(role => role.name === name);
    }

    getFullName() {
        let { fullName, firstName, lastName } = this.info;
        return fullName || (firstName + ' ' + lastName);
    }
}

}());