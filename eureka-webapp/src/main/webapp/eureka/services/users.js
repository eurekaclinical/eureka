(function() {

    'use strict';

    /**
     * @ngdoc service
     * @name eureka.users
     * @description
     * This is the users service.
     * @requires $http
     * @requires $q
     * @requires eureka.appProperties
     */

    angular
        .module('eureka')
        .factory('users', users);

    users.$inject = ['$http', '$q', 'appProperties'];

    function users($http, $q, appProperties) {

        let { dataEndpoint } = appProperties;

        return {
            getUser: getCurrentUser,
            getRole: getRole,
            getUsers: getUsers
        };

        function getRole(roleId) {
            return $http.get(dataEndpoint+'/roles/' + roleId).then(function(res) {
                return res.data;
            }, function(err) {
                console.error('role retrieval failed:', err);
                return err;
            });
        }

        function getCurrentUser() {
            return $http.get(dataEndpoint+'/users/me',
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

        function getUsers() {
            return $http.get(dataEndpoint+'/users')
            .then(handleSuccess, handleError)
            .then(function(users){
                
                return $q.all( _.map(users, function(user){
                    return $q.all(_.map(user.roles, getRole))
                        .then(function(roleObjects){
                            user.roles = roleObjects;
                            return user;

                        });
                }));
            });
        }

        function handleSuccess(response) {
            return response.data;
        }

        function handleError(response) {
            if (!angular.isObject(response.data) && !response.data) {
                return ($q.reject('An unknown error occurred.'));
            }
            return ($q.reject(response.data));
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