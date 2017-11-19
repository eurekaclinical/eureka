# Eureka! Services
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides backend services for managing phenotypes, cohorts and running phenotyping jobs.

## Version 3.0 development series
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-services/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eureka-services)

Version 3 will refactor the `eureka-services` and `eureka-protempa-etl` modules to migrate from a layer architecture to a microservice architecture. It also will improve performance.

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
* `dateRangePhenotypeKey`: optional unique key of a phenotype or concept on which to constrain the date range.
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
* `prompts`: an array of SourceConfig objects containing any parameters for accessing the specified data source (see below).
* `propositionIds`: the keys of the data and/or phenotypes to retrieve from the data source.
* `name`: optional name for the job.

#### SourceConfig object
For specifying values of a source config's parameters.

Properties:
* `id`: the unique id string of the source config.
* `dataSourceBackends`: an array representing the data source backends that are being parameterized:
  * `id`: the id string of the data source backend.
  * `options`: an array of the parameters to set:
    * `name`: the unique name of the parameter.
    * `value`: the value of the parameter.

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
* `getStatisticsSupported`: boolean indicating whether the `/api/protected/jobs/{id}/stats` call is supported for this job.

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

#### Statistics object
Represents summary statistics of the resource created by the job.

Properties:
* `numberOfKeys`: the number of patients in the resource.
* `counts`: a map of concept or phenotype to the number of times it appears in the resource.
* `childrenToParents`: a map from each concept or phenotype to its parent concepts or phenotypes.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### POST `/api/protected/jobs`
Submits a job. A JobSpec object is passed in as the body of the request. Returns the URI representing the corresponding Job object.

##### GET `/api/protected/jobs/{id}`
Gets the Job with the specified numerical unique id.
###### Example:
URL: https://localhost:8443/eureka-services/api/protected/jobs/1

Return: 
```
{ "id":1,
  "startTimestamp":1483992452240,
  "sourceConfigId":"Spreadsheet",
  "destinationId":"Load into i2b2 on localhost with Eureka ontology",
  "username":"superuser",
  "status":"COMPLETED",
  "jobEvents":[
    { "id":1,
      "status":"STARTED",
      "exceptionStackTrace":null,
      "timeStamp":1483992452303,
      "message":"Processing started"},       
    { "id":2,
      "status":"COMPLETED",
      "exceptionStackTrace":null,
      "timeStamp":1483992511412,
      "message":"Processing completed without error"}],
  "links":[],
  "getStatisticsSupported":false,
  "finishTimestamp":148399251141
}
```

##### GET `/api/protected/jobs[?order=asc|desc]`
Gets all jobs for the current user. Optionally, you can specify whether jobs will be returned in ascending or descending order.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/jobs?order=desc

Return:
```
[
  { "id":2,
    "startTimestamp":1483992674788,
    "sourceConfigId":"Spreadsheet",
    "destinationId":"Load into i2b2 on localhost with Eureka ontology",
    "username":"superuser",
    "status":"COMPLETED",
    "jobEvents": [
      { "id":3,
        "status":"STARTED",
        "exceptionStackTrace":null,
        "timeStamp":1483992674792,
        "message":"Processing started"},
      { "id":4,
        "status":"COMPLETED",
        "exceptionStackTrace":null,
        "timeStamp":1483992752190,
        "message":"Processing completed without error"}
    ],
    "links":[],
    "getStatisticsSupported":false,
    "finishTimestamp":1483992752190
  },
  { "id":1,
    "startTimestamp":1483992452240,
    "sourceConfigId":"Spreadsheet",
    "destinationId":"Load into i2b2 on localhost with Eureka ontology",
    "username":"superuser",
    "status":"COMPLETED",
    "jobEvents": [
      { "id":1,
        "status":"STARTED",
        "exceptionStackTrace":null,
        "timeStamp":1483992452303,
        "message":"Processing started"},
      { "id":2,
        "status":"COMPLETED",
        "exceptionStackTrace":null,
        "timeStamp":1483992511412,
        "message":"Processing completed without error"}
    ],
    "links":[],
    "getStatisticsSupported":false,
    "finishTimestamp":1483992511412
  }
]
```

##### GET `/api/protected/jobs/status?jobId=jobId&userId=userId&state=foo&from=bar&to=baz`
Gets an array of all Jobs for the current user, optionally filtered by job id, user id, state (status) and/or date range (from date, to date).

##### GET `/api/protected/jobs/{jobId}/stats[/{key}]`
Gets a Statistics object for the specified Job, optionally constraining the results to statistics about the concept or phenotype with the specified key.

