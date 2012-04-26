package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
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
 */
public class JpaFileDao extends GenericDao<FileUpload, Long>
		implements FileDao {

	/**
	 * Construct an object with the given entity manager.
	 *
	 * @param inEMProvider The entity manager provider used to fetch an entity
	 *                     manager to work with the data store.
	 */
	@Inject
	public JpaFileDao(Provider<EntityManager> inEMProvider) {
		super(FileUpload.class, inEMProvider);
	}

	/**
	 * Gets all the file uploads that belong to the given user.
	 * @param userId The unique identifier for the user.
	 *
	 * @return A list of file uploads belonging to the given user.
	 */
	@Override
	public List<FileUpload> getByUserId(Long userId) {
		QueryPathProvider<FileUpload, Long> provider = new
				QueryPathProvider<FileUpload, Long>() {
			@Override
			public Path<Long> getPath(Root<FileUpload> root,
					CriteriaBuilder builder) {
				return root.get(FileUpload_.user).get(User_.id);
			}
		};
		return this.getListByAttribute(provider, userId);
	}
}
