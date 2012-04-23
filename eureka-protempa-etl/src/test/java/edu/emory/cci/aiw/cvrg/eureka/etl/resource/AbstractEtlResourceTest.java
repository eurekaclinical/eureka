/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;

import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractResourceTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.ContextTestListener;
import edu.emory.cci.aiw.cvrg.eureka.etl.test.Setup;

/**
 * @author hrathod
 */
public abstract class AbstractEtlResourceTest extends AbstractResourceTest {

	@Override
	protected final Class<? extends ServletContextListener> getListener() {
		return ContextTestListener.class;
	}

	@Override
	protected final Class<? extends Filter> getFilter() {
		return GuiceFilter.class;
	}

	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[]{new AppTestModule()};
	}
}
