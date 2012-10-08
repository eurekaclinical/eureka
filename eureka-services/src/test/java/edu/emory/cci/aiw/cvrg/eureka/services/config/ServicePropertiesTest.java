package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.util.List;

import org.junit.Test;

import com.google.inject.Module;

import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;

import junit.framework.Assert;

public class ServicePropertiesTest extends AbstractTest {
	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[] { new AppTestModule() };
	}

	@Test
	public void testDefaultSystemProps () {
		ServiceProperties properties = this.getInstance(ServiceProperties
			.class);
		List<String> propositionNames = properties
			.getDefaultSystemPropositions();
		Assert.assertEquals(32,propositionNames.size());
	}
}
