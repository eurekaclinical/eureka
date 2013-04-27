---
-- #%L
-- Eureka Protempa ETL
-- %%
-- Copyright (C) 2012 - 2013 Emory University
-- %%
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- #L%
---

--create user  cvrg identified by cvrg
--default tablespace users
--QUOTA 10000M ON users
--temporary tablespace temp;
--
--grant create session to cvrg;
--grant create table to  cvrg;
--grant create view to  cvrg;
--grant create any index to  cvrg;
--GRANT CREATE PUBLIC SYNONYM TO  cvrg;
--GRANT DROP PUBLIC SYNONYM TO  cvrg;
--GRANT CREATE SEQUENCE TO  cvrg;
--GRANT CREATE PROCEDURE TO  cvrg;
--grant CREATE ANY TYPE TO cvrg;
--grant ALTER ANY TYPE TO cvrg;
--grant DROP ANY TYPE TO cvrg;
--grant EXECUTE ANY TYPE TO cvrg;
--grant UNDER ANY TYPE TO cvrg;
--grant CREATE ANY TRIGGER to  cvrg;
--grant ALTER ANY TRIGGER to  cvrg;
--grant DROP ANY TRIGGER to  cvrg;


CREATE SCHEMA IF NOT EXISTS "EUREKA";

SET SCHEMA "EUREKA";

CREATE TABLE IF NOT EXISTS "PATIENT" (

"PATIENT_KEY"    NUMBER(22,0) NOT NULL ,
"FIRST_NAME"     VARCHAR2(32) ,
"LAST_NAME"      VARCHAR2(32) ,
"DOB"            DATE         ,
"LANGUAGE"       VARCHAR2(32) ,
"MARITAL_STATUS" VARCHAR2(32) ,
"RACE"           VARCHAR2(32) ,
"GENDER"         VARCHAR2(16) ,

CONSTRAINT patient_pk PRIMARY KEY (patient_key)
);
--tablespace users
--nologging;



CREATE TABLE IF NOT EXISTS "PROVIDER" (

"PROVIDER_KEY"  NUMBER(22,0) NOT NULL ,
"FIRST_NAME"    VARCHAR2(32) ,
"LAST_NAME"     VARCHAR2(32) ,

CONSTRAINT provider_pk PRIMARY KEY (provider_key)
);
--tablespace users
--nologging;


CREATE TABLE IF NOT EXISTS "ENCOUNTER" (

"ENCOUNTER_KEY"   NUMBER(22,0) NOT NULL ,
"PATIENT_KEY"     NUMBER(22,0) NOT NULL ,
"PROVIDER_KEY"    NUMBER(22,0) NOT NULL ,
"TS_START"        TIMESTAMP(4) ,
"TS_END"          TIMESTAMP(4) ,
"ENCOUNTER_TYPE"  VARCHAR2(64) ,
"DISCHARGE_DISP"  VARCHAR2(64) ,
 
CONSTRAINT encounter_pk PRIMARY KEY (encounter_key)
);
--tablespace users
--nologging;
 
 
 
 
 
CREATE TABLE IF NOT EXISTS "CPT_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
 
CONSTRAINT cpt_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
 
 
 
CREATE TABLE IF NOT EXISTS "ICD9D_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
 
CONSTRAINT icd9d_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
 
 
 
CREATE TABLE IF NOT EXISTS "ICD9P_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
 
CONSTRAINT icd9p_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
 
 
 
 
CREATE TABLE IF NOT EXISTS "MEDS_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
 
CONSTRAINT meds_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
 
 
 
 
CREATE TABLE IF NOT EXISTS "LABS_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
"RESULT_STR"    VARCHAR2(32) ,
"RESULT_NUM"    NUMBER(18,4) ,
"UNITS"         VARCHAR2(16) ,
"FLAG"          VARCHAR2(8) ,
 
CONSTRAINT labs_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
 
 
 
 
CREATE TABLE IF NOT EXISTS "VITALS_EVENT" (
 
"EVENT_KEY"     VARCHAR2(32) NOT NULL ,
"ENCOUNTER_KEY" NUMBER(22,0) NOT NULL ,
"TS_OBX"        TIMESTAMP(4) ,
"ENTITY_ID"     VARCHAR2(128) NOT NULL ,
"RESULT_STR"    VARCHAR2(32) ,
"RESULT_NUM"    NUMBER(18,4) ,
"UNITS"         VARCHAR2(16) ,
"FLAG"          VARCHAR2(8) ,
 
CONSTRAINT vitals_event_pk PRIMARY KEY (event_key)
);
--tablespace users
--nologging;
