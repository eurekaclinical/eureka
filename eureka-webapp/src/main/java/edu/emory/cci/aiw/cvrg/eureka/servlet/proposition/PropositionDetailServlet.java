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
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;


public class PropositionDetailServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionDetailServlet.class);

	private WebResource webResource;


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
	
	private void getAllSystemData(JsonTreeData d) {
		
		PropositionWrapper propWrapper = webResource.path("/api/proposition/system/"+ d.getId())
				.accept(MediaType.APPLICATION_JSON)
				.get(PropositionWrapper.class);
		
		for (String sysTarget : propWrapper.getSystemTargets()) {
			JsonTreeData newData = createData(sysTarget, sysTarget);
			newData.setType("system");
			d.addNodes(newData);
			getAllSystemData(newData);
		}		
		
	}

	private void getAllData(JsonTreeData d) {
		
		PropositionWrapper propWrapper = webResource.path("/api/proposition/user/get/"+ d.getId())
				.accept(MediaType.APPLICATION_JSON)
				.get(PropositionWrapper.class);
		
		for (String sysTarget : propWrapper.getSystemTargets()) {
			JsonTreeData newData = createData(sysTarget, sysTarget);
			newData.setType("system");
			d.addNodes(newData);
		}		
		
		for (Long userTarget : propWrapper.getUserTargets()) {
			PropositionWrapper propUserWrapper = 
					webResource.path("/api/proposition/user/get/"+ userTarget)
					.accept(MediaType.APPLICATION_JSON)
					.get(PropositionWrapper.class);
			
			JsonTreeData newData = createData(propUserWrapper.getAbbrevDisplayName(), propUserWrapper.getId());
			getAllData(newData);
			newData.setType("user");
			d.addNodes(newData);
			
		}
		
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<JsonTreeData> l = new ArrayList<JsonTreeData>();
		String propId = req.getParameter("propId");
	

		PropositionWrapper propWrapper = webResource.path("/api/proposition/user/get/"+ propId)
				.accept(MediaType.APPLICATION_JSON)
				.get(PropositionWrapper.class);
		
		JsonTreeData d = createData(propWrapper.getAbbrevDisplayName(), propWrapper.getId());
		d.setType("user");
		getAllData(d);
		
		l.add(d);
		
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
