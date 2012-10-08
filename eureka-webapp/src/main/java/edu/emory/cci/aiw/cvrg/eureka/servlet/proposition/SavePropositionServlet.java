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
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper.Type;
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
		String id = req.getParameter("id");
		String propositions = req.getParameter("proposition");
		String type = req.getParameter("type");
		String name = req.getParameter("name");
		String description = req.getParameter("description");

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		ObjectMapper mapper = new ObjectMapper();
		List<UserProposition> props = null;
		try {
			props = mapper.readValue(propositions,
					new TypeReference<List<UserProposition>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {


			Client client = CommUtils.getClient();
			Principal principal = req.getUserPrincipal();
			String userName = principal.getName();

			WebResource webResource = client.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/byname/" + userName)
					.accept(MediaType.APPLICATION_JSON).get(User.class);


			PropositionWrapper pw = new PropositionWrapper();

			if (id != null && !id.equals("")) {
				pw.setId(Long.valueOf(id));
			}

			pw.setAbbrevDisplayName(name);
			if (type.equals("AND")) {
				pw.setType(Type.AND);
			} else {
				pw.setType(Type.OR);

			}

			List<PropositionWrapper> children =
                    new ArrayList<PropositionWrapper>(props.size());

			pw.setInSystem(false);

			for (UserProposition userProposition : props) {
				System.out.println(userProposition.getId());
				LOGGER.debug(userProposition.getId());

				PropositionWrapper child = new PropositionWrapper();
				child.setSummarized(true);
				if (userProposition.getType().equals("system")) {
					child.setInSystem(true);
					child.setKey(userProposition.getId());
				} else {
					child.setId(Long.valueOf(userProposition.getId()));
				}
                children.add(child);

			}


			pw.setChildren(children);
			pw.setAbbrevDisplayName(name);
			pw.setDisplayName(description);
			pw.setUserId(user.getId());

            ClientResponse response =
                        webResource.path("/api/proposition/user/validate/" + user.getId())
                            .type(MediaType.APPLICATION_JSON)
                                .post(ClientResponse.class, pw);

            int status = response.getClientResponseStatus().getStatusCode();
            if (status != HttpServletResponse.SC_OK) {

                String msg = response.getEntity(String.class);
                req.setAttribute("error", msg);
                LOGGER.debug("Error: {}", msg);
            }


            if (pw.getId() != null) {
			    webResource.path("/api/proposition/user/update")
			    	.type(MediaType.APPLICATION_JSON)
			    		.accept(MediaType.TEXT_PLAIN)
			    			.put(ClientResponse.class, pw);

            } else {
			    webResource.path("/api/proposition/user/create")
			    	.type(MediaType.APPLICATION_JSON)
			    		.accept(MediaType.TEXT_PLAIN)
			    			.post(ClientResponse.class, pw);
            }


		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
