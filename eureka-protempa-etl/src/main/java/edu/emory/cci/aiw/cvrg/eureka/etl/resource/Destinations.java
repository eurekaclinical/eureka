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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DestinationType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
public final class Destinations extends Configs<EtlDestination, DestinationEntity> {

	private final XPathExpression typeExpr;
	private final XPathExpression displayNameExpr;
	private final DocumentBuilderFactory docBuilderFactory;

	public Destinations(EtlProperties inEtlProperties, EtlUser inEtlUser, DestinationDao inDestinationDao) {
		super("destination", inEtlProperties, inEtlUser, inEtlProperties.getDestinationConfigDirectory(), inDestinationDao);
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
	}

	@Override
	EtlDestination config(String destId, Perm perm) {
		DocumentBuilder builder;
		try {
			builder = this.docBuilderFactory.newDocumentBuilder();
			Document doc = builder.parse(getEtlProperties().destinationConfigFile(destId));
			String type = (String) typeExpr.evaluate(doc, XPathConstants.STRING);
			EtlDestination dest;
			switch (DestinationType.valueOf(type)) {
				case I2B2:
					dest = new EtlI2B2Destination();
					break;
				case COHORT:
					dest = new EtlCohortDestination();
					break;
				default:
					throw new AssertionError("Unexpected destination type " + type);
			}
			
			dest.setId(destId);
			
			dest.setRead(perm.read);
			dest.setWrite(perm.write);
			dest.setExecute(perm.execute);
			
			if (type == null) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, "No type specified in the configuration for destination '" + destId + "'");
			}
			String displayName = (String) displayNameExpr.evaluate(doc, XPathConstants.STRING);
			dest.setDisplayName(displayName);
			return dest;
		} catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	@Override
	List<DestinationEntity> configs(EtlUser user) {
		return user.getDestinations();
	}

	@Override
	List<DestinationGroupMembership> groupConfigs(EtlGroup group) {
		return group.getDestinations();
	}

	@Override
	String toConfigId(File file) {
		return FromConfigFile.toDestId(file);
	}
	
	
}
