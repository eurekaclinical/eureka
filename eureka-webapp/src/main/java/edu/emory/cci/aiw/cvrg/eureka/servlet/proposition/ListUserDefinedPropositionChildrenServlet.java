package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;


public class ListUserDefinedPropositionChildrenServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ListUserDefinedPropositionChildrenServlet.class);

	private WebResource webResource;

    private String getDisplayName(PropositionWrapper p) {
        String displayName = "";

        if (p.getAbbrevDisplayName() != null && !p.getAbbrevDisplayName().equals("")) {

            displayName = p.getAbbrevDisplayName(); 

        } else if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {

            displayName = p.getDisplayName();

        } else {

            displayName = p.getKey();

        }

        return displayName;
    }


	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		String eurekaServicesUrl = config.getServletContext()
				.getInitParameter("eureka-services-url");

		Client client;
		try {

			client = CommUtils.getClient();
			this.webResource = client.resource(eurekaServicesUrl);

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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



	private void getAllData(JsonTreeData d) {



		PropositionWrapper propWrapper = webResource.path("/api/proposition/user/get/"+ d.getId())
				.accept(MediaType.APPLICATION_JSON)
				.get(PropositionWrapper.class);
		LOGGER.debug("got propWrapper {}", propWrapper.getId());
		for (String sysTarget : propWrapper.getSystemTargets()) {
			JsonTreeData newData = createData(sysTarget, sysTarget);
			newData.setType("system");
			LOGGER.debug("add sysTarget {}", sysTarget);
			d.addNodes(newData);
		}

		for (Long userTarget : propWrapper.getUserTargets()) {
			PropositionWrapper propUserWrapper =
					webResource.path("/api/proposition/user/get/"+ userTarget)
					.accept(MediaType.APPLICATION_JSON)
					.get(PropositionWrapper.class);

			JsonTreeData newData = createData(propUserWrapper
				.getAbbrevDisplayName(), String.valueOf(propUserWrapper.getId
				().longValue()));
			getAllData(newData);
			newData.setType("user");
			LOGGER.debug("add user defined {}", propUserWrapper.getId());
			d.addNodes(newData);

		}

	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		Client client = null;
		try {
			client = CommUtils.getClient();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<JsonTreeData> l = new ArrayList<JsonTreeData>();
		String propId = req.getParameter("propId");

		//"/system/{userId}/{propKey}"
//		PropositionWrapper propWrapper =
//				webResource.path("/api/proposition/system/"+ user.getId() + "/" + propId)
//				.accept(MediaType.APPLICATION_JSON)
//				.get(PropositionWrapper.class);

		PropositionWrapper propWrapper = webResource.path("/api/proposition/user/get/"+ propId)
				.accept(MediaType.APPLICATION_JSON)
				.get(PropositionWrapper.class);

		JsonTreeData newData = createData(this.getDisplayName(propWrapper), propId);
		getAllData(newData);
		l.add(newData);


		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
