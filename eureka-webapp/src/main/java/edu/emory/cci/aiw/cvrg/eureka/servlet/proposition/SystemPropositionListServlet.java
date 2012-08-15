package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;


public class SystemPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemPropositionListServlet.class);

	

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
			Principal principal = req.getUserPrincipal();
			String userName = principal.getName();
					
			LOGGER.debug("got username {}", userName);
			WebResource webResource = client.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/byname/" + userName)
					.accept(MediaType.APPLICATION_JSON).get(User.class);
			
			List<PropositionWrapper> props = webResource.path("/api/proposition/system/" + user.getId() + "/list")
					.get(new GenericType<List<PropositionWrapper>>() {
						// Nothing to implement, used to hold returned data.
					});
			for (PropositionWrapper proposition : props) {
				JsonTreeData d = createData(proposition.getKey(), proposition.getKey());
				l.add(d);
			}
			LOGGER.debug("executed resource get");
			for (PropositionWrapper p : props) {
				LOGGER.debug("id = {}, name = {}", p.getId(), p.getDisplayName());
			}
		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);
		}		
		
			
		/*
		String id = req.getParameter("id");
		
		if (id.equals("root")) {
			
			Data d1 = createData("Child 1","199.1");
			Data d2 = createData("Child 2", "200.1");
			Data d3 = createData("Sub Child 1", "300");
			Data d4 = createData("Sub Child 2", "400");
			List<Data> l = new ArrayList<Data>();
			d2.addNodes(d3, d4);
			l.add(d1); l.add(d2);
			ObjectMapper mapper = new ObjectMapper();
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, l);
		} else {
			Data d1 = createData("Lazy Loaded Child","11");
			List<Data> l = new ArrayList<Data>();
			l.add(d1); 
			ObjectMapper mapper = new ObjectMapper();
			
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, l);
			
		}
		*/
		
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
