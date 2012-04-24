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

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;

/**
 * An implementation of {@link FileDao} interface using JPA entities.
 *
 * @author hrathod
 *
 */
public class JpaFileDao extends GenericDao<FileUpload, Long> implements FileDao {

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
		super(FileUpload.class, inEMProvider);
		this.emProvider = inEMProvider;
	}

	@Override
	public List<FileUpload> getByUserId(Long userId) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<FileUpload> criteriaQuery = builder.createQuery(
				FileUpload.class);
		Root<FileUpload> root = criteriaQuery.from(FileUpload.class);
		Path<Long> path = root.get(FileUpload_.user).get(User_.id);
		TypedQuery<FileUpload> query = entityManager.createQuery(criteriaQuery.
				where(builder.equal(path, userId)));
		return query.getResultList();
	}
}