##### GET `/api/protected/jobs/latest`
Gets the most recently submitted Job for the user.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/jobs/latest

Returns:
```
[
  { "id":2,
    "startTimestamp":1483992674788,
    "sourceConfigId":"Spreadsheet",
    "destinationId":"Load into i2b2 on localhost with Eureka ontology",
    "username":"superuser",
    "status":"COMPLETED",
    "jobEvents":[
      { "id":3,
        "status":"STARTED",
        "exceptionStackTrace":null,
        "timeStamp":1483992674792,
        "message":"Processing started" },
        { "id":4,
          "status":"COMPLETED",
          "exceptionStackTrace":null,
          "timeStamp":1483992752190,
          "message":"Processing completed without error" }
    ],
    "links":[],
    "getStatisticsSupported":false,
    "finishTimestamp":1483992752190}
]
```

### `/api/protected/file`
Manages data file uploads.

#### Role-based authorization
Must have `research` role.

#### Requires successful authentication
Yes

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### POST /protected/file/upload/{sourceConfigId}/{sourceId}
Submit a multipart form containing a file with form parameter name `file` for the source config with the specified unique name (`sourceConfigId`). The sourceId is a source config-specific identifier for the file.

### `/api/protected/destinations`
Manages job actions.

#### Role-based authorization
Must have `research` role.

#### Requires successful authentication
Yes

#### Destination object
Destinations are job actions that create a resource. They all have the following properties:

Properties:
* `id`: unique number identifying the cohort (set by the server on object creation, and required thereafter).
* `type`: always must have value `COHORT`.
* `name`: required unique name of the cohort.
* `description`: an optional description of the cohort.
* `links`: an array of Link objects that point to resources related to the cohort see Link object above.
* `ownerUserId`: required username string of the owning user.
* `read`: required boolean indicating whether the user may read this object.
* `write`: required boolean indicating whether the user may update this object.
* `execute`: required boolean indicating whether the user may use this cohort specification as an action.
* `createdAt`: timestamp, in milliseconds since the epoch, indicating when this cohort specification was created; populated server-side.
* `updatedAt`: timestamp, in milliseconds since the epoch, indicating when this cohort specification was updated; populated server-side.
* `getStatisticsSupported`: required boolean indicating whether the resource created by a job executing this action supports getting statistics.
* `jobConceptListSupported`: required boolean indicating whether a job executing this action has a concept/phenotype list.
* `requiredConcepts`: any concepts or phenotypes that must be in the concept list.

#### CohortDestination object
Creates a patient set containing only patients who match the specified criteria.

Properties:
* `cohort`: a required Cohort object (see below).

#### Cohort object
A specification of a patient cohort in terms of concepts and phenotypes.

Properties:
* `id`: unique numerical id of the cohort (set by the server on object creation, and required thereafter).
* `node`: required Literal or BinaryOperator object (see below). Use a Literal object if the cohort is defined by a single concept or phenotype. If the cohort is defined by multiple concepts or phenotypes, use a chain of BinaryOperator objects ending with a Literal object.

#### BinaryOperator object
`ANDs` two nodes together.

Properties:
`leftNode`: required Literal object.
`op`: the operator, always `AND`.
`rightNode`: required BinaryOperator or Literal object.

#### Literal object
Represents a concept or phenotype included in a cohort definition.

Properties:
* `name`: required unique key of the concept or phenotype.
* `start`: always `null`.
* `finish`: always `null`.

#### I2b2Destination object
Populates an i2b2 data warehouse.

Properties:
No additional properties

#### Neo4jDestination object
Populates a Neo4j database.

