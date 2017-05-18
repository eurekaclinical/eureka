# Eureka! Services
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides backend services for managing phenotypes, cohorts and running phenotyping jobs.

## Version 3.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-services/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-services)

Version 3 will refactor the `eureka-services` and `eureka-protempa` modules to migrate from a layer architecture to a microservice architecture. It also will improve performance.

## Version history
### Version 2.5.2
As compared with version 1, we refactored and renamed some of the REST APIs.

### Version 1.9
The initial version of the Eureka backend is implemented as a layer architecture, in which the webapp makes all REST calls to `eureka-services`, and `eureka-services` forwards some calls to `eureka-protempa-etl`.

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org)
* Also running
  * The [eureka-webapp](https://github.com/eurekaclinical/eureka/tree/master/eureka-webapp) war
  * The [eureka-protempa-etl](https://github.com/eurekaclinical/eureka/tree/master/eureka-protempa-etl) war
  * The [eurekaclinical-user-webapp](https://github.com/eurekaclinical/eurekaclinical-user-webapp) war
  * The [eurekaclinical-user-service](https://github.com/eurekaclinical/eurekaclinical-user-service) war
  * The [cas-server](https://github.com/eurekaclinical/cas) war

## REST APIs
### `/api/protected/jobs`
Manages phenotyping jobs.

#### Role-based authorization
Must have `research` role.

#### Requires successful authentication
Yes

#### JobSpec object
Used to submit a job request.

Properties:
* `sourceConfigId`: required string containing the name of the data source.
* `destinationId`: required string containing the name of the action.
* `dateRangePhenotypeKey`: optional unique numerical id of a phenotype on which to constrain the date range.
* `earliestDate`: optional timestamp, as milliseconds since the epoch, indicating the lower bound of the date range.
* `earliestDateSide`: optional string indicating on which side of the date range phenotype's interval to apply the earliest date; required if a value for `earliestDate` is specified; may be:
  * `START`: the beginning of the interval.
  * `FINISH`: the end of the interval.
* `latestDate`: optional timestamp, as milliseconds since the epoch, indicating the upper bound of the date range.
* `latestDateSide`: string indicating on which side of the date range phenotype's interval to apply the latest date; required if a value for `latestDate` is specified; may be:
  * `START`: the beginning of the interval.
  * `FINISH`: the end of the interval.
* `updateDate`: boolean indicating whether to update or replace data:
  * `true`: update data
  * `false`: replace data
* `prompts`: an array of SourceConfig objects containing any parameters for accessing the specified data source.
* `propositionIds`: the data and/or phenotypes to retrieve from the data source.
* `name`: optional name for the job.

#### Job object
Created internally when a job is created. This object is read-only.

Properties:
* `id`: unique number identifying the job.
* `startTimestamp`: required timestamp, as milliseconds since the epoch, representing when the job began execution.
* `finishTimestamp`: required timestamp, as milliseconds since the epoch, representing when job execution ended.
* `sourceConfigId`: required string containing the name of the data source.
* `destinationId`: required string containing the name of the action.
* `username`: required string containing the username of who created the job.
* `status`: required string indicating the current status of the job:
  * `STARTING`: job is received but has not started yet.
  * `VALIDATING`: job is being validated.
  * `VALIDATED`: job has passed validation.
  * `STARTED`: job has started execution.
  * `COMPLETED`: job has completed without error.
  * `WARNING`: job has thrown a non-fatal warning.
  * `ERROR`: job has thrown a fatal error.
  * `FAILED`: job has completed in error.
* `jobEvents`: an array of JobEvent objects indicating job status (see below).
* `links`: an array of Link objects that point to resources created by the job (see below).

#### JobEvent object
Represents events occurring during the execution of a job. This object is read-only.

Properties:
* `id`: unique number identifying the job event.
* `status`: the status of the job.
  * `STARTING`: job is received but has not started yet.
  * `VALIDATING`: job is being validated.
  * `VALIDATED`: job has passed validation.
  * `STARTED`: job has started execution.
  * `COMPLETED`: job has completed without error.
  * `WARNING`: job has thrown a non-fatal warning.
  * `ERROR`: job has thrown a fatal error.
  * `FAILED`: job has completed in error.
* `message`: optionally provides additional descriptive information for the job event.
* `exceptionStackTrace`: populated in job events with `ERROR` status with a stack trace.
* `timeStamp`: the timestamp, as milliseconds since the epoch, when this event occurred.

#### Link object
Represents a hyperlink to a resource created by the job.

Properties:
* `url`: required URL of the hyperlink.
* `displayName`: optional name for the link to display in a user interface.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### POST /api/protected/jobs
Submits a job. A JobSpec object is passed in as the body of the request. Returns the URI representing the corresponding Job object.

##### GET /api/protected/jobs/{id}
Gets the Job with the specified numerical unique id.
###### Example:
URL: https://localhost:8443/eureka-services/api/protected/jobs/1
Return: 
```
{"id":1,"startTimestamp":1483992452240,"sourceConfigId":"Spreadsheet","destinationId":"Load into i2b2 on localhost with Eureka ontology","username":"superuser","status":"COMPLETED","jobEvents":[{"id":1,"status":"STARTED","exceptionStackTrace":null,"timeStamp":1483992452303,"message":"Processing started"},{"id":2,"status":"COMPLETED","exceptionStackTrace":null,"timeStamp":1483992511412,"message":"Processing completed without error"}],"links":[],"getStatisticsSupported":false,"finishTimestamp":1483992511412}
```

##### GET /api/protected/jobs[?order=asc|desc]
Gets all jobs for the current user. Optionally, you can specify whether jobs will be returned in ascending or descending order.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/jobs?order=desc
Return: 
[{"id":2,"startTimestamp":1483992674788,"sourceConfigId":"Spreadsheet","destinationId":"Load into i2b2 on localhost with Eureka ontology","username":"superuser","status":"COMPLETED","jobEvents":[{"id":3,"status":"STARTED","exceptionStackTrace":null,"timeStamp":1483992674792,"message":"Processing started"},{"id":4,"status":"COMPLETED","exceptionStackTrace":null,"timeStamp":1483992752190,"message":"Processing completed without error"}],"links":[],"getStatisticsSupported":false,"finishTimestamp":1483992752190},{"id":1,"startTimestamp":1483992452240,"sourceConfigId":"Spreadsheet","destinationId":"Load into i2b2 on localhost with Eureka ontology","username":"superuser","status":"COMPLETED","jobEvents":[{"id":1,"status":"STARTED","exceptionStackTrace":null,"timeStamp":1483992452303,"message":"Processing started"},{"id":2,"status":"COMPLETED","exceptionStackTrace":null,"timeStamp":1483992511412,"message":"Processing completed without error"}],"links":[],"getStatisticsSupported":false,"finishTimestamp":1483992511412}]

### GET /api/protected/jobs/status?jobId=jobId&userId=userId&state=foo&from=bar&to=baz
Gets all jobs for the current user, optionally filtered by job id, user id, state and/or date range (from date, to date).

### GET /api/protected/jobs/{jobId}/stats/{key}
Gets summary statistics for the specified job.

### GET /api/protected/jobs/latest
Gets the latest submitted job for the user.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/jobs/latest
#### Return: 
[{"id":2,"startTimestamp":1483992674788,"sourceConfigId":"Spreadsheet","destinationId":"Load into i2b2 on localhost with Eureka ontology","username":"superuser","status":"COMPLETED","jobEvents":[{"id":3,"status":"STARTED","exceptionStackTrace":null,"timeStamp":1483992674792,"message":"Processing started"},{"id":4,"status":"COMPLETED","exceptionStackTrace":null,"timeStamp":1483992752190,"message":"Processing completed without error"}],"links":[],"getStatisticsSupported":false,"finishTimestamp":1483992752190}]

## File uploads

### POST /protected/file/upload/{sourceConfigId}/{sourceId}
Submit a multipart form containing a file with form parameter name file for source config with the specified unique name (sourceConfigId). The sourceId is a source config-specific identifier for the file.

## Destinations

### GET /api/protected/destinations[?type=[I2B2,COHORT,PATIENT_SET_SENDER]
Gets all data destinations visible to the current user.  Optionally, filter the returned destinations by type (I2B2=i2b2 database destination, COHORT=cohort specified in the cohorts screens, PATIENT_SET_SENDER=patient set sender for the i2b2 patient set sender plugin).
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/destinations?type=I2B2
#### Return: 
[{"type":"I2B2","id":1,"name":"Load into i2b2 on localhost with Eureka ontology","description":null,"phenotypeFields":null,"links":[],"ownerUserId":1,"read":true,"write":true,"execute":true,"getStatisticsSupported":true,"jobConceptListSupported":true,"requiredConcepts":["Encounter"],"created_at":1430774820000,"updated_at":1430774820000}]

### GET /api/protected/destinations/{id}
Gets the data destination with the specified unique name (id), if it is visible to the current user.

### POST /api/protected/destinations
Create a new data destination.
Returns nothing.

### PUT /api/protected/destinations
Updates an existing data destination
Returns nothing.

### DELETE /api/protected/destinations/{id}
Deletes the data destination with the specified unique numerical id.
Returns nothing.

## Frequency types

### GET /api/protected/frequencytypes
Returns a list of possible frequency types in ascending order.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/frequencytypes
#### Return: 
[{"id":1,"name":"at least","description":"at least","rank":1,"default":true},{"id":2,"name":"first","description":"first","rank":2,"default":false}]

### GET /api/protected/frequencytypes/{id}
Returns the frequency type with the specified numerical unique id, or a 404 (NOT FOUND) status code if no frequency type with the specified id is supported.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/frequencytypes/1
#### Return: 
{"id":1,"name":"at least","description":"at least","rank":1,"default":true}

### GET /api/protected/frequencytypes/byname/{name}
Returns the frequency type with the specified unique name, or a 404 (NOT FOUND) status code if no frequency type exists with the specified name.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/frequencytypes/byname/at%20least
#### Return: 
{"id":1,"name":"at least","description":"at least","rank":1,"default":true}

## Temporal relation operators

### GET /api/protected/relationops
Gets a list of the possible temporal relation operators in rank order.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/relationops
#### Return: 
[{"id":1,"name":"before","description":"before","rank":1,"type":"SEQUENTIAL","default":true},{"id":2,"name":"after","description":"after","rank":2,"type":"SEQUENTIAL","default":false},{"id":3,"name":"around","description":"around","rank":3,"type":"OVERLAPPING","default":false}]

### GET /api/protected/relationops/{id}
Returns the temporal relation operator with the specified numerical unique id, or a 404 (NOT FOUND) status code if no temporal relation operator exists with the specified id.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/relationops/1
#### Return: 
{"id":1,"name":"before","description":"before","rank":1,"type":"SEQUENTIAL","default":true}

### GET /api/protected/relationops/byname/{name}
Returns the temporal relation operator with the specified unique name, or a 404 (NOT FOUND) status code if no temporal relation operator with the specified name is supported.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/relationops/byname/before
#### Return: 
{"id":1,"name":"before","description":"before","rank":1,"type":"SEQUENTIAL","default":true}

## Value threshold operators

### GET /api/protected/thresholdsops
Gets a list of the supported value threshold operators.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/thresholdsops
#### Return: 
[{"id":1,"name":"any","description":"any"},{"id":2,"name":"all","description":"all"}]

### GET /api/protected/thresholdsops/{id}
Returns the value threshold operator with the specified numerical unique id, or a 404 (NOT FOUND) status code if no value threshold operator exists with the specified id.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/thresholdsops/1
#### Return: 
{"id":1,"name":"any","description":"any"}

### GET /api/protected/thresholdsops/byname/{name}
Returns the value threshold operator with the specified unique name, or a 404 (NOT FOUND) status code if no value threshold operator with the specified name is supported.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/thresholdsops/byname/any
#### Return: 
{"id":1,"name":"any","description":"any"}

## Value comparators

### GET /api/protected/valuecomps
Gets a list of the supported value comparators in rank order.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/valuecomps
#### Return: 
[{"id":5,"name":"<","description":"less than","threshold":"UPPER_ONLY","rank":1},{"id":6,"name":"<=","description":"less than or equal to","threshold":"UPPER_ONLY","rank":2},{"id":1,"name":"=","description":"equals","threshold":"BOTH","rank":3},{"id":2,"name":"not=","description":"not equals","threshold":"BOTH","rank":4},{"id":3,"name":">","description":"greater than","threshold":"LOWER_ONLY","rank":5},{"id":4,"name":">=","description":"greater than or equal to","threshold":"LOWER_ONLY","rank":6}]

### GET /api/protected/valuecomps/{id}
Returns the value comparator operator with the specified numerical unique id, or a 404 (NOT FOUND) status code if no value comparator operator exists with the specified id.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/valuecomps/1
#### Return: 
{"id":1,"name":"=","description":"equals","threshold":"BOTH","rank":3}

### GET /api/protected/valuecomps/byname/{name}
Returns the value comparator operator with the specified unique name, or a 404 (NOT FOUND) status code if no value comparator operator with the specified name is supported.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/valuecomps/byname/=
#### Return: 
{"id":1,"name":"=","description":"equals","threshold":"BOTH","rank":3}

## Time units

### GET /api/protected/timeunits
Gets a list of the supported time units in rank order.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/timeunits
#### Return: 
[{"id":3,"name":"minute","description":"minutes","rank":1,"default":false},{"id":2,"name":"hour","description":"hours","rank":2,"default":false},{"id":1,"name":"day","description":"days","rank":3,"default":true}]

### GET /api/protected/timeunits/{id}
Returns the time unit with the specified numerical unique id, or a 404 (NOT FOUND) status code if no time unit exists with the specified id.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/timeunits/1
#### Return: 
{"id":1,"name":"day","description":"days","rank":3,"default":true}

### GET /api/protected/timeunits/byname/{name}
Returns the time unit with the specified unique name, or a 404 (NOT FOUND) status code if no time unit with the specified name is supported.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/timeunits/byname/day
#### Return: 
{"id":1,"name":"day","description":"days","rank":3,"default":true}

## Source configs

### GET /api/protected/sourceconfig
Gets a list of the available source configurations.

### GET /api/protected/sourceconfig/{id}
Returns the source configuration with the specified numerical unique id, or the 404 (NOT FOUND) status code if no source configuration with that id exists that is accessible by the current user.

### GET /api/protected/sourceconfig/parameters/list
Gets a list of the available source configurations' parameter names and values.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/sourceconfig/parameters/list
#### Return: 
[{"id":"Spreadsheet","name":"Spreadsheet","uploads":[{"name":"Eureka Spreadsheet Data Source Backend","sourceId":null,"acceptedMimetypes":["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],"sampleUrl":"../docs/sample.xlsx","required":true}]}]

### GET /api/protected/sourceconfig/parameters/{id}
Returns the parameter names and values for the source configuration with the specified numerical unique id, or the 404 (NOT FOUND) status code if no source configuration with that id exists that is accessible by the current user.

## User-defined concepts

### GET /api/protected/phenotypes[?summarize=yes|no]
Returns the concepts accessible by the current user. Optionally, return each concept in a summarized form suitable for listing.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/phenotypes?summarize=yes
#### Return: 
[{"type":"CATEGORIZATION","id":2,"key":"USER:testCategorization","userId":1,"description":"test","displayName":"testCategorization","inSystem":false,"created":1484772736590,"lastModified":1484772736590,"summarized":false,"internalNode":false,"children":[{"id":null,"phenotypeKey":"Patient","phenotypeDescription":"","phenotypeDisplayName":"Patient","hasDuration":null,"minDuration":null,"minDurationUnits":null,"maxDuration":null,"maxDurationUnits":null,"hasPropertyConstraint":null,"property":null,"propertyValue":null,"type":"SYSTEM","categoricalType":null,"inSystem":true},{"id":null,"phenotypeKey":"PatientDetails","phenotypeDescription":"","phenotypeDisplayName":"Patient Details","hasDuration":null,"minDuration":null,"minDurationUnits":null,"maxDuration":null,"maxDurationUnits":null,"hasPropertyConstraint":null,"property":null,"propertyValue":null,"type":"SYSTEM","categoricalType":null,"inSystem":true}],"categoricalType":"CONSTANT"}]

### GET /api/protected/phenotypes/{key}[?summarize=yes|no]
Returns the concept with the specified key, or the 404 (NOT FOUND) status code if no such concept exists. Optionally, return each concept in a summarized form suitable for listing.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/phenotypes/USER:testCategorization?summarize=yes
#### Return: 
{"type":"CATEGORIZATION","id":2,"key":"USER:testCategorization","userId":1,"description":"test","displayName":"testCategorization","inSystem":false,"created":1484772736590,"lastModified":1484772736590,"summarized":false,"internalNode":false,"children":[{"id":null,"phenotypeKey":"Patient","phenotypeDescription":"","phenotypeDisplayName":"Patient","hasDuration":null,"minDuration":null,"minDurationUnits":null,"maxDuration":null,"maxDurationUnits":null,"hasPropertyConstraint":null,"property":null,"propertyValue":null,"type":"SYSTEM","categoricalType":null,"inSystem":true},{"id":null,"phenotypeKey":"PatientDetails","phenotypeDescription":"","phenotypeDisplayName":"Patient Details","hasDuration":null,"minDuration":null,"minDurationUnits":null,"maxDuration":null,"maxDurationUnits":null,"hasPropertyConstraint":null,"property":null,"propertyValue":null,"type":"SYSTEM","categoricalType":null,"inSystem":true}],"categoricalType":"CONSTANT"}

### POST /api/protected/phenotypes
Creates a new concept.

### PUT /api/protected/phenotypes
Saves an existing concept.

### DELETE /api/protected/phenotypes/{userId}/{key}
Deletes the specified concept.

## System concepts

### GET /api/protected/concepts
Returns the top-level system concepts accessible by the current user. Optionally, return each concept in a summarized form suitable for listing.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/concepts
#### Return: 
[{"type":"SYSTEM","id":null,"key":"Patient","userId":null,"description":"","displayName":"Patient","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":false,"systemType":"CONSTANT","children":null,"properties":["patientId"],"parent":false},{"type":"SYSTEM","id":null,"key":"PatientDetails","userId":null,"description":"","displayName":"Patient Details","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":false,"systemType":"CONSTANT","children":null,"properties":["ageInYears","dateOfBirth","dateOfDeath","gender","language","maritalStatus","race","vitalStatus","patientId"],"parent":false},{"type":"SYSTEM","id":null,"key":"Encounter","userId":null,"description":"","displayName":"Encounter","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":false,"systemType":"EVENT","children":null,"properties":["age","type","encounterId"],"parent":false},{"type":"SYSTEM","id":null,"key":"VitalSign","userId":null,"description":"","displayName":"Vital Sign","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"PRIMITIVE_PARAMETER","children":null,"properties":[],"parent":true},{"type":"SYSTEM","id":null,"key":"\\ACT\\Medications\\","userId":null,"description":"","displayName":"Medications","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":[],"parent":true},{"type":"SYSTEM","id":null,"key":"ICD9:Diagnoses","userId":null,"description":"","displayName":"ICD9 Diagnosis Codes","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":["DXPRIORITY"],"parent":true},{"type":"SYSTEM","id":null,"key":"\\ACT\\Labs\\","userId":null,"description":"","displayName":"Lab Test Results","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"PRIMITIVE_PARAMETER","children":null,"properties":[],"parent":true},{"type":"SYSTEM","id":null,"key":"ICD9:Procedures","userId":null,"description":"","displayName":"ICD9 Procedure Codes","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":[],"parent":true},{"type":"SYSTEM","id":null,"key":"ICD10PCS:Procedures","userId":null,"description":"","displayName":"ICD10 Procedure Codes","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":[],"parent":true},{"type":"SYSTEM","id":null,"key":"ICD10:Diagnoses","userId":null,"description":"","displayName":"ICD10 Diagnosis Codes","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":[],"parent":true}]

### POST /api/protected/concepts
Form parameters:
* key: The keys of the system concepts of interest (optional). If omitted, the empty list is returned.
* summarize: yes or no if you want returned concepts in a summarized form suitable for listing (optional).
Returns the requested system concepts that are accessible by the current user. Optionally, returns each concept in a summarized form suitable for listing.

### GET /api/protected/concepts/{key}
Gets the requested system concept with the specified key or the 404 (NOT FOUND) status code if no such system concept exists and is accessible to the current user. 
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/concepts/Patient
#### Return: 
{"type":"SYSTEM","id":null,"key":"Patient","userId":null,"description":"","displayName":"Patient","inSystem":true,"created":null,"lastModified":null,"summarized":false,"internalNode":false,"systemType":"CONSTANT","children":[],"properties":["patientId"],"parent":false}

### GET /api/protected/concepts/propsearch/{searchKey}
Gets the system concepts with the specified text in their display name, case insensitive.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/concepts/propsearch/ICD9%20Procedure%20Codes
#### Return: 
[{"type":"SYSTEM","id":null,"key":"ICD9:Procedures","userId":null,"description":"","displayName":"ICD9 Procedure Codes","inSystem":true,"created":null,"lastModified":null,"summarized":true,"internalNode":true,"systemType":"EVENT","children":null,"properties":[],"parent":true}]

### GET /api/protected/concepts/search/{searchKey}
Gets a list of the keys of the system concepts with the specified text in their display name, case insensitive.
#### Example:
#### URL: 
https://localhost:8443/eureka-services/api/protected/concepts/search/Patient
#### Return: 
["ICD10:Diagnoses","ICD10:S00-T88","ICD10:S00-S09","ICD10:S06","ICD10:S06.3","ICD10:S06.37","ICD10:S06.376","ICD10:S06.6","ICD10:S06.6X","ICD10:S06.6X6","ICD10:S06.33","ICD10:S06.336","ICD10:S06.8","ICD10:S06.81","ICD9:Diagnoses","ICD9:V-codes","ICD9:V70-V82","ICD9:V76","ICD9:V76.1","ICD10:Z00-Z99","ICD10:Z40-Z53","ICD10:Z53","ICD10:Z53.2","ICD10:T30-T32","ICD10:T30","ICD9:E-codes","ICD9:E870-E879","ICD10:S06.89","ICD10:S06.82","ICD10:S06.30","ICD10:V00-Y99","ICD10:Y83-Y84","ICD10:Y84","ICD10:S06.38","ICD10:S06.386","ICD10:S06.306","ICD10:S06.0","ICD10:S06.0X","ICD10:S06.0X6","ICD10:S06.34","ICD10:S06.346","ICD10:S06.4","ICD10:S06.4X","ICD10:S06.4X6","ICD10:Y90-Y99","ICD10:Y92","ICD10:Y92.2","ICD10:Y92.23","ICD10:S06.32","ICD10:S06.326","ICD10:S06.31","ICD10:S06.36","ICD10:S06.366","ICD10:F01-F99","ICD10:F60-F69","ICD10:F68","ICD10:S06.2","ICD10:S06.2X","ICD10:S06.9","ICD10:S06.9X","ICD10:S06.9X6","ICD10:S06.1","ICD10:S06.1X","ICD10:S06.1X6","ICD10:Z77-Z99","ICD10:Z91","ICD10:Z91.1","ICD10:S06.826","ICD9:V60-V69","ICD9:V68","ICD9:V68.8","ICD10:O00-O9A","ICD10:O20-O29","ICD10:O26","ICD10:Y62-Y69","ICD10:Y65","ICD10:Y65.5","ICD10:R00-R99","ICD10:R90-R94","ICD10:R93","ICD10:Y92.5","ICD10:Y92.53","ICD10:S06.2X6","ICD10:Z20-Z28","ICD10:Z28","ICD10:Z28.2","ICD10:S06.316","ICD10:S06.35","ICD10:S06.356","ICD9:E878","ICD10:S06.5","ICD10:S06.5X","ICD10:Z91.13","ICD9:V64","ICD10:S06.896","ICD10:S06.816","ICD9:E873","ICD10:Y63","ICD10:Z91.12","ICD10:Z30-Z39","ICD10:Z31","ICD10:Z31.4","ICD10:Z31.44","ICD10:Z28.0","ICD9:E879","ICD9:E875","ICD10:Z69-Z76","ICD10:Z76","ICD10:Z76.8","ICD10:Z53.0","ICD10:S06.5X6","ICD10:O26.2","ICD10:Z70","ICD10:Y83","ICD10:Z28.8","ICD10:Z31.8"]

## Building it
See the parent project's [README.md](https://github.com/eurekaclinical/eureka/blob/master/README.md).

## Performing system tests
See the parent project's [README.md](https://github.com/eurekaclinical/eureka/blob/master/README.md).

## Installation
### Configuration
This webapp is configured using a properties file located at `/etc/eureka/application.properties`. It supports the following properties:
* `cas.url`: https://hostname.of.casserver:port/cas-server
* `eureka.common.callbackserver`: https://hostname:port
* `eureka.common.demomode`: true or false depending on whether to act like a demonstration; default is false.
* `eureka.common.ephiprohibited`: true or false depending on whether to display that managing ePHI is prohibited; default is true.
* `eureka.support.uri`: URI link for contacting support. Could be http, https, or mailto.
* `eureka.support.uri.name`: Display name of the URI link for contacting support.
* `eureka.services.url`: URL of the server running the services layer; default is https://localhost:8443/eureka-services.
* `eureka.services.callbackserver`: URL of the server running the services layer; default is https://localhost:8443.
* `eureka.services.jobpool.size`: the number of threads in the ETL threadpool; default is 5.
* `eureka.services.registration.timeout`: timeout in hours for registration request verification; default is 72.
* `eureka.services.defaultprops`: concept subtrees to show in the concept tree: default is Patient PatientDetails Encounter  ICD9:Diagnoses ICD9:Procedures ICD10:Diagnoses ICD10:Procedures LAB:LabTest MED:medications VitalSign

A Tomcat restart is required to detect any changes to the configuration file.

### WAR installation
1) Stop Tomcat.
2) Remove any old copies of the unpacked war from Tomcat's webapps directory.
3) Copy the warfile into the Tomcat webapps directory, renaming it to remove the version if necessary. For example, rename `eureka-services-1.0.war` to `eureka-services.war`.
4) Start Tomcat.

## Maven dependency
```
<dependency>
    <groupId>org.eurekaclinical</groupId>
    <artifactId>eureka-services</artifactId>
    <version>version</version>
</dependency>
```

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eureka-services) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eureka-services.svg)](http://javadoc.io/doc/org.eurekaclinical/eureka-services)

## Getting help
Feel free to contact us at help@eurekaclinical.org.

