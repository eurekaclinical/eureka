package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
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
	@Transactional
	public void save(FileUpload fileUpload) {
		this.entityManager.persist(fileUpload);
	}

	@Override
	public FileUpload get(Long inId) {
		return this.entityManager
				.createQuery("select f from FileUpload f where f.id = ?1",
						FileUpload.class).setParameter(1, inId)
				.getSingleResult();
	}

	@Override
	public List<FileUpload> getByUserId(Long userId) {
		return this.entityManager
				.createQuery(
						"select f from FileUpload f where f.user.id  = ?1",
						FileUpload.class).setParameter(1, userId)
				.getResultList();
	}
}