Properties:
No additional properties.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/destinations[?type=[I2B2,COHORT,PATIENT_SET_SENDER]`
Gets all data destinations visible to the current user.  Optionally, filter the returned destinations by type:
* `I2B2`: i2b2 database destination.
* `COHORT`: Cohort specified in the cohorts screens.
* `PATIENT_SET_SENDER`: patient set sender.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/destinations?type=I2B2

Returns:
```
[
  { "type":"I2B2",
    "id":1,
    "name":"Load into i2b2 on localhost with Eureka ontology",
    "description":null,
    "phenotypeFields":null,
    "links[],
    "ownerUserId":1,
    "read":true,
    "write":true,
    "execute":true,
    "getStatisticsSupported":true,
    "jobConceptListSupported":true,
    "requiredConcepts":["Encounter"],
    "created_at":1430774820000,
    "updated_at":1430774820000 }
]
```

##### GET `/api/protected/destinations/{id}`
Gets the data destination with the specified unique name (id), if it is visible to the current user.

##### POST `/api/protected/destinations`
Create a new data destination, returning a URI representing the created destination object.

##### PUT `/api/protected/destinations`
Updates an existing data destination
Returns nothing.

##### DELETE `/api/protected/destinations/{id}`
Deletes the destination with the specified unique numerical id. Returns nothing.

### `/api/protected/frequencytypes`
Read-only, enumerates the allows types of frequency phenotypes.

#### Role-based authorization
None

#### Requires successful authentication
Yes

#### FrequencyType object
Names and describes a frequency type.

Properties:
* `id`: unique numerical id of the frequency type.
* `name`: unique name of the frequency type.
* `description`: unique description of the frequency type.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/frequencytypes`
Returns a list of possible frequency types in ascending order.

##### Example:
URL: https://localhost:8443/eureka-services/api/protected/frequencytypes

Returns:
```
[
  { "id":1,
    "name":"at least",
    "description":"at least",
    "rank":1,
    "default":true},
  { "id":2,
    "name":"first",
    "description":"first",
    "rank":2,
    "default":false}
]
```

##### GET `/api/protected/frequencytypes/{id}`
Returns the frequency type with the specified numerical unique id.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/frequencytypes/1

Returns:
```
{ "id":1,
  "name":"at least",
  "description":"at least",
  "rank":1,
  "default":true }
```

##### GET `/api/protected/frequencytypes/byname/{name}`
Returns the frequency type with the specified unique name.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/frequencytypes/byname/at%20least

Returns:
```
{ "id":1,
  "name":"at least",
  "description":"at least",
  "rank":1,
  "default":true }
```

### `/api/protected/relationops`
Read-only, enumerates the allows types of relation operations.

#### Role-based authorization
None

#### Requires successful authentication
Yes

#### RelationOp object
Names and describes a relation operator.

Properties:
* `id`: unique numerical id of the relation operator.
* `name`: unique name of the relation operator.
* `description`: unique description of the relation operator.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/relationops`
Gets a list of the possible temporal relation operators in rank order.
###### Example:
URL: https://localhost:8443/eureka-services/api/protected/relationops

Returns: 
```
[
  { "id":1,
    "name":"before",
    "description":"before",
    "rank":1,
    "type":"SEQUENTIAL",
    "default":true},
  { "id":2,
    "name":"after",
    "description":"after",
    "rank":2,
    "type":"SEQUENTIAL",
    "default":false},
  { "id":3,
    "name":"around",
    "description":"around",
    "rank":3,
    "type":"OVERLAPPING",
    "default":false}
]
```

##### GET `/api/protected/relationops/{id}`
Returns the temporal relation operator with the specified numerical unique id.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/relationops/1

Returns:
```
{"id":1,"name":"before","description":"before","rank":1,"type":"SEQUENTIAL","default":true}
```

##### GET /api/protected/relationops/byname/{name}
Returns the temporal relation operator with the specified unique name.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/relationops/byname/before

Returns: 
```
{"id":1,"name":"before","description":"before","rank":1,"type":"SEQUENTIAL","default":true}
```
### `/api/protected/thresholdops`
Read-only, enumerates the allows types of value threshold operations.

#### Role-based authorization
None

#### Requires successful authentication
Yes

#### ThresholdOp object

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/thresholdops`
Gets a list of the supported value threshold operators.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/thresholdsops

Returns:
```
[{"id":1,"name":"any","description":"any"},{"id":2,"name":"all","description":"all"}]
```

##### GET `/api/protected/thresholdsops/{id}`
Returns the value threshold operator with the specified numerical unique id.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/thresholdsops/1

Returns:
```
{"id":1,"name":"any","description":"any"}
```

##### GET `/api/protected/thresholdsops/byname/{name}`
Returns the value threshold operator with the specified unique name.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/thresholdsops/byname/any

Returns:
```
{"id":1,"name":"any","description":"any"}
```

### `/api/protected/valuecomps`
Read-only, enumerates the allows types of value comparators.

#### Role-based authorization
None

#### Requires successful authentication
Yes

#### ValueComparator object
Properties:
* `id`: unique numerical id of the relation operator.
* `name`: unique name of the relation operator.
* `description`: unique description of the relation operator.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/valuecomps`
Gets a list of the supported value comparators in rank order.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/valuecomps

Returns:
```
[
  { "id":5,
    "name":"<",
    "description":"less than",
    "threshold":"UPPER_ONLY","rank":1}, 
  { "id":6,
    "name":"<=",
    "description":"less than or equal to",
    "threshold":"UPPER_ONLY",
    "rank":2},
  { "id":1,
    "name":"=",
    "description":"equals",
    "threshold":"BOTH",
    "rank":3},
  { "id":2,
    "name":"not=",
    "description":"not equals",
    "threshold":"BOTH",
    "rank":4},
  { "id":3,
    "name":">",
    "description":"greater than",
    "threshold":"LOWER_ONLY",
    "rank":5},
  { "id":4,
    "name":">=",
    "description":"greater than or equal to",
    "threshold":"LOWER_ONLY",
    "rank":6}
]
```

##### GET `/api/protected/valuecomps/{id}`
Returns the value comparator operator with the specified numerical unique id.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/valuecomps/1

Returns:
```
{"id":1,"name":"=","description":"equals","threshold":"BOTH","rank":3}
```

##### GET `/api/protected/valuecomps/byname/{name}`
Returns the value comparator operator with the specified unique name.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/valuecomps/byname/=

Returns:
```
{"id":1,"name":"=","description":"equals","threshold":"BOTH","rank":3}
```

### `/api/protected/timeunits`
Read-only, enumerates the allowed time units.

#### Role-based authorization
None

#### Requires successful authentication
Yes

#### TimeUnit object
Properties:
* `id`: unique numerical id of the time unit.
* `name`: unique name of the time unit.
* `description`: unique description of the time unit.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/timeunits`
Gets an array of the supported time units in rank order.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/timeunits

Returns:
```
[
  {"id":3,"name":"minute","description":"minutes","rank":1,"default":false},
  {"id":2,"name":"hour","description":"hours","rank":2,"default":false},
  {"id":1,"name":"day","description":"days","rank":3,"default":true}
]
```

##### GET /api/protected/timeunits/{id}
Returns the time unit with the specified numerical unique id.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/timeunits/1

Returns:
```
{"id":1,"name":"day","description":"days","rank":3,"default":true}
```

##### GET /api/protected/timeunits/byname/{name}
Returns the time unit with the specified unique name.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/timeunits/byname/day

Returns:
```
{"id":1,"name":"day","description":"days","rank":3,"default":true}
```

### `/api/protected/sourceconfig`
Manages data source configurations.

#### Role-based authorization
Must have `researcher` role.

#### Requires successful authentication
Yes

#### SourceConfig object
Fully specifies a source configuration.

Properties:
* `id`: unique id number of the source config (set by the server on object creation, and required thereafter).
* `displayName`: optional display name of the source config for display in user interfaces.
* `ownerUsername`: required username of the owning user.
* `dataSourceBackends`: an array representing sources of data:
  * `id`: the id string of the data source backend.
  * `options`: an array of the parameters to set:
    * `name`: the unique name of the parameter.
    * `displayName`: optional display name of the parameter for display in user interfaces.
    * `required`: required boolean indicating whether the parameter must have a non-null value.
    * `propertyType`: required data type of the parameter, one of:
      * `STRING`
      * `BOOLEAN`
      * `INTEGER`
      * `LONG`
      * `FLOAT`
      * `DOUBLE`
      * `CHARACTER`
      * `STRING_ARRAY`
      * `DOUBLE_ARRAY`
      * `FLOAT_ARRAY`
      * `INTEGER_ARRAY`
      * `LONG_ARRAY`
      * `BOOLEAN_ARRAY`
      * `CHARACTER_ARRAY`
    * `prompt`: required boolean indicating whether the user should be prompted for a value for this parameter.
    * `value`: the value of the parameter.
* `knowledgeSourceBackends`: an array representing sources of concepts:
  * `id`: the id string of the knowledge source backend.
  * `displayName`: optional display name of the knowledge source backend for display in user interfaces.
  * `options`: an array of parameters to set:
    * `name`: required unique name of the parameter.
    * `displayName`: optional display name of the parameter for display in user interfaces.
    * `required`: required boolean indicating whether the parameter must have a non-null value.
    * `propertyType`: required data type of the parameter, one of:
      * `STRING`
      * `BOOLEAN`
      * `INTEGER`
      * `LONG`
      * `FLOAT`
      * `DOUBLE`
      * `CHARACTER`
      * `STRING_ARRAY`
      * `DOUBLE_ARRAY`
      * `FLOAT_ARRAY`
      * `INTEGER_ARRAY`
      * `LONG_ARRAY`
      * `BOOLEAN_ARRAY`
      * `CHARACTER_ARRAY`
    * `prompt`: required boolean indicating whether the user should be prompted for a value for this parameter.
    * `value`: the value of the parameter.
* `algorithmSourceBackends`: array representing sources of value processing algorithms; currently read-only.
  * `id`: the id string of the algorithm source backend.
  * `displayName`: optional display name of the algorithm source backend for display in user interfaces.
  * `options`: an array of parameters to set:
    * `name`: required unique name of the parameter.
    * `displayName`: optional display name of the parameter for display in user interfaces.
    * `required`: required boolean indicating whether the parameter must have a non-null value.
    * `propertyType`: required data type of the parameter, one of:
      * `STRING`
      * `BOOLEAN`
      * `INTEGER`
      * `LONG`
      * `FLOAT`
      * `DOUBLE`
      * `CHARACTER`
      * `STRING_ARRAY`
      * `DOUBLE_ARRAY`
      * `FLOAT_ARRAY`
      * `INTEGER_ARRAY`
      * `LONG_ARRAY`
      * `BOOLEAN_ARRAY`
      * `CHARACTER_ARRAY`
    * `prompt`: required boolean indicating whether the user should be prompted for a value for this parameter.
    * `value`: the value of the parameter.
* `read`: required boolean indicating whether the current user can read this source config.
* `write`: required boolean indicating whether the current user can update this source config.
* `execute`: required boolean indicating whether the current user can read data and concepts from this source config.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET /api/protected/sourceconfig
Gets a list of the available source configurations.

##### GET /api/protected/sourceconfig/{id}
Returns the source configuration with the specified numerical unique id.

##### GET /api/protected/sourceconfig/parameters/list
Gets a list of the available source configurations' parameter names and values.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/sourceconfig/parameters/list

Returns:
```
[
  { "id":"Spreadsheet",
    "name":"Spreadsheet",
    "uploads":[
      { "name":"Eureka Spreadsheet Data Source Backend",
        "sourceId":null,
        "acceptedMimetypes":["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],
        "sampleUrl":"../docs/sample.xlsx",
        "required":true}
    ]
  }
]
```

##### GET /api/protected/sourceconfig/parameters/{id}
Returns the parameter names and values for the source configuration with the specified numerical unique id.

### `/api/protected/phenotypes`
Manages user-defined phenotypes.

#### Role-based authorization
Must have `researcher` role.

#### Requires successful authentication
Yes

#### Phenotype object
Phenotypes are computable descriptions of a type of patient. They all have the following properties:
* `id`: unique id number of the phenotype (set by the server on object creation, and required thereafter).
* `key`: required unique name of the phenotype.
* `userId`: required username of the owner of the phenotype.
* `description`: optional description of the phenotype.
* `displayName`: optional user-visible name for the phenotype.
* `inSystem`: required boolean indicating whether it is a concept provided by the system. For user-defined phenotypes, the value should be set to `false`.
* `created`: required timestamp, in milliseconds since the epoch, indicating when the phenotype was created (set by the server).
* `lastModified`: timestamp, in milliseconds since the epoch, indicating when the phenotype was last modified (set by the server).
* `summarized`: read-only boolean indicating whether the phenotype was retrieved with the `summarize` query parameter set to `true`.
* `type`: the type of phenotype, one of:
  * `SYSTEM`: a Concept provided by the system.
  * `CATEGORIZATION`: a category phenotype.
  * `SEQUENCE`: a sequence phenotype.
  * `FREQUENCY`: a frequency phenotype.
  * `VALUE_THRESHOLD`: a value threshold phenotype.
* `internalNode`: read-only boolean indicating whether the phenotype has any children.

#### PhenotypeField
Used in phenotype definitions for referring to dependent phenotypes.

Properties:
* `id`: numerical id of the phenotype (set by the server on phenotype creation), or `null` if a system concept.
* `phenotypeKey`: the unique key for the phenotype or concept.
* `phenotypeDescription`: the phenotype's description.
* `phenotypeDisplayName`: the phenotype's display name.
* `hasDuration`: field indicating whether to apply a duration constraint to the phenotype. Default is `false`.
* `minDuration`: the lower limit of the duration constraint.
* `minDurationUnits`: the units string for the lower limit of the duration constraint.
* `maxDuration`: the upper limit of the duration constraint.
* `maxDurationUnits`: the units string for the upper limit of the duration constraint.
* `hasPropertyConstraint`: required field indicating whether to apply a property constraint to the phenotype. Default is `false`.
* `property`: the name of the property.
* `propertyValue`: the value of the property as a string.
* `type`: the type of the phenotype, one of `CATEGORIZATION`, `SEQUENCE`, `FREQUENCY`, `VALUE_THRESHOLD`, `SYSTEM`.
* `categoricalType`: for category phenotypes, the categorical type, one of `CONSTANT`, `EVENT`, `PRIMITIVE_PARAMETER`, `LOW_LEVEL_ABSTRACTION`, `COMPOUND_LOW_LEVEL_ABSTRACTION`, `HIGH_LEVEL_ABSTRACTION`, `SLICE_ABSTRACTION`, `SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION`, `MIXED`, `UNKNOWN`.

#### SystemPhenotype object
Represents Concepts on which a phenotype depends. It is read-only.

Additional properties:
* `systemType`: required, one of `CONSTANT`, `EVENT`, `PRIMITIVE_PARAMETER`, `LOW_LEVEL_ABSTRACTION`, `COMPOUND_LOW_LEVEL_ABSTRACTION`, `HIGH_LEVEL_ABSTRACTION`, `SLICE_ABSTRACTION`, `SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION`, `CONTEXT`.
* `children`: an array of SystemPhenotype objects.
* `isParent`: whether this phenotype has any children.
* `properties`: array of property names.

#### CategoryPhenotype object
Represents category phenotypes.

Additional properties:
* `children`: an array of PhenotypeField objects.
* `categoricalType`: required, one of `CONSTANT`, `EVENT`, `PRIMITIVE_PARAMETER`, `LOW_LEVEL_ABSTRACTION`, `COMPOUND_LOW_LEVEL_ABSTRACTION`, `HIGH_LEVEL_ABSTRACTION`, `SLICE_ABSTRACTION`, `SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION`, `MIXED`, `UNKNOWN`.

#### Sequence object
Represents sequence phenotypes.

Additional properties:
* `primaryPhenotype`: required PhenotypeField object.
* `relatedPhenotypes`: array of related phenotypes and their temporal relationships to each other and the primary phenotype:
  * `phenotypeField`: the phenotype on the left-hand-side of the relation as a PhenotypeField object.
  * `relationOperator`: the numerical id of the relation operator.
  * `sequentialPhenotype`: the key of the phenotype or concept on the right-hand-side of the relation.
  * `sequentialPhenotypeSource`: 
  * `relationMinCount`: the lower time limit of the temporal relation.
  * `relationMinUnits`: numerical id of the time units of the lower time limit.
  * `relationMaxCount`: the upper time limit of the temporal relation.
  * `relationMaxUnits`: numerical id of the time units of the upper time limit.

#### Frequency object
Represents frequency phenotypes.

Additional properties:
* `atLeast`: required minimum count.
* `isConsecutive`: required boolean indicating whether only consecutive values should be considered (only can be `true` for observations with values).
* `phenotype`: required PhenotypeField object representing the phenotype or concept for which you want to threshold its frequency.
* `isWithin`: required boolean indicating whether to apply a time amount between matching phenotypes or concepts.
* `withinAtLeast`: optional lower limit of a time amount between matching phenotypes or concepts.
* `withinAtLeastUnits`: optional time units for the lower limit.
* `withinAtMost`: optional upper limit of a time amount between matching phenotypes or concepts.
* `withinAtMostUnits`: optional time units for the upper limit.
* `frequencyType`: id value of the frequency type to apply.

#### ValueThresholds object
Represents value threshold phenotypes.

Additional properties:
* `name`: name of the value threshold.
* `thresholdsOperator`: id value of the thresholds operator to apply.
* `valueThresholds`: an array of ValueThreshold objects:
  * `phenotype`: the valued phenotype or concept to threshold as a PhenotypeField
  * `lowerComp`: id value of the comparator of the lower limit of the threshold.
  * `lowerValue`: the value of the lower limit of the threshold.
  * `lowerUnits`: value units string for the lower limit.
  * `upperComp`: id value of the comparator of the upper limit of the threshold.
  * `upperValue`: value of the upper limit of the threshold.
  * `upperUnits`: value units string for the upper limit.
  * `relationOperator`: id value of the relation operator to apply for any context phenotypes or concepts.
  * `relatedPhenotypes`: an array of any context phenotypes or concepts as PhenotypeFields.
  * `withinAtLeast`: optional lower limit of a time amount between matching phenotypes or concepts.
  * `withinAtLeastUnits`: optional time units for the lower limit.
  * `withinAtMost`: optional upper limit of a time amount between matching phenotypes or concepts.
  * `withinAtMostUnits`: optional time units for the upper limit.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/phenotypes[?summarize=yes|no]`
Returns the concepts accessible by the current user. Optionally, return each concept in a summarized form suitable for listing.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/phenotypes?summarize=yes

Return:
```
[
  { "type":"CATEGORIZATION",
    "id":2,
    "key":"USER:testCategorization",
    "userId":1,
    "description":"test",
    "displayName":"testCategorization",
    "inSystem":false,
    "created":1484772736590,
    "lastModified":1484772736590,
    "summarized":false,
    "internalNode":false,
    "children": [
      { "id":null,
        "phenotypeKey":"Patient",
        "phenotypeDescription":"",
        "phenotypeDisplayName":"Patient",
        "hasDuration":null,
        "minDuration":null,
        "minDurationUnits":null,
        "maxDuration":null,
        "maxDurationUnits":null,
        "hasPropertyConstraint":null,
        "property":null,
        "propertyValue":null,
        "type":"SYSTEM",
        "categoricalType":null,
        "inSystem":true},
      { "id":null,
        "phenotypeKey":"PatientDetails",
        "phenotypeDescription":"",
        "phenotypeDisplayName":"Patient Details",
        "hasDuration":null,
        "minDuration":null,
        "minDurationUnits":null,
        "maxDuration":null,
        "maxDurationUnits":null,
        "hasPropertyConstraint":null,
        "property":null,
        "propertyValue":null,
        "type":"SYSTEM",
        "categoricalType":null,
        "inSystem":true}
    ],
    "categoricalType":"CONSTANT"}
]
```

##### GET /api/protected/phenotypes/{key}[?summarize=yes|no]
Returns the concept with the specified key. Optionally, return each concept in a summarized form suitable for listing.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/phenotypes/USER:testCategorization?summarize=yes

Returns:
```
{ "type":"CATEGORIZATION",
  "id":2,
  "key":"USER:testCategorization",
  "userId":1,
  "description":"test",
  "displayName":"testCategorization",
  "inSystem":false,
  "created":1484772736590,
  "lastModified":1484772736590,
  "summarized":false,
  "internalNode":false,
  "children": [
    { "id":null,
      "phenotypeKey":"Patient",
      "phenotypeDescription":"",
      "phenotypeDisplayName":"Patient",
      "hasDuration":null,
      "minDuration":null,
      "minDurationUnits":null,
      "maxDuration":null,
      "maxDurationUnits":null,
      "hasPropertyConstraint":null,
      "property":null,
      "propertyValue":null,
      "type":"SYSTEM",
      "categoricalType":null,
      "inSystem":true},
    { "id":null,
      "phenotypeKey":"PatientDetails",
      "phenotypeDescription":"",
      "phenotypeDisplayName":"Patient Details",
      "hasDuration":null,
      "minDuration":null,
      "minDurationUnits":null,
      "maxDuration":null,
      "maxDurationUnits":null,
      "hasPropertyConstraint":null,
      "property":null,
      "propertyValue":null,
      "type":"SYSTEM",
      "categoricalType":null,
      "inSystem":true}
  ],
  "categoricalType":"CONSTANT"}
```

##### POST `/api/protected/phenotypes`
Stores a new phenotype.

##### PUT `/api/protected/phenotypes`
Saves an existing phenotype.

##### DELETE `/api/protected/phenotypes/{userId}/{key}`
Deletes the specified phenotype.

### `/api/protected/concepts`
System concepts provided by the system.

#### Role-based authorization
Must have `researcher` role.

#### Requires successful authentication
Yes

#### Concept object
Representation of concepts provided by the system. They are read-only.

Properties:
* `id`: always `null`.
* `key`: required unique name of the concept.
* `userId`: always `null`.
* `description`: optional description of the concept.
* `displayName`: optional user-visible name for the concept.
* `inSystem`: required boolean indicating whether it is a concept provided by the system. Always `true`.
* `created`: required timestamp, in milliseconds since the epoch, indicating when the concept was created (set by the server).
* `lastModified`: timestamp, in milliseconds since the epoch, indicating when the concept was last modified (set by the server).
* `summarized`: read-only boolean indicating whether the concept was retrieved with the `summarize` query parameter set to `true`.
* `type`: the type of concept, always `SYSTEM`.
* `internalNode`: read-only boolean indicating whether the concept has any children.
* `systemType`: required, one of `CONSTANT`, `EVENT`, `PRIMITIVE_PARAMETER`, `LOW_LEVEL_ABSTRACTION`, `COMPOUND_LOW_LEVEL_ABSTRACTION`, `HIGH_LEVEL_ABSTRACTION`, `SLICE_ABSTRACTION`, `SEQUENTIAL_TEMPORAL_PATTERN_ABSTRACTION`, `CONTEXT`.
* `children`: an array of Concept objects.
* `isParent`: whether this concept has any children.
* `properties`: array of property names.

#### Calls
Uses status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification).

##### GET `/api/protected/concepts[?summarize=true]`
Returns the top-level system concepts accessible by the current user. Optionally, return each concept in a summarized form suitable for listing.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/concepts

Returns: 
```
[
  { "type":"SYSTEM",
    "id":null,
    "key":"Patient",
    "userId":null,
    "description":"",
    "displayName":"Patient",
    "inSystem":true,
    "created":null,
    "lastModified":null,
    "summarized":true,
    "internalNode":false,
    "systemType":"CONSTANT",
    "children":null,
    "properties":["patientId"],
    "parent":false},
  { "type":"SYSTEM",
    "id":null,
    "key":"PatientDetails",
    "userId":null,
    "description":"",
    "displayName":"Patient Details",
    "inSystem":true,
    "created":null,
    "lastModified":null,
    "summarized":true,
    "internalNode":false,
    "systemType":"CONSTANT",
    "children":null,
    "properties": [
      "ageInYears",
      "dateOfBirth",
      "dateOfDeath",
      "gender",
      "language",
      "maritalStatus",
      "race",
      "vitalStatus",
      "patientId"
    ],
    "parent":false},
  { "type":"SYSTEM",
    "id":null,
    "key":"Encounter",
    "userId":null,
    "description":"",
    "displayName":"Encounter",
    "inSystem":true,
    "created":null,
    "lastModified":null,
    "summarized":true,
    "internalNode":false,
    "systemType":"EVENT",
    "children":null,
    "properties": ["age","type","encounterId"],
    "parent":false},
  { "type":"SYSTEM",
    "id":null,
    "key":"VitalSign",
    "userId":null,
    "description":"",
    "displayName":"Vital Sign",
    "inSystem":true,
    "created":null,
    "lastModified":null,
    "summarized":true,
    "internalNode":true,
    "systemType":"PRIMITIVE_PARAMETER",
    "children":null,
    "properties":[],
    "parent":true}
]
```

##### POST `/api/protected/concepts[?summarize=true]`
Retrieves the concepts enumerated in the form body. Optionally, returns each concept in a summarized form suitable for listing.

Form parameters:
* key: The keys of the system concepts of interest (optional). If omitted, the empty list is returned.
* summarize: yes or no if you want returned concepts in a summarized form suitable for listing (optional).

##### GET `/api/protected/concepts/{key}`
Gets the requested system concept with the specified key or the 404 (NOT FOUND) status code if no such system concept exists and is accessible to the current user. 

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/concepts/Patient

Returns:
```
{ "type":"SYSTEM",
  "id":null,
  "key":"Patient",
  "userId":null,
  "description":"",
  "displayName":"Patient",
  "inSystem":true,
  "created":null,
  "lastModified":null,
  "summarized":false,
  "internalNode":false,
  "systemType":"CONSTANT",
  "children":[],
  "properties":["patientId"],
  "parent":false }
```

##### GET `/api/protected/concepts/propsearch/{searchKey}`
Gets the concepts with the specified text in their display name, case insensitive.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/concepts/propsearch/ICD9%20Procedure%20Codes

Returns: 
```
[
  { "type":"SYSTEM",
    "id":null,
    "key":"ICD9:Procedures",
    "userId":null,
    "description":"",
    "displayName":"ICD9 Procedure Codes",
    "inSystem":true,
    "created":null,
    "lastModified":null,
    "summarized":true,
    "internalNode":true,
    "systemType":"EVENT",
    "children":null,
    "properties":[],
    "parent":true}
]
```

##### GET `/api/protected/concepts/search/{searchKey}`
Gets an array of the keys of the system concepts with the specified text in their display name, case insensitive.

###### Example:
URL: https://localhost:8443/eureka-services/api/protected/concepts/search/ICD10:Diagnoses

Returns:
```
["ICD10:Diagnoses", "ICD10:S00-T88", "ICD10:S00-S09", ...]
```

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

