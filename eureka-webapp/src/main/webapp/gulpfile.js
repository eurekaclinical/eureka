var gulp = require('gulp'),
    sourcemaps = require("gulp-sourcemaps"),
    rev = require('gulp-rev'),
    inject = require('gulp-inject')
    babel = require("gulp-babel"),
    concat = require("gulp-concat"),
    //wiredep = require('wiredep').stream,
    opn = require('opn'),
    gulpDocs = require('gulp-ngdocs'),
    htmlmin = require('gulp-htmlmin'),
    useref = require('gulp-useref'),
    gulpif = require('gulp-if'),
    uglify = require('gulp-uglify'),
    cleanCSS = require('gulp-clean-css'),
    templateCache = require('gulp-angular-templatecache'),
    map = require('map-stream'),
    less = require('gulp-less'),
    es = require('event-stream')



process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

gulp.task('bower', function () {
  gulp.src('./index.html')
    //.pipe(wiredep())
    .pipe(gulp.dest('./'));
});

gulp.task('ngdocs', ['process-js'], function () {
  var options = {
    title: "Eureka Angular Documentation",
    html5Mode: false
  };
  return gulp.src('build/app-*.js')
    .pipe(gulpDocs.process(options))
    .pipe(gulp.dest('./ng-docs'));
});

gulp.task('test', ['process-js', 'ngdocs', 'bower'], function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('process-js', ['cache-templates'], function () {
  return gulp.src(['eureka/**/module.js', './build/templates.js', 'eureka/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(sourcemaps.init())
    .pipe(babel())
    .pipe(concat('app.js'))
    .pipe(rev())
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('build'));
});

gulp.task('inject-hash', ['ngdocs', 'bower'], function () {
  var target = gulp.src('./index.html');
  var sources = gulp.src(['./build/app-*.js'], {read: false});
 
    return target.pipe(inject(sources,{relative: true}))
        .pipe(gulp.dest('./build/injected'));
});

gulp.task('less', function () {
    return gulp.src('./eureka/eureka.less')
        .pipe(less())
        .pipe(gulp.dest('./build'));
});

//gulp.task('lint', function() {
//    gulp.src(['eureka/**/*.js', '!eureka/js/**/*.js'])
//        .pipe(jshint('.jshintrc'))
//        .pipe(jshint.reporter('jshint-stylish'))
//        .pipe(exitOnJshintError());
//}); */

gulp.task('cache-templates', function () {
  return gulp.src('eureka/**/*.html')
    .pipe(templateCache('templates.js', {
        module: 'eureka',
        root: 'eureka/'
    }))
    .pipe(gulp.dest('./build'));
});

gulp.task('compile:html', ['less', 'inject-hash'], function() {
    return gulp.src('build/injected/index.html')
        .pipe(useref({ searchPath: ['bower_components', 'eureka', 'assets', 'build', '.'] }))
        .pipe(gulpif('*.js', uglify()))
        .pipe(gulpif('*.css', cleanCSS()))
        .pipe(gulp.dest('build'));
});

gulp.task('build-html', ['compile:html'], function() {
    return gulp.src('./build/index.html')
        .pipe(htmlmin({collapseWhitespace: true}))
        .pipe(gulp.dest('./dist'));
});

gulp.task('build', ['build-html'], function() {
    return es.concat(
        gulp.src('assets/**/*', { base: './' })
            .pipe(gulp.dest('./dist')),
        gulp.src(['./build/*.min.js', './build/*.min.css'])
            .pipe(gulp.dest('./dist'))
    );
});

gulp.task('default', ['build']);