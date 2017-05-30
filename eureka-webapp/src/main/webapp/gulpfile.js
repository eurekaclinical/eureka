var gulp = require('gulp'),
    sourcemaps = require("gulp-sourcemaps"),
    rev = require('gulp-rev'),
    inject = require('gulp-inject')
    babel = require("gulp-babel"),
    concat = require("gulp-concat"),
    gulpDocs = require('gulp-ngdocs'),
    uglify = require('gulp-uglify'),
    templateCache = require('gulp-angular-templatecache'),
    addStream = require('add-stream'),
    Server = require('karma').Server,
    jshint = require('gulp-jshint');

gulp.task('lint', function() {
    gulp.src(['eureka/**/*.js', '!eureka/js/**/*.js'])
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(jshint.reporter('fail', {'ignoreWarning': true}));
});

gulp.task('compile', ['lint'], function () {
  var target = gulp.src('./index.html');
  var sources = gulp.src(['eureka/**/module.js', 'eureka/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(sourcemaps.init())
    .pipe(addStream.obj(gulp.src('eureka/**/*.html')
		.pipe(templateCache('templates.js', {module: 'eureka', root: 'eureka/'}))))
    .pipe(babel({presets: ['es2015']}))
    .pipe(concat('app.js'))
    .pipe(rev())
    .pipe(uglify())
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('build'));
 
    return target.pipe(inject(sources,{relative: true}))
        .pipe(gulp.dest('./build'));
});

gulp.task('test', ['compile'], function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('ngdocs', function () {
  var options = {
    title: "Eureka! Clinical Analytics Web Client Documentation",
    html5Mode: false
  };
  return gulp.src(['eureka/**/module.js', 'eureka/**/*.js', '!eureka/**/*-spec.js'])
    .pipe(gulpDocs.process(options))
    .pipe(gulp.dest('./ng-docs'));
});

gulp.task('default', ['ngdocs', 'test']);