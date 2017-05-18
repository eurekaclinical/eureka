# Eureka! Clinical Analytics
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides tools for electronic health record (EHR) phenotyping, that is, finding patients of interest that match specified patterns in clinical and administrative EHR data. Eureka stores these patterns in computable form, and it computes them rapidly in clinical datasets and databases, including i2b2 clinical data warehouses. It supports building a repository of phenotypes representing best practices in how to find patient populations of interest. See http://www.eurekaclinical.org/docs/analytics/ for more information.

## Version 3.0 development series
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

## Proxied REST APIs
You can call all of [eureka](https://github.com/eurekaclinical/eurekaclinical-user-service)'s REST APIs through a proxy provided by `eureka-webapp`. The proxy will forward selected calls to `eureka-protempa-etl` and [eurekaclinical-user-service](https://github.com/eurekaclinical/eurekaclinical-user-service). All other valid URLs will be forwarded to `eureka-services`. Replace `/protected/api` with `/proxy-resource` in your URLs. See the documentation 
### Proxy calls that are forwarded to `eureka-protempa-etl`
* `/proxy-resource/file`
* `/proxy-resource/output`

### Proxy calls that are forwarded to [eurekaclinical-user-service](https://github.com/eurekaclinical/eurekaclinical-user-service)
* `/proxy-resource/users`
* `/proxy-resource/roles`

### Proxy calls that are forwarded to `eureka-services`
Everything else

## Building it
The project uses the maven build tool. 

The system tests that are run automatically during build require more RAM than Java's default. Add the `MAVEN_OPTS` environment variable to your user account's profile, and set the max Java heap size to 4GB. On Linux and Mac, this is specified in your `~/.bash_profile` as follows:
```
export MAVEN_OPTS='-Xmx4g`
```

Typically, you build it by invoking `mvn clean install` at the command line. For simple file changes, not additions or deletions, you can usually use `mvn install`. See https://github.com/eurekaclinical/dev-wiki/wiki/Building-Eureka!-Clinical-projects for more details.

You can build any of the modules separately by appending `-pl <module-name>` to your maven command, where `<module-name>` is the artifact id of the module.

## Performing system tests
You can run this project in an embedded tomcat by executing `mvn tomcat7:run` after you have built it. It will be accessible in your web browser at https://localhost:8443/eureka-webapp/. Your username will be `superuser`.

## Installation
NOTE: we have [Ansible](http://www.ansible.com) provisioning scripts that automate the installation process. Contact use for details. The following provides detail on the steps that those scripts perform. We have omitted general steps such as installation of Tomcat, SSL certificates, and the like.

### Database schema creation
The `eureka-services` and `eureka-protempa-etl` modules each have a database schema. Each has a [Liquibase](http://www.liquibase.org) changelog at `src/main/resources/dbmigration/changelog-master.xml` for creating the schema's objects. [Liquibase 3.3 or greater](http://www.liquibase.org/download/index.html) is required.

#### eureka-services
Perform the following steps:
1) Create a schema for i2b2-export-service in your database.
2) Get a JDBC driver for your database and put it the liquibase lib directory.
3) Run the following:
```
./liquibase \
      --driver=JDBC_DRIVER_CLASS_NAME \
      --classpath=/path/to/jdbcdriver.jar:/path/to/eureka-services.war \
      --changeLogFile=dbmigration/changelog-master.xml \
      --url="JDBC_CONNECTION_URL" \
      --username=DB_USER \
      --password=DB_PASS \
      update
```
4) Add the following Resource tag to Tomcat's `context.xml` file:
```
<Context>
...
    <Resource name="jdbc/EurekaService" auth="Container"
            type="javax.sql.DataSource"
            driverClassName="JDBC_DRIVER_CLASS_NAME"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            url="JDBC_CONNECTION_URL"
            username="DB_USER" password="DB_PASS"
            initialSize="3" maxActive="20" maxIdle="3" minIdle="1"
            maxWait="-1" validationQuery="SELECT 1" testOnBorrow="true"/>
...
</Context>
```

The validation query above is suitable for PostgreSQL. For Oracle and H2, use
`SELECT 1 FROM DUAL`.

#### eureka-protempa-etl
Perform the following steps:
1) Create a schema for i2b2-export-service in your database.
2) Get a JDBC driver for your database and put it the liquibase lib directory.
3) Run the following:
```
./liquibase \
      --driver=JDBC_DRIVER_CLASS_NAME \
      --classpath=/path/to/jdbcdriver.jar:/path/to/eureka-protempa-etl.war \
      --changeLogFile=dbmigration/changelog-master.xml \
      --url="JDBC_CONNECTION_URL" \
      --username=DB_USER \
      --password=DB_PASS \
      update
```
4) Add the following Resource tag to Tomcat's `context.xml` file:
```
<Context>
...
    <Resource name="jdbc/EurekaBackend" auth="Container"
            type="javax.sql.DataSource"
            driverClassName="JDBC_DRIVER_CLASS_NAME"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            url="JDBC_CONNECTION_URL"
            username="DB_USER" password="DB_PASS"
            initialSize="3" maxActive="20" maxIdle="3" minIdle="1"
            maxWait="-1" validationQuery="SELECT 1" testOnBorrow="true"/>
...
</Context>
```

The validation query above is suitable for PostgreSQL. For Oracle and H2, use
`SELECT 1 FROM DUAL`.

### Configuration
Eureka is configured using a properties file located at `/etc/eureka/application.properties`. It supports the following properties:
* `eurekaclinical.userwebapp.url`: https://hostname.of.eurekaclinicaluserwebapp:port/eurekaclinical-user-webapp
* `eurekaclinical.userservice.url`: https://hostname.of.eurekaclinicaluserservice:port/eurekaclinical-user-service
* `cas.url`: https://hostname.of.casserver:port/cas-server
* `eureka.common.callbackserver`: https://hostname:port
* `eureka.common.demomode`: true or false depending on whether to act like a demonstration; default is false.
* `eureka.common.ephiprohibited`: true or false depending on whether to display that managing ePHI is prohibited; default is true.
* `eureka.webapp.registrationenabled`: true or false to enable/disable registering for an account managed by this project; default is true.
* `eureka.support.uri`: URI link for contacting support. Could be http, https, or mailto.
* `eureka.support.uri.name`: Display name of the URI link for contacting support.
* `eureka.webapp.callbackserver`: URL of the server running the webapp; default is https://localhost:8443.
* `eureka.webapp.url`: the URL of the webapp; default is https://localhost:8443/eureka-webapp.
* `eureka.webapp.ephiprohibited`: true or false depending on whether to display that managing ePHI is prohibited; default is true.
* `eureka.webapp.demomode`: true or false depending on whether to act like a demonstration; default is false.
* `eureka.etl.url`: URL of the server running the backend; default is https://localhost:8443/eureka-protempa-etl.
* `eureka.etl.threadpool.size`: the number of threads in the ETL threadpool; default is 4.
* `eureka.etl.callbackserver`: URL of the server running the backend; default is https://localhost:8443.
* `eureka.services.url`: URL of the server running the services layer; default is https://localhost:8443/eureka-services.
* `eureka.services.callbackserver`: URL of the server running the services layer; default is https://localhost:8443.
* `eureka.services.jobpool.size`: the number of threads in the ETL threadpool; default is 5.
* `eureka.services.registration.timeout`: timeout in hours for registration request verification; default is 72.
* `eureka.jstree.searchlimit`: max number of results returned from a concept search; default is 200.
* `eureka.services.defaultprops`: concept subtrees to show in the concept tree: default is Patient PatientDetails Encounter  ICD9:Diagnoses ICD9:Procedures ICD10:Diagnoses ICD10:Procedures LAB:LabTest MED:medications VitalSign

A Tomcat restart is required to detect any changes to the configuration file.

### Tomcat configuration
In the `$CATALINA_HOME/bin/setenv.sh` file, add the following:
```
CATALINA_OPTS="${CATALINA_OPTS} -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true 
-Doracle.jdbc.ReadTimeout=43200000 -Djava.security.egd=file:///dev/urandom"
JAVA_OPTS="${JAVA_OPTS} -Xms512m -Xmx6G"
```

The `oracle.jdbc.ReadTimeout` system property is only needed if using an Oracle database with Eureka. The max heap size may need to be  increased more depending on the data volume being processed.

### WAR installation
1) Stop Tomcat.
2) Remove any old copies of Eureka's three unpacked wars from Tomcat's webapps directory.
3) Copy new warfiles into the tomcat webapps directory.
4) Start Tomcat.

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eureka-webapp.svg)](http://javadoc.io/doc/org.eurekaclinical/eureka-webapp)

## Note on licensing
Out of the box, Eureka! Clinical Analytics is available under the Apache License. If you use the Neo4j plugin provided by the [aiw-neo4j-etl](https://github.com/eurekaclinical/aiw-neo4j-etl) project, due to the licensing of Neo4j, you cannot use the Apache license anymore. For that reason, Eureka! Clinical Analytics is optionally available under the GPL version 3.

## Getting help
Feel free to contact us at help@eurekaclinical.org.
