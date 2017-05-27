var gulp = require('gulp'),
    sourcemaps = require("gulp-sourcemaps"),
    rev = require('gulp-rev'),
    inject = require('gulp-inject')
    babel = require("gulp-babel"),
    concat = require("gulp-concat"),
    gulpDocs = require('gulp-ngdocs'),
    uglify = require('gulp-uglify'),
    templateCache = require('gulp-angular-templatecache'),


gulp.task('ngdocs', ['process-js'], function () {
  var options = {
    title: "Eureka Angular Documentation",
    html5Mode: false
  };
  return gulp.src('build/app-*.js')
    .pipe(gulpDocs.process(options))
    .pipe(gulp.dest('./ng-docs'));
});

gulp.task('test', ['process-js', 'ngdocs'], function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('cache-templates', function () {
  return gulp.src('eureka/**/*.html')
    .pipe(templateCache('templates.js', {
        module: 'eureka',
        root: 'eureka/'
    }))
    .pipe(gulp.dest('./build'));
});

gulp.task('process-js', ['cache-templates'], function () {
  return gulp.src(['eureka/**/module.js', './build/templates.js', 'eureka/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(sourcemaps.init())
    .pipe(babel({presets: ['es2015']}))
    .pipe(concat('app.js'))
    .pipe(rev())
    .pipe(uglify())
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('build'));
});

gulp.task('inject-hash', ['ngdocs'], function () {
  var target = gulp.src('./index.html');
  var sources = gulp.src(['./build/app-*.js'], {read: false});
 
    return target.pipe(inject(sources,{relative: true}))
        .pipe(gulp.dest('./build/injected'));
});

gulp.task('default', ['inject-hash']);