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
