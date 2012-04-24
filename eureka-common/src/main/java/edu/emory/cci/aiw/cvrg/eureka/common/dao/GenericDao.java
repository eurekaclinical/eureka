package edu.emory.cci.aiw.cvrg.eureka.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 *
 * @param <T>
 * @param <PK>
 * @author hrathod
 */
public class GenericDao<T, PK> implements Dao<T, PK> {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			GenericDao.class);
	/**
	 * The type for the entities this DAO instance handles.
	 */
	private final Class<T> entityClass;
	/**
	 * Provides entity managers on demand.
	 */
	private final Provider<EntityManager> managerProvider;

	/**
	 * Creates a generic DAO that handles the given type of entity. The entity
	 * manager provider is used to fetch entity managers to interact with the
	 * data store.
	 *
	 * @param inEntityClass The type of entities to handle.
	 * @param inManagerProvider Provides entity managers on demand.
	 */
	protected GenericDao(Class<T> inEntityClass,
			Provider<EntityManager> inManagerProvider) {
		this.entityClass = inEntityClass;
		this.managerProvider = inManagerProvider;
	}

	@Override
	@Transactional
	public T create(T entity) {
		EntityManager entityManager = this.getEntityManager();
		entityManager.persist(entity);
		entityManager.flush();
		return entity;
	}

	@Override
	public T retrieve(PK uniqueId) {
		return this.getEntityManager().find(this.entityClass, uniqueId);
	}

	@Override
	public T update(T entity) {
		return this.create(entity);
	}

	@Override
	public T remove(T entity) {
		EntityManager entityManager = this.getEntityManager();
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			entityManager.remove(entityManager.merge(entity));
		}
		return entity;
	}

	@Override
	public T refresh(T entity) {
		this.getEntityManager().refresh(entity);
		return entity;
	}

	@Override
	public List<T> getAll() {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(this.entityClass);
		criteriaQuery.from(this.entityClass);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	protected <Y> T getUniqueByAttribute(SingularAttribute<T, Y> attribute,
			Y value) {
		TypedQuery<T> query = this.createTypedQuery(attribute, value);
		T result = null;
		try {
			result = query.getSingleResult();
		} catch (NonUniqueResultException nure) {
			LOGGER.error("Result not unique.", nure);
			result = null;
		} catch (NoResultException nre) {
			LOGGER.error("Result not existant", nre);
			result = null;
		}
		return result;
	}

	private <Y> TypedQuery<T> createTypedQuery(SingularAttribute<T, Y> attribute,
			Y value) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(this.entityClass);
		Root<T> root = criteriaQuery.from(this.entityClass);
		Path<Y> path = root.get(attribute);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery.where(
				builder.equal(path, value)));
		return typedQuery;

//		EntityManager entityManager = this.getEntityManager();
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
//		Root<User> root = criteriaQuery.from(User.class);
//		Path<String> path = root.get(User_.email);
//		TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery.
//				where(builder.equal(path, name)));
//		return typedQuery.getSingleResult();
	}

	/**
	 * Returns an entity manager instance that can be used to interact with the
	 * data source.
	 *
	 * @return An instance of EntityManager.
	 */
	protected final EntityManager getEntityManager() {
		return this.managerProvider.get();
	}
}
