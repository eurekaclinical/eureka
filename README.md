# Eureka! Clinical Analytics

Version 3.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka)

Version 3 will break Eureka up into microservices.

## Version history
### Version 2.5.2
As compared with version 1 of Eureka, version 2 primarily differs in much more efficient backend code for processing data from relational databases. The performance of spreadsheet data processing is also much better.

### Version 1.9
Version 1.9 includes an update to the UI, now using Bootstrap 3, 
to make the application more usable on mobile devices.  It also 
includes support for i2b2 1.7.  A new feature to allow usage of 
BioPortal for ontologies was added.  The codebase was also updated 
to utilize Java 7.  A data element search functionality was 
added to the phenotype editor and job submission screens.  Eureka! 
can now utilize OAuth as an authentication mechanism, allowing 
the use of services like Facebook, Twitter, Google Plus, and other 
to log into the system.  Finally, many improvements were made 
to ease the process of installing and configuring the software.

## What does it do
Analytics provides tools for electronic health record (EHR) phenotyping, that is, finding patients of interest that match specified patterns in clinical and administrative EHR data. Eureka stores these patterns in computable form, and it computes them rapidly in clinical datasets and databases, including i2b2 clinical data warehouses. It supports building a repository of phenotypes representing best practices in how to find patient populations of interest. See http://www.eurekaclinical.org/docs/analytics/ for more information.

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)
* Minimum 8 GB of RAM

We build Eureka regularly on Mac and Linux. It may also build on Windows.

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org)
* Also running
  * The [eurekaclinical-user-service](https://github.com/eurekaclinical/eurekaclinical-user-service) war
  * The [eurekaclinical-user-webapp](https://github.com/eurekaclinical/eurekaclinical-user-webapp) war
  * The [cas-server](https://github.com/eurekaclinical/cas) war

## Building it
The project uses the maven build tool. 

The system tests that are run automatically during build require more RAM than Java's default. Add the `MAVEN_OPTS` environment variable to your user account's profile, and set the max Java heap size to 4GB. On Linux and Mac, this is specified in your `~/.bash_profile` as follows:
```
export MAVEN_OPTS='-Xmx4g`
```

Typically, you build it by invoking `mvn clean install` at the command line. For simple file changes, not additions or deletions, you can usually use `mvn install`. See https://github.com/eurekaclinical/dev-wiki/wiki/Building-Eureka!-Clinical-projects for more details.

## Performing system tests
You can run this project in an embedded tomcat by executing `mvn tomcat7:run` after you have built it. It will be accessible in your web browser at https://localhost:8443/eureka-webapp/. Your username will be `superuser`.

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eureka-webapp.svg)](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp)

## Configuration
Eureka is configured using a properties file located at `/etc/eureka/application.properties`. It supports the following properties:

## Getting help
Feel free to contact us at help@eurekaclinical.org.
