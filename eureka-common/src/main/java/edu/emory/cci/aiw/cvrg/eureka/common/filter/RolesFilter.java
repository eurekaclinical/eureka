/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.filter;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A filter to fetch roles from a database, and assign them to the current
 * principal.
 *
 * @author hrathod
 *
 */
public class RolesFilter implements Filter {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RolesFilter.class);
	/**
	 * The datasource used to fetch the roles data.
	 */
	private DataSource dataSource;
	/**
	 * The SQL to run to fetch the roles data.
	 */
	private String sql;
	/**
	 * The column name in the result set that contains the role name.
	 */
	private String colName;

	@Override
	public void init(FilterConfig inFilterConfig) throws ServletException {
		this.sql = inFilterConfig.getInitParameter("sql");
		LOGGER.debug("Got SQL {}", this.sql);
		this.colName = inFilterConfig.getInitParameter("rolecolumn");
		LOGGER.debug("Got column name {}", this.colName);

		String sourceName = inFilterConfig.getInitParameter("datasource");
		LOGGER.debug("Using datasource {}", sourceName);
		try {
			Context context = new InitialContext();
			this.dataSource = (DataSource) context.lookup(sourceName);
		} catch (NamingException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest inRequest, ServletResponse inResponse,
			FilterChain inChain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) inRequest;
		Principal principal = servletRequest.getUserPrincipal();
		if (principal != null) {
			Set<String> roles = new HashSet<String>();
			String name = principal.getName();
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				connection = this.dataSource.getConnection();
				preparedStatement = connection.prepareStatement(this.sql);
				preparedStatement.setString(1, name);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					String role = resultSet.getString(this.colName);
					if (role != null) {
						String authority = "ROLE_" + role.toUpperCase();
						LOGGER.debug("Assigning role {}", authority);
						roles.add(authority);
					}
				}
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
				throw new ServletException(e);
			} finally {
				this.close(resultSet, preparedStatement, connection);
			}
			HttpServletRequest wrappedRequest = new RolesRequestWrapper(
					servletRequest, principal, roles);
			inChain.doFilter(wrappedRequest, inResponse);
		} else {
			inChain.doFilter(inRequest, inResponse);
		}
	}

	/**
	 * Properly close the give ResultSet, Statement, and Connection.
	 *
	 * @param resultSet The result set to dispose of.
	 * @param statement The statement to dispose of.
	 * @param connection The connection to dispose of.
	 */
	private void close(ResultSet resultSet, Statement statement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException sqle) {
				LOGGER.error(sqle.getMessage(), sqle);
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqle) {
				LOGGER.error(sqle.getMessage(), sqle);
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqle) {
				LOGGER.error(sqle.getMessage(), sqle);
			}
		}
	}

	@Override
	public void destroy() {
		this.dataSource = null;
		this.colName = null;
		this.sql = null;
	}
}
