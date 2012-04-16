package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of {@link FileDao} interface using JPA entities.
 *
 * @author hrathod
 *
 */
public class JpaFileDao implements FileDao {

	/**
	 * The entity manager provider.
	 */
	private final Provider<EntityManager> emProvider;

	/**
	 * Construct an object with the given entity manager.
	 *
	 * @param inEMProvider The entity manager provider used to fetch an entity
	 * manager to work with the data store.
	 *
	 */
	@Inject
	public JpaFileDao(Provider<EntityManager> inEMProvider) {
		this.emProvider = inEMProvider;
	}

	/**
	 * Get an entity manager.
	 *
	 * @return An entity manager.
	 */
	private EntityManager getEntityManager() {
		// return this.entityManagerFactory.createEntityManager();
		return this.emProvider.get();
	}

	@Override
	@Transactional
	public void save(FileUpload fileUpload) {
		EntityManager entityManager = this.getEntityManager();
		entityManager.persist(fileUpload);
		entityManager.flush();
	}

	@Override
	public void refresh(FileUpload inFileUpload) {
		this.getEntityManager().refresh(inFileUpload);
	}

	@Override
	public FileUpload get(Long inId) {
		return this.getEntityManager().find(FileUpload.class, inId);
	}

	@Override
	public List<FileUpload> getByUserId(Long userId) {
		FileUpload fileUpload = null;
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FileUpload> criteriaQuery = builder.createQuery(FileUpload.class);
		Root<FileUpload> root = criteriaQuery.from(FileUpload.class);
		TypedQuery<FileUpload> query = entityManager.createQuery(criteriaQuery.
				where(builder.equal(root.get(FileUpload_.user).get(User_.id), userId)));
		return query.getResultList();
	}
}