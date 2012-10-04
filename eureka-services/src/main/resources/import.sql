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
