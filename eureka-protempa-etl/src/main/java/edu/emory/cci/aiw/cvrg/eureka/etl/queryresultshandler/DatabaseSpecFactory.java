package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import edu.emory.cci.aiw.i2b2etl.dest.config.DataSourceDatabaseSpec;
import edu.emory.cci.aiw.i2b2etl.dest.config.DatabaseSpec;
import edu.emory.cci.aiw.i2b2etl.dest.config.DriverManagerDatabaseSpec;

/**
 * Factory for creating {@link DatabaseSpec}s. This is specific to Eureka
 * because Eureka requires either a JDBC URL database connection string, in
 * which case it uses the {@link java.sql.DriverManager} API to connect to 
 * the database; or a JNDI database URI, in which case it uses a
 * database connection configured in the servlet container.
 * 
 * @author Andrew Post
 */
class DatabaseSpecFactory {
	/**
	 * Creates a new {@link DatabaseSpec} with the provided connect string, 
	 * username and password.
	 * @param connect Either a JDBC URL or a JNDI database URI. 
	 * Cannot be <code>null</code>.
	 * @param user A valid database username.
	 * @param passwd The password for the provided username.
	 * @return a {@link DatabaseSpec} implementation appropriate for the 
	 * provided connect JDBC URL or JNDI database URI connect string.
	 */
	DatabaseSpec getInstance(String connect, String user, String passwd) {
		if (connect == null) {
			throw new IllegalArgumentException("connect cannot be null");
		}
		if (connect.startsWith("jdbc:")) {
			return new DriverManagerDatabaseSpec(connect, user, passwd);
		} else if (connect.startsWith("java:")) {
			return new DataSourceDatabaseSpec(connect, user, passwd);
		} else {
			throw new IllegalArgumentException("Invalid connect string: must be a JDBC URL or a JNDI data source URI");
		}
	}
}
