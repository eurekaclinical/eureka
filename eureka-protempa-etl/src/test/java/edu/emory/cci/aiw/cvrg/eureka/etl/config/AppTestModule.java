package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorImpl;

/**
 *
 * @author hrathod
 */
public class AppTestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new JpaPersistModule("backend-jpa-unit"));
		bind(JobDao.class).to(JpaJobDao.class);
		bind(ConfDao.class).to(JpaConfDao.class);
		bind(PropositionValidator.class).to(PropositionValidatorImpl.class);
	}
}
