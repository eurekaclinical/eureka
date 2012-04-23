/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.test;

/**
 * Sets up the data needed to run unit or resource tests for the module.
 *
 * @author hrathod
 */
public interface TestDataProvider {

	/**
	 * Set up the data source for the test with needed data.
	 *
	 * @throws TestDataException If the data can not be provided properly.
	 */
	public void setUp() throws TestDataException;

	/**
	 * Tear down the data source for the test.
	 *
	 * @throws TestDataException If the data can not be cleaned up properly.
	 */
	public void tearDown() throws TestDataException;
}
