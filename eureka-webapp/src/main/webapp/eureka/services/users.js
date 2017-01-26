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
    
    class User {
        constructor(info) {
            this.info = info;
        }

        hasRole(name) {
            let { roles } = this.info;
            return roles.some(role => role.name === name);
        }

        getDisplayName() {
            let { fullName, firstName, lastName, username } = this.info;
            if (fullName) {
                return fullName;
            } else if (firstName || lastName) {
                return [firstName, lastName]
                .filter(function (val) {return val && val.length > 0 ? val : undefined;})
                .join(' ');
            }else{
                return username;
            }
        }
    }

    function users($http, $q, appProperties) {

        let { dataEndpoint } = appProperties;

        return {
            getUser: getCurrentUser,
            getRole: getRole
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
                    return new User(userInfo);
                });
            }, function(err) {
                console.error('error getting user:', err);
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

}());