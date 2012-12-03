package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;

public class JpaValueComparatorDao extends GenericDao<ValueComparator, Long> implements
        ValueComparatorDao {
	
	@Inject
	protected JpaValueComparatorDao(Provider<EntityManager> inManagerProvider) {
		super(ValueComparator.class, inManagerProvider);
	}
}
