package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class JobPollServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		resp.setContentType("application/text");

		try {
			Client client = CommUtils.getClient();
			WebResource webResource = client.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/byname/super.user@emory.edu")
					.accept(MediaType.APPLICATION_JSON).get(User.class);

			JobInfo jobInfo = webResource
					.path("/api/job/status/" + user.getId())
					.accept(MediaType.APPLICATION_JSON).get(JobInfo.class);

			String message = "status: " + jobInfo.getCurrentStep();
			System.out.println(message);
			resp.setContentLength(message.length());
			PrintWriter out = resp.getWriter();
			out.println(message);
			out.close();
			out.flush();

		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);
		}

	}
}
