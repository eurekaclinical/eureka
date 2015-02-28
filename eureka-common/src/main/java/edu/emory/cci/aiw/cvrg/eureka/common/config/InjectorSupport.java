/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arpost
 */
public class InjectorSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(InjectorSupport.class);
	private final Injector injector;
	
	public InjectorSupport(Module[] modules, Stage stage) {
		LOGGER.debug("Creating Guice injector");
		this.injector = Guice.createInjector(
					stage,
					modules.clone());
	}
	
	public InjectorSupport(Module[] modules, AbstractProperties properties) {
		LOGGER.debug("Creating Guice injector");
		this.injector = Guice.createInjector(
					Stage.valueOf(properties.getStage()),
					modules.clone());
	}
	
	public Injector getInjector() {
		return this.injector;
	}
}
