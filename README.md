# Eureka! Clinical Analytics

Version 3.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka)

Version 3 will break Eureka up into microservices.

## Version history
Version 2.5.2
As compared with version 1 of Eureka, version 2 primarily differs in much more efficient backend code for processing data from relational databases. The performance of spreadsheet data processing is also much better.

Version 1.9
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
See http://eurekaclinical.org

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
