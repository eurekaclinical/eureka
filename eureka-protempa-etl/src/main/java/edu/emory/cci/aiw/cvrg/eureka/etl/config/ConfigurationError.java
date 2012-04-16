/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/**
 *
 * @author hrathod
 */
public class ConfigurationError extends Error {

	/**
	 * Create an error with the given string as the error message.
	 *
	 * @param message The error message.
	 */
	ConfigurationError(String message) {
		super(message);
	}

	/**
	 * Creates an error with the given {@link Throwable} as the root cause.
	 *
	 * @param throwable The root cause.
	 */
	ConfigurationError(Throwable throwable) {
		super(throwable);
	}
}
