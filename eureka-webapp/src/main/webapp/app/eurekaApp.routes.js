eurekaApp.config(function($routeProvider) {
    $routeProvider.
        when('/index', {
            templateUrl: "app/views/index_partial.jsp",
            controller: 'MainController'
        }).
        when('/register', {
            templateUrl: "app/views/register/register.html",
            controller: 'RegisterController'
        }).
        when('/register_info', {
            templateUrl: "app/views/register/registration_info.html",
            controller: 'RegisterController'
        }).
        when('/cohort_home', {
            templateUrl: "app/views/cohorts/cohort_home.html",
            controller: 'CohortController',
            controllerAs: 'cohortController'
        }).
        when('/edit_cohort', {
            templateUrl: "app/views/cohorts/edit_cohort.html",
            controller: 'CohortEditController',
            controllerAs: 'cohortEditController'
        }).
        when('/edit_cohort/:key', {
            templateUrl: "app/views/cohorts/edit_cohort_key.html",
            controller: 'CohortEditController',
            controllerAs: 'cohortEditController'
        }).
        when('/edit_cohort/:name', {
            templateUrl: "app/views/cohorts/edit_cohort.html",
            controller: 'CohortController',
            controllerAs: 'cohortController'
        }).
        otherwise({
            redirectTo: '/index'
        });
});


function processReq(_p, res) {
    var resp = [];
    fs.readdir(_p, function(err, list) {
        for (var i = list.length - 1; i >= 0; i--) {
            resp.push(processNode(_p, list[i]));
        }
        res.json(resp);
    });
}

function processNode(_p, f) {
    var s = fs.statSync(path.join(_p, f));
    return {
        "id": path.join(_p, f),
        "text": f,
        "icon" : s.isDirectory() ? 'jstree-custom-folder' : 'jstree-custom-file',
        "state": {
            "opened": false,
            "disabled": false,
            "selected": false
        },
        "li_attr": {
            "base": path.join(_p, f),
            "isLeaf": !s.isDirectory()
        },
        "children": s.isDirectory()
    };
}
