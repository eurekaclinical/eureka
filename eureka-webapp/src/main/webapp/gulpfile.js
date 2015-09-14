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

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

gulp.task('bower', function () {
  gulp.src('./index.html')
    .pipe(wiredep())
    .pipe(gulp.dest('./'));
});

/**
* Run test once and exit
*/
gulp.task('test', function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('process-js', function () {
  return gulp.src(['eureka/**/*.js', '!eureka/js/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(sourcemaps.init())
    .pipe(babel())
    .pipe(concat("all.js"))
    .pipe(sourcemaps.write("."))
    .pipe(gulp.dest("dist"));
});

gulp.task('lint', function() {
    gulp.src(['eureka/**/*.js', '!eureka/js/**/*.js'])
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('autotest', function() {
  return gulp.watch(['eureka/**/*.js'], ['test']);
});

gulp.task('watch-js', ['process-js'], function() {
    browserSync.reload();
});

gulp.task('browser-sync', function() {

    // Serve files from the root of this project
    browserSync.init({
        open: false,
        server: {
            baseDir: "./"
        }
    });

    gulp.watch("eureka/**/*.js", ['watch-js']);
    gulp.watch("./**/*.html").on("change", browserSync.reload);

});

// Watch scss AND html files, doing different things with each.
gulp.task('serve', ['bower', 'browser-sync'], function () {
    opn('https://localhost:8443/eureka-angular/');
});

gulp.task('default', function() {

});