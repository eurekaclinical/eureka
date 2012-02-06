package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 * 
 * @author hrathod
 * 
 */
public class JpaUserDao implements UserDao {

	/**
	 * The entity manager used to communicate with the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Create an object with the give entity manager.
	 * 
	 * @param manager The entity manager to be used for communication with the
	 *            data store.
	 */
	@Inject
	public JpaUserDao(EntityManager manager) {
		this.entityManager = manager;

	}

	@Override
	@Transactional
	public void save(User u) {
		this.entityManager.persist(u);
	}

	@Override
	public List<User> getUsers() {
		final Query query = this.entityManager.createQuery(
				"select u from User u", User.class);
		return query.getResultList();
	}

	@Override
	public User get(Long id) {
		final Query query = this.entityManager.createQuery(
				"select u from User u where u.id = ?1", User.class)
				.setParameter(1, id);
		return (User) query.getSingleResult();
	}

	@Override
	public User get(String name) {
		final Query query = this.entityManager.createQuery(
				"select u from User u where u.email = ?1", User.class).setParameter(
				1, name);
		return (User) query.getSingleResult();
	}

	@Override
	public User delete(Long id) {
		return null;
	}
}
