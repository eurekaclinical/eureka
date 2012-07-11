package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * An implementation of the {@link PropositionDao} interface, backed by JPA
 * entities and queries.
 *
 * @author hrathod
 */
public class JpaPropositionDao extends GenericDao<Proposition, Long> implements
		PropositionDao {

	/**
	 * Create an object with the given entity manager provider.
	 * @param inProvider An entity manager provider.
	 */
	@Inject
	public JpaPropositionDao (Provider<EntityManager> inProvider) {
		super(Proposition.class, inProvider);
	}
}
