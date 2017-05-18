# Eureka! Webapp
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides an angular-based web client for interacting with the Eureka! system. It also implements a proxy servlet and router for web clients to access the web services provided by `eureka-services` and `eureka-protempa-etl`.

## Version 3.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-webapp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-webapp)

Version 3 will finish the "angularization" of the web client.

## Version history
### Version 2.5.2
As compared with version 1, we have partially angularized the application. The actual functionality is unchanged.

### Version 1.9
Version 1.9 includes an update to the UI, now using Bootstrap 3, 
to make the application more usable on mobile devices.  

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org)
* Also running
  * The [eureka-services](https://github.com/eurekaclinical/eureka/tree/master/eureka-services) war
  * The [eureka-protempa-etl](https://github.com/eurekaclinical/eureka/tree/master/eureka-protempa-etl) war
  * The [eurekaclinical-user-webapp](https://github.com/eurekaclinical/eurekaclinical-user-webapp) war
  * The [eurekaclinical-user-service](https://github.com/eurekaclinical/eurekaclinical-user-service) war
  * The [cas-server](https://github.com/eurekaclinical/cas) war

## Proxied REST APIs
See the parent project's README for details.

## Building it
See the parent project's README for details.

## Performing system tests
See the parent project's README for details.

## Installation
### Configuration
This webapp is configured using a properties file located at `/etc/ec-user/application.properties`. It supports the following properties:
* `eurekaclinical.userwebapp.callbackserver`: https://hostname:port
* `eurekaclinical.userwebapp.url`: https://hostname:port/eurekaclinical-user-webapp
* `eurekaclinical.userservice.url`: https://hostname.of.userservice:port/eurekaclinical-user-service
* `cas.url`: https://hostname.of.casserver:port/cas-server
* `eurekaclinical.userwebapp.localregistrationenabled`: true or false to enable/disable registering for an account managed by this project; default is true.
* `eurekaclinical.userwebapp.githuboauthkey`: the key for registering using a GitHub OAuth account.
* `eurekaclinical.userwebapp.githuboauthsecret`:  the secret for registering using a GitHub OAuth account.
* `eurekaclinical.userwebapp.globusoauthkey`:  the key for registering using a GitHub OAuth account.
* `eurekaclinical.userwebapp.globusoauthsecret`:  the secret for registering using a Globus OAuth account.
* `eurekaclinical.userwebapp.googleoauthkey`:  the key for registering using a Google OAuth account.
* `eurekaclinical.userwebapp.googleoauthsecret`:  the secret for registering using a Google OAuth account.
* `eurekaclinical.userwebapp.twitteroauthkey`:   the key for registering using a Twitter OAuth account.
* `eurekaclinical.userwebapp.twitteroauthsecret`:  the secret for registering using a Google OAuth account.
* `eurekaclinical.userwebapp.demomode`: true or false depending on whether to act like a demonstration; default is false.
* `eurekaclinical.userwebapp.ephiprohibited`: true or false depending on whether to display that managing ePHI is prohibited; default is true.

A Tomcat restart is required to detect any changes to the configuration file.

### WAR installation
1) Stop Tomcat.
2) Remove any old copies of the unpacked war from Tomcat's webapps directory.
3) Copy the warfile into the Tomcat webapps directory, renaming it to remove the version if necessary. For example, rename `eureka-webapp-1.0.war` to `eureka-webapp.war`.
4) Start Tomcat.

## Maven dependency
```
<dependency>
    <groupId>org.eurekaclinical</groupId>
    <artifactId>eureka-webapp</artifactId>
    <version>version</version>
</dependency>
```

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eureka-webapp.svg)](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp)

## Getting help
Feel free to contact us at help@eurekaclinical.org.

