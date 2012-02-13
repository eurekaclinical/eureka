package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class ListUsersWorker implements ServletWorker {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		try {
			Client client = CommUtils.getClient();
			WebResource webResource = client.resource(eurekaServicesUrl);
			List<User> users = webResource.path("/api/user/list")
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<User>>() {
						// Nothing to implement, used to hold returned data.
					});

			// Set sort order to show the inactive users first.
			Collections.sort(users, new Comparator<User>() {
				public int compare(User user1, User user2) {
					int u1 = 0;
					int u2 = 0;
					if (user1.isActive())
						u1 = 1;
					if (user2.isActive())
						u2 = 1;

					return u1 - u2;
				}
			});
			req.setAttribute("users", users);
			req.getRequestDispatcher("/protected/admin.jsp").forward(req, resp);
		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);
		}
	}
}
