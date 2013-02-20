/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.clients;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.AbstractClient;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class I2b2RestClient extends AbstractClient implements I2b2Client {

	private Configuration cfg;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(I2b2RestClient.class);
	private final ServiceProperties serviceProperties;

	@Inject
	public I2b2RestClient(ServiceProperties inProperties) {
		super();
		this.serviceProperties = inProperties;
	}

	@Override
	protected String getResourceUrl() {
		return this.serviceProperties.getI2b2URL();
	}

	@Override
	public void changePassword(String email, String passwd) throws HttpStatusException {

		try {
			setFMConfigs();
			Template tpl = cfg.getTemplate("changePassword.ftl");
			StringWriter writer = new StringWriter();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			String domain = serviceProperties.getI2b2Domain();
			String adminUsername = serviceProperties.getI2b2AdminUser();
			String adminPassword = serviceProperties.getI2b2AdminPassword();
			
			if (domain == null 
					|| adminUsername == null 
					|| adminPassword == null) {
				throw new IllegalStateException(
						"One or more of the following required application properties are missing: " +
						"i2b2 domain=" + domain + "; i2b2 admin username=" + 
						adminUsername + "; i2b2 admin password=" + 
						(adminPassword != null ? "*********" : null));
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", email);
			params.put("new_password", passwd);
			params.put("todayDate", dateFormat.format(date));
			params.put("domain", domain);
			params.put("admin_username", adminUsername);
			params.put("admin_password", adminPassword);

			tpl.process(params, writer);

			ClientResponse response = this.getResource().type(MediaType.TEXT_XML)
					.accept(MediaType.TEXT_XML)
					.entity(writer.toString())
					.post(ClientResponse.class);

			String xmloutput = response.getEntity(String.class);

			writer.close();
			int status = response.getClientResponseStatus().getStatusCode();


			if (status == Status.OK.getStatusCode()) {
				parseResponseXML(xmloutput);
			} else {
				LOGGER.error(xmloutput);
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"Error while changing password. Please contact the administrator. ");
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AssertionError(
					"IOException in i2b2ServicesClient.changePassword: " + 
					ex.getMessage());
		} catch (TemplateException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AssertionError(
					"TemplateException in i2b2ServicesClient.changePassword: " 
					+ ex.getMessage());
		}


	}

	private void parseResponseXML(String xmlResponse) throws HttpStatusException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new InputSource(new StringReader(xmlResponse)));
			NodeList nList = doc.getElementsByTagName("status");
			Element el = (Element) nList.item(0);

			if (el.getAttribute("type").equals("ERROR")) {
				LOGGER.error(xmlResponse);
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, "Error while changing password. Please contact the administrator.");
			}


		} catch (ParserConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
			throw new AssertionError("ParserConfigurationException in i2b2ServicesClient.parseResponseXML");
		} catch (SAXException e) {
			LOGGER.error(e.getMessage(), e);
			throw new AssertionError("SAXException in i2b2ServicesClient.parseResponseXML");
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new AssertionError("IOException in i2b2ServicesClient.parseResponseXML");
		}


	}

	private void setFMConfigs() {
		this.cfg = new Configuration();
		this.cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		this.cfg.setObjectWrapper(new DefaultObjectWrapper());

	}
}
