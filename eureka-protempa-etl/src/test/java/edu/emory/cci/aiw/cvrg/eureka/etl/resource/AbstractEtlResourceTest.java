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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;

import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractResourceTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.ContextTestListener;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.test.Setup;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.arp.javautil.io.TempDirectoryCreator;

/**
 * @author hrathod
 */
public abstract class AbstractEtlResourceTest extends AbstractResourceTest {
	
	static {
		try {
			File configFile = File.createTempFile("eureka", ".properties");
			configFile.deleteOnExit();
			File configDir = new TempDirectoryCreator().create("eureka", null, null);
			Properties props = new Properties();
			props.setProperty("eureka.config.dir", configDir.getAbsolutePath());
			try (FileWriter fw = new FileWriter(configFile)) {
					props.store(fw, null);
			}
			System.setProperty("eureka.config.file", configFile.getAbsolutePath());
			
			EtlProperties etlProps = new EtlProperties();
			File protempaConfigDir = etlProps.getSourceConfigDirectory();
			if (!protempaConfigDir.mkdirs()) {
				throw new RuntimeException("Protempa config dir " + protempaConfigDir.getAbsolutePath() + " could not be created!");
			}
			File protempaConfigFile = etlProps.sourceConfigFile("foo");
			if (!protempaConfigFile.createNewFile()) {
				throw new RuntimeException("Protempa config file " + protempaConfigFile.getAbsolutePath() + " already exists!");
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

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
