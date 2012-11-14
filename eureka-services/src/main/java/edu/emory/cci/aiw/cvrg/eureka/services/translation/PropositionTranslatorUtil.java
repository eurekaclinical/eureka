package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * Contains common utility functions for all implementations of
 * {@link PropositionTranslator}.
 */
class PropositionTranslatorUtil {

	private PropositionTranslatorUtil() {
	}

	/**
	 * Populates the fields common to all data elements and propositions.
	 * 
	 * @param proposition
	 *            the {@link Proposition} to populate. Modified as a result of
	 *            calling this method.
	 * @param dataElement
	 *            the {@link DataElement} to get the data from
	 */
	static <P extends Proposition> void populateCommonFields(P proposition,
	        DataElement dataElement) {
		proposition.setId(dataElement.getId());
		proposition.setKey(dataElement.getKey());
		proposition.setDisplayName(dataElement.getDisplayName());
		proposition.setAbbrevDisplayName(dataElement.getAbbrevDisplayName());
		proposition.setCreated(dataElement.getCreated());
		proposition.setLastModified(dataElement.getLastModified());
		proposition.setUserId(dataElement.getUserId());
	}
}
