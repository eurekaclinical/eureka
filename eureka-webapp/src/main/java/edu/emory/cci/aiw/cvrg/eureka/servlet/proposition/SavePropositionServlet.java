package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
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
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper.Type;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class SavePropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SavePropositionServlet.class);


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		LOGGER.debug("SavePropositionServlet");
		String id = req.getParameter("proposition");
		String type = req.getParameter("type");
		String name = req.getParameter("name");
		
		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		
		ObjectMapper mapper = new ObjectMapper();
		List<UserProposition> props = null;
		try {
			props = mapper.readValue(id,
					new TypeReference<List<UserProposition>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {

			
			
			PropositionWrapper pw = new PropositionWrapper();
	
			pw.setAbbrevDisplayName(name);
			if (type.equals("AND")) {
				pw.setType(Type.AND);
			} else {
				pw.setType(Type.OR);
				
			}
	
			List<String> systemTargets = new ArrayList<String>();
			List<Long> userTargets = new ArrayList<Long>();
			pw.setInSystem(false);
			
			for (UserProposition userProposition : props) {
				System.out.println(userProposition.getId());
				LOGGER.debug(userProposition.getId());
				
				if (userProposition.getType().equals("systemTree")) {
					//pw.setInSystem(true);
					systemTargets.add(userProposition.getId());			
				} else {
					userTargets.add(Long.valueOf(userProposition.getId()));
				}
				
			}
			pw.setSystemTargets(systemTargets);
			pw.setUserTargets(userTargets);
		
			Client client = CommUtils.getClient();
//			Principal principal = req.getUserPrincipal();
//			String userName = principal.getName();
					
			
			//LOGGER.debug("got username {}", userName);
			WebResource webResource = client.resource(eurekaServicesUrl);
//			User user = webResource.path("/api/proposition")
//					.accept(MediaType.APPLICATION_JSON).get(User.class);

			pw.setUserId(1L);
			
			
			webResource.path("/api/proposition/user/create")
				.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.TEXT_PLAIN)
						.post(ClientResponse.class, pw);

			
			List<Proposition> propositions = webResource.path("/api/proposition/user/list/1" )
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Proposition>>() {
						// Nothing to implement, used to hold returned data.
					});
			System.out.println(propositions.size());
			for (Proposition proposition : propositions) {
				System.out.println("got user prop: " + proposition.getId());
			}
			
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.debug(id);
		System.out.println(id);

	}
}
