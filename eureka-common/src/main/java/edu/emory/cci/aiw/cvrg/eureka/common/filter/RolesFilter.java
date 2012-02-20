package edu.emory.cci.aiw.cvrg.eureka.common.filter;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RolesFilter.class);
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
		Set<String> roles = new HashSet<String>();
		if (principal != null) {
			String name = principal.getName();
			try {
				Connection connection = this.dataSource.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(this.sql);
				preparedStatement.setString(1, name);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					String role = resultSet.getString(this.colName);
					LOGGER.debug("Assigning role {}", role);
					roles.add(role);
				}
				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
				throw new ServletException(e);
			}
		}
		HttpServletRequest wrappedRequest = new RolesRequestWrapper(
				servletRequest, principal, roles);
		inChain.doFilter(wrappedRequest, inResponse);
	}

	@Override
	public void destroy() {
		this.dataSource = null;
		this.colName = null;
		this.sql = null;
	}

}
