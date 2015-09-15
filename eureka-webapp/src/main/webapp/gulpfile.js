var gulp = require('gulp');
var sourcemaps = require("gulp-sourcemaps");
var babel = require("gulp-babel");
var concat = require("gulp-concat");
var jshint = require('gulp-jshint');
var Server = require('karma').Server;
var url = require('url');
var proxy = require('proxy-middleware');
var browserSync = require('browser-sync').create();
var wiredep = require('wiredep').stream;
var opn = require('opn');
var gulpDocs = require('gulp-ngdocs');

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

gulp.task('bower', function () {
  gulp.src('./index.html')
    .pipe(wiredep())
    .pipe(gulp.dest('./'));
});

gulp.task('ngdocs', ['process-js'], function () {
  var options = {
    title: "Eureka Angular Documentation",
    html5Mode: false
  }
  return gulp.src('build/app.js')
    .pipe(gulpDocs.process(options))
    .pipe(gulp.dest('./ng-docs'));
});

gulp.task('test', ['process-js', 'ngdocs', 'bower'], function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('process-js', ['lint'], function () {
  return gulp.src(['eureka/**/module.js', 'eureka/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(sourcemaps.init())
    .pipe(babel())
    .pipe(concat('app.js'))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('build'));
});

gulp.task('lint', function() {
    gulp.src(['eureka/**/*.js', '!eureka/js/**/*.js'])
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('watch-js', ['test'], function() {
    browserSync.reload();
});

gulp.task('browser-sync', ['test'], function() {

    // Serve files from the root of this project
    browserSync.init({
        open: false,
        server: {
            baseDir: "./"
        }
    });

    gulp.watch("eureka/**/*.js", ['watch-js']);
    gulp.watch("./**/*.html").on('change', browserSync.reload);

});

// Watch scss AND html files, doing different things with each.
gulp.task('serve', ['bower', 'browser-sync'], function () {
    opn('https://localhost:8443/eureka-angular/');
});

gulp.task('default', function() {

});