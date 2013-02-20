---
-- #%L
-- Eureka Services
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
-- Creates the role list and the superuser account, if they do not exist. The
-- superuser's default password is 'defaultpassword'. Please change this
-- immediately after installing Eureka! There's a Java class file,
-- edu.emory.cci.aiw.cvrg.eureka.services.util.SuperUserDefaultPasswordGenerator,
-- which is for generating the hash values for the superuser default password,
-- should there be a need to change it.
insert into roles values (1, 1, 'researcher');
insert into roles values (2, 0, 'admin');
insert into roles values (3, 0, 'superuser');
insert into users values (1, 1, 'super.user@emory.edu', 'Super', null, 'User', 'N/A', '4a94e453e6ee6a8253def63db4d159', null, 1);
insert into user_role values (1, 1);
insert into user_role values (2, 1);
insert into user_role values (3, 1);
