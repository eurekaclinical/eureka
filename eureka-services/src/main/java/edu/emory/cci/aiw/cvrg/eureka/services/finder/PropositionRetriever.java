package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

public interface PropositionRetriever<U, K> {
	PropositionWrapper retrieve (U inUserId, K inKey);
}
