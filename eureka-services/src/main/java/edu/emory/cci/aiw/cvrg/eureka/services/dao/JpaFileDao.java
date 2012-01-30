package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;

/**
 * An implementation of {@link FileDao} interface using JPA entities.
 * 
 * @author hrathod
 * 
 */
public class JpaFileDao implements FileDao {

	/**
	 * The entity manager used to communicate with the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Construct an object with the given entity manager.
	 * 
	 * @param inEntityManager The entity manager used to communicate with the
	 *            data store.
	 */
	@Inject
	public JpaFileDao(EntityManager inEntityManager) {
		this.entityManager = inEntityManager;
	}

	@Override
	public void save(FileUpload fileUpload) {
		this.entityManager.persist(fileUpload);
	}

	@Override
	public FileUpload get(Long inId) {
		FileUpload fileUpload = this.entityManager
				.createQuery("select FileUpload f from f where f.id = ?1",
						FileUpload.class).setParameter(1, inId)
				.getSingleResult();
		return fileUpload;
	}
}
