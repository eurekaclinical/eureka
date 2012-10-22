/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 Emory University
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.awt.PageAttributes;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

/**
 *
 * @author hrathod
 */
public class ConfigurationResourceTest extends AbstractEtlResourceTest {

	@Test
	public void testConfigurationList() {
		WebResource resource = this.resource();
		Configuration configuration = resource.path("/api/configuration/get/1").accept(
				MediaType.APPLICATION_JSON).get(Configuration.class);
		Assert.assertNotNull(configuration);
	}
}
