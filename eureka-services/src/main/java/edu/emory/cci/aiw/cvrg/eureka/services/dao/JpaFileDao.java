package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;

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
	 *            manager to work with the data store.
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
		getEntityManager().persist(fileUpload);
	}

	@Override
	public FileUpload get(Long inId) {
		return getEntityManager()
				.createQuery("select f from FileUpload f where f.id = ?1",
						FileUpload.class).setParameter(1, inId)
				.getSingleResult();
	}

	@Override
	public List<FileUpload> getByUserId(Long userId) {
		return getEntityManager()
				.createQuery(
						"select f from FileUpload f where f.user.id  = ?1",
						FileUpload.class).setParameter(1, userId)
				.getResultList();
	}
}