package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role_;

/**
 * A {@link RoleDao} implementation, backed by JPA entities and queries.
 *
 * @author hrathod
 *
 */
public class JpaRoleDao implements RoleDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaRoleDao.class);
	/**
	 * The entity manager to be used for communication with the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Create a new object with the given entity manager.
	 *
	 * @param manager An {@link EntityManager} used for communication with the
	 * data store.
	 */
	@Inject
	public JpaRoleDao(EntityManager manager) {
		this.entityManager = manager;
	}

	@Override
	public List<Role> getRoles() {
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
		Root<Role> root = criteriaQuery.from(Role.class);
		TypedQuery<Role> query = this.entityManager.createQuery(criteriaQuery.
				select(root));
		return query.getResultList();
	}

	@Override
	public Role getRoleByName(String name) {
		Role role = null;
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
		Root<Role> root = criteriaQuery.from(Role.class);
		TypedQuery<Role> query = this.entityManager.createQuery(criteriaQuery.
				where(builder.equal(root.get(Role_.name), name)));
		try {
			role = query.getSingleResult();
		} catch (NoResultException nre) {
			LOGGER.warn("No result found for user name: {}", name);
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Multiple results found for user name: {}", name);
		}
		return role;
	}

	@Override
	public Role getRoleById(Long id) {
		return this.entityManager.find(Role.class, id);
	}

	@Override
	@Transactional
	public void save(Role role) {
		this.entityManager.persist(role);
	}
}
