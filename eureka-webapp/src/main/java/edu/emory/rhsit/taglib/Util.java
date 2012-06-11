package edu.emory.rhsit.taglib;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

public class Util {


	public static boolean contains(Collection<?> coll, Object o) {

		return coll.contains(o);

	}

	public static boolean isUserInRole(HttpServletRequest request, String role) {
		return request.isUserInRole(role);
	}
}
