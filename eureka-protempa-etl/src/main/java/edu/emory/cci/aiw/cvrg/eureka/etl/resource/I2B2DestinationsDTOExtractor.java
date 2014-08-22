package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.io.IOException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
class I2B2DestinationsDTOExtractor extends DestinationsDTOExtractor<EtlI2B2Destination, I2B2DestinationEntity> {

	private final XPathExpression displayNameExpr;
	private final DocumentBuilderFactory docBuilderFactory;
	private final EtlProperties etlProperties;

	public I2B2DestinationsDTOExtractor(EtlProperties inEtlProperties, EtlUserEntity user, EtlGroupDao inGroupDao) {
		super(user, inGroupDao);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			displayNameExpr = xpath.compile("/queryResultsHandler/displayName/text()");
		} catch (XPathExpressionException ex) {
			throw new AssertionError("Invalid xpath expression: " + ex.getMessage());
		}
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		this.docBuilderFactory.setNamespaceAware(true); // never forget this!

		this.etlProperties = inEtlProperties;
	}

	@Override
	EtlI2B2Destination extractDTO(Perm perm,
			I2B2DestinationEntity destinationEntity) {
		try {
			DocumentBuilder builder = this.docBuilderFactory.newDocumentBuilder();
			EtlI2B2Destination dest = new EtlI2B2Destination();
			Document doc = builder.parse(this.etlProperties.destinationConfigFile(destinationEntity.getName()));
			String displayName = (String) displayNameExpr.evaluate(doc, XPathConstants.STRING);
			dest.setName(displayName);

			dest.setId(destinationEntity.getId());

			dest.setRead(perm.read);
			dest.setWrite(perm.write);
			dest.setExecute(perm.execute);

			dest.setOwnerUserId(destinationEntity.getOwner().getId());

			return dest;
		} catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
