package edu.emory.cci.aiw.cvrg.eureka.services.clients;

import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;

/**
 * @author akalsan
 * @author hrathod
 */
public interface I2b2Client {
	public void changePassword(String email, String passwd) throws 
			HttpStatusException;
}
