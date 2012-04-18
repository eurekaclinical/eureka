package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author hrathod
 *
 */
public class JpaUserDao implements UserDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaUserDao.class);
	/**
	 * The provider used to fetch the entity manager to communicate with the
	 * data store.
	 */
	private final Provider<EntityManager> emProvider;

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 * the data store.
	 */
	@Inject
	public JpaUserDao(Provider<EntityManager> inEMProvider) {
		this.emProvider = inEMProvider;
	}

	/**
	 * Get the entity manager used to communicate with the data store.
	 *
	 * @return The entity manager.
	 */
	private EntityManager getEntityManager() {
		return this.emProvider.get();
	}

	@Override
	@Transactional
	public void save(User u) {
		EntityManager entityManager = this.getEntityManager();
		entityManager.persist(u);
		entityManager.flush();
	}

	@Override
	public void refresh(User user) {
		if (user != null) {
			this.getEntityManager().refresh(user);
		}
	}

	@Override
	public List<User> getUsers() {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);
		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery.
				select(root));
		return typedQuery.getResultList();
	}

	@Override
	public User getById(Long id) {
		return this.getEntityManager().find(User.class, id);
	}

	@Override
	public User getByName(String name) {
		User user = null;
		try {
			EntityManager entityManager = this.getEntityManager();
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> root = criteriaQuery.from(User.class);
			TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery.
					where(builder.equal(root.get(User_.email), name)));
			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.warn("No result found for user name: {}", name);
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Multiple results found for user name: {}", name);
		}
		return user;
	}

	@Override
	public User getByVerificationCode(String inCode) {
		User user = null;
		try {
			EntityManager entityManager = this.getEntityManager();
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> root = criteriaQuery.from(User.class);
			criteriaQuery.where(builder.equal(root.get(User_.verificationCode), inCode));
			TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
			return typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.warn("No result found for verification code: {}", inCode);
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Multiple results found for verification code: {}",
					inCode);
		}
		return user;
	}
}
