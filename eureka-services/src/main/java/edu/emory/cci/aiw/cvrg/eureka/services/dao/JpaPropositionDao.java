package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of the {@link PropositionDao} interface, backed by JPA
 * entities and queries.
 *
 * @author hrathod
 */
public class JpaPropositionDao extends GenericDao<Proposition, Long>
		implements PropositionDao {

	/**
	 * Create an object with the given entity manager provider.
	 *
	 * @param inProvider An entity manager provider.
	 */
	@Inject
	public JpaPropositionDao(Provider<EntityManager> inProvider) {
		super(Proposition.class, inProvider);
	}

	@Override
	public Proposition getByKey(String inKey) {
		return this.getUniqueByAttribute(Proposition_.key, inKey);
	}

	@Override
	public List<Proposition> getByUserId(Long inId) {
		QueryPathProvider<Proposition, Long> provider =
				new QueryPathProvider<Proposition, Long>() {
					@Override
					public Path<Long> getPath(Root<Proposition> root,
							CriteriaBuilder builder) {
						return root.get(Proposition_.user).get(User_.id);
					}
				};
		return this.getListByAttribute(provider, inId);
	}
}
