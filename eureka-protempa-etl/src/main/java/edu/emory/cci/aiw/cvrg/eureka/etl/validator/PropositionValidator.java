/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

/**
 *
 * @author hrathod
 */
public interface PropositionValidator {

	void setConfiguration(Configuration inConfiguration);

	void setTargetProposition(PropositionWrapper inWrapper);

	void setPropositions (List<PropositionWrapper> inWrappers);

	boolean validate() throws PropositionValidatorException;

	List<String> getMessages();

}
