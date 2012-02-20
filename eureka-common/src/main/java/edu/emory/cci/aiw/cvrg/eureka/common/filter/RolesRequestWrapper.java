package edu.emory.cci.aiw.cvrg.eureka.common.filter;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wraps an HttpServletRequest object, so that roles for the principal can be
 * implemented.
 * 
 * @author hrathod
 * 
 */
public class RolesRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * The original principal.
	 */
	private final Principal principal;
	/**
	 * The roles assigned to the principal;
	 */
	private final Set<String> roles;
	/**
	 * The original request.
	 */
	private final HttpServletRequest request;

	/**
	 * Create a wrapper with the given principal, role assigned to that
	 * principal, and the original request.
	 * 
	 * @param inRequest The original request.
	 * @param inPrincipal The request principal.
	 * @param inRoles The roles assigned to the principal.
	 */
	public RolesRequestWrapper(HttpServletRequest inRequest,
			Principal inPrincipal, Set<String> inRoles) {
		super(inRequest);
		this.request = inRequest;
		this.principal = inPrincipal;
		this.roles = inRoles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServletRequestWrapper#isUserInRole(java.lang.String
	 * )
	 */
	@Override
	public boolean isUserInRole(String inRole) {
		boolean result;
		if (this.roles == null) {
			result = this.request.isUserInRole(inRole);
		} else {
			result = this.roles.contains(inRole);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequestWrapper#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		Principal result;
		if (this.principal == null) {
			result = this.request.getUserPrincipal();
		} else {
			result = this.principal;
		}
		return result;
	}

}
