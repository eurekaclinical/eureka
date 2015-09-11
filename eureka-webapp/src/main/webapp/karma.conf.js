// Karma configuration
// Generated on Wed Sep 09 2015 20:41:19 GMT-0400 (EDT)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
      'assets/js/jquery-2.1.3.min.js',
      'assets/bootstrap-3.3.4-dist/js/bootstrap.min.js',
      'app/js/angular.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'app/js/angular-route.js',
      'app/js/angular-messages.js',
      'app/js/angular-validator.min.js',
      'assets/js/eureka.tree-cohort.js',
      'assets/js/eureka.cohort.js',
      'assets/js/jstree-3.1.1.min.js',
      'app/eurekaApp.js',
      'app/eurekaApp.routes.js',
      'app/services/*.js',
      'app/controllers/*.js',
      'app/**/*-spec.js'
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['PhantomJS'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false
  })
}
