package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;


public class UserPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserPropositionListServlet.class);

	

	private JsonTreeData createData(String data, String id) {
		JsonTreeData d = new JsonTreeData();
		d.setData(data);
		d.setKeyVal("id", id);
		
		return d;
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		LOGGER.debug("doGet");
		List<JsonTreeData> l = new ArrayList<JsonTreeData>();
		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		try {
			Client client = CommUtils.getClient();
			//Principal principal = req.getUserPrincipal();
			//String userName = principal.getName();
					
			//LOGGER.debug("got username {}", userName);
			WebResource webResource = client.resource(eurekaServicesUrl);
//			User user = webResource.path("/api/proposition")
//					.accept(MediaType.APPLICATION_JSON).get(User.class);
			List<Proposition> props = webResource.path("/api/proposition/user/list/1")// + user.getId())
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Proposition>>() {
						// Nothing to implement, used to hold returned data.
					});
			for (Proposition proposition : props) {
				JsonTreeData d = createData(proposition.getAbbrevDisplayName(), String.valueOf(proposition.getId()));
				l.add(d);
				System.out.println("Added user prop: " + d.getData());
			}
			LOGGER.debug("executed resource get");
			for (Proposition p : props) {
				LOGGER.debug("id = {}, name = {}", p.getId(), p.getDisplayName());
			}
		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);
		}		
		
			

		
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
