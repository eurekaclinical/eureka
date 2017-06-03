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
      'https://code.jquery.com/jquery-2.2.4.min.js',
      'https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js',
      'https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.1/angular-mocks.js',
      'https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.15/angular-ui-router.min.js',
      'https://cdnjs.cloudflare.com/ajax/libs/angular-messages/1.4.14/angular-messages.min.js',
      'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js',
      'https://cdnjs.cloudflare.com/ajax/libs/angular-validator/1.2.9/angular-validator.min.js',
      'https://cdnjs.cloudflare.com/ajax/libs/angular-ui-tree/2.9.0/angular-ui-tree.js',
      'https://cdn.jsdelivr.net/lodash/3.10.1/lodash.min.js',
      'assets/js/eureka.tree-cohort.js',
      'assets/js/eureka.cohort.js',
      'build/app-*.js',
      'eureka/**/*-spec.js'
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
    reporters: [
      'mocha'
    ],


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
