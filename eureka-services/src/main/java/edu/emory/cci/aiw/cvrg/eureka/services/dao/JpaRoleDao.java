package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.Role;

/**
 * A {@link RoleDao} implementation, backed by JPA entities and queries.
 * 
 * @author hrathod
 * 
 */
public class JpaRoleDao implements RoleDao {

	/**
	 * The entity manager to be used for communication with the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Create a new object with the given entity manager.
	 * 
	 * @param manager An {@link EntityManager} used for communication with the
	 *            data store.
	 */
	@Inject
	public JpaRoleDao(EntityManager manager) {
		this.entityManager = manager;
	}

	@Override
	public List<Role> getRoles() {
		Query query = this.entityManager.createQuery("select r from Role r",
				Role.class);
		return query.getResultList();
	}

	@Override
	public Role getRoleByName(String name) {
		Query query = this.entityManager.createQuery(
				"select r from Role r where r.name = ?1", Role.class)
				.setParameter(1, name);
		return (Role) query.getSingleResult();
	}

}
