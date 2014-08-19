package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import java.io.File;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.protempa.dest.keyloader.Criteria;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
class CohortCriteria implements Criteria {
	private Cohort cohort;

	CohortCriteria(File config) {
		if (config == null) {
			throw new IllegalArgumentException("config cannot be null");
		}
	}

	@Override
	public boolean evaluate(List<Proposition> propositions) {
		return this.cohort.evaluate(propositions);
	}

	@Override
	public String[] getPropositionIdsSpecified() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
}
