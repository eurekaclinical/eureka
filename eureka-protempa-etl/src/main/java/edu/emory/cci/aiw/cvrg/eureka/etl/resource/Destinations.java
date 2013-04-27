package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
public class Destinations {

	private final XPathExpression typeExpr;
	private final XPathExpression displayNameExpr;
	private final DocumentBuilderFactory docBuilderFactory;
	private final EtlProperties etlProperties;

	public Destinations(EtlProperties etlProperties) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			typeExpr = xpath.compile("/queryResultsHandler/type/text()");
			displayNameExpr = xpath.compile("/queryResultsHandler/displayName/text()");
		} catch (XPathExpressionException ex) {
			throw new AssertionError("Invalid xpath expression: " + ex.getMessage());
		}
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		this.docBuilderFactory.setNamespaceAware(true); // never forget this!
		this.etlProperties = etlProperties;
		if (!this.etlProperties.getDestinationConfigDirectory().exists()) {
			try {
				this.etlProperties.getDestinationConfigDirectory().mkdir();
			} catch (SecurityException ex) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
						"Could not create destination config directory", ex);
			}
		}
	}

	public Destination getDestination(String destId) {
		File file = this.etlProperties.destinationConfigFile(destId);
		if (!file.exists()) {
			throw new HttpStatusException(Status.NOT_FOUND, "Invalid destination id '" + destId + "'");
		}
		return getInstance(destId, file);
	}

	public List<Destination> getAll() {
		List<Destination> result = new ArrayList<Destination>();
		try {
			File[] files = this.etlProperties.getDestinationConfigDirectory().listFiles();
			if (files == null) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"Destination config directory " + this.etlProperties.getDestinationConfigDirectory().getAbsolutePath() + " does not exist");
			}
			for (File file : files) {
				result.add(getInstance(FromConfigFile.toDestId(file), file));
			}
		} catch (SecurityException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
					"Destination config directory " + this.etlProperties.getDestinationConfigDirectory().getAbsolutePath() + " could not be accessed", ex);
		}
		return result;
	}

	private Destination getInstance(String destId, File file) {
		DocumentBuilder builder;
		try {
			builder = this.docBuilderFactory.newDocumentBuilder();
			Document doc = builder.parse(file);
			Destination dest = new Destination();
			dest.setId(destId);
			String type = (String) typeExpr.evaluate(doc, XPathConstants.STRING);
			if (type == null) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, "No type specified in the configuration for destination '" + destId + "'");
			}
			try {
				dest.setType(Destination.Type.valueOf(type));
			} catch (IllegalArgumentException ex) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid destination type '" + type + "' in destination '" + destId + "'; allowed values are " + StringUtils.join(Destination.Type.values(), ", "));
			}
			String displayName = (String) displayNameExpr.evaluate(doc, XPathConstants.STRING);
			dest.setDisplayName(displayName);
			return dest;
		} catch (XPathExpressionException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		} catch (SAXException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		} catch (IOException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		} catch (ParserConfigurationException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
