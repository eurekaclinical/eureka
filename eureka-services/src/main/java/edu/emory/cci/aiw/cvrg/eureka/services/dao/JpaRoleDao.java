package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role_;

/**
 * A {@link RoleDao} implementation, backed by JPA entities and queries.
 *
 * @author hrathod
 *
 */
public class JpaRoleDao extends GenericDao<Role, Long> implements RoleDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			JpaRoleDao.class);

	/**
	 * Create a new object with the given entity manager.
	 *
	 * @param inManagerProvider A provider for entity manager instances.
	 */
	@Inject
	public JpaRoleDao(Provider<EntityManager> inManagerProvider) {
		super(Role.class, inManagerProvider);
	}

	@Override
	public Role getRoleByName(String name) {
		return this.getUniqueByAttribute(Role_.name, name);
	}
}
