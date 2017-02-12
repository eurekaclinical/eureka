var gulp = require('gulp'),
    opn = require('opn'),
    less = require('gulp-less');


gulp.task('less', function () {
    return gulp.src('./eureka/eureka.less')
        .pipe(less())
        .pipe(gulp.dest('./build'));
});


gulp.task('default', ['less']);