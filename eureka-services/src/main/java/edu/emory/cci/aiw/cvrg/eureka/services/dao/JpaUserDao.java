package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author hrathod
 *
 */
public class JpaUserDao extends GenericDao<User, Long> implements UserDao {

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 * the data store.
	 */
	@Inject
	public JpaUserDao(Provider<EntityManager> inEMProvider) {
		super(User.class, inEMProvider);
	}

	@Override
	public User getByName(String name) {
		User user = this.getUniqueByAttribute(User_.email, name);
		return user;

//		EntityManager entityManager = this.getEntityManager();
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
//		Root<User> root = criteriaQuery.from(User.class);
//		Path<String> path = root.get(User_.email);
//		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery.
//				where(builder.equal(path, name)));
//		return typedQuery.getSingleResult();
	}

	@Override
	public User getByVerificationCode(String inCode) {
		return this.getUniqueByAttribute(User_.verificationCode, inCode);
	}
}
