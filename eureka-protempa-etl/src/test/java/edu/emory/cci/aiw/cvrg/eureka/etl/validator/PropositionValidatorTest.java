package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Module;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.test.Setup;
import edu.stanford.smi.protege.util.Assert;

import static org.junit.Assert.assertTrue;

public class PropositionValidatorTest extends AbstractTest {

	private static Long USER_ID = Long.valueOf(1L);

	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[] {new AppTestModule()};
	}

	@Test
	public void testNoPropositions() {
		ConfDao confDao = this.getInstance(ConfDao.class);
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();
		PropositionValidatorImpl validator = new PropositionValidatorImpl();
		validator.setPropositions(wrappers);
		validator.setConfiguration(confDao.getByUserId(USER_ID));

		boolean actual;
		try {
			actual = validator.validate();
		} catch (PropositionValidatorException e) {
			e.printStackTrace();
			actual = false;
		}

		assertTrue(actual);
	}

	@Test
	public void testSinglePropositionNoDef() {
		ConfDao confDao = this.getInstance(ConfDao.class);
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();
		PropositionWrapper wrapper = new PropositionWrapper();
		wrapper.setId(1L);
		wrapper.setAbbrevDisplayName("Test");
		wrapper.setDisplayName("Wrapper for unit tests.");
		wrappers.add(wrapper);
		PropositionValidatorImpl validator = new PropositionValidatorImpl();
		validator.setConfiguration(confDao.getByUserId(USER_ID));
		validator.setPropositions(wrappers);

		boolean actual;
		try {
			actual = validator.validate();
		} catch (PropositionValidatorException e) {
			e.printStackTrace();
			actual = false;
		}
		assertTrue(actual);
	}

	@Test
	public void testCycleDetection() {

		ConfDao confDao = this.getInstance(ConfDao.class);

		PropositionWrapper wrapper1 = new PropositionWrapper();
		PropositionWrapper wrapper2 = new PropositionWrapper();
		PropositionWrapper wrapper3 = new PropositionWrapper();

		wrapper1.setId(Long.valueOf(1L));
		wrapper1.setAbbrevDisplayName("test-prop-1");
		wrapper1.setInSystem(false);

		wrapper2.setId(Long.valueOf(2L));
		wrapper2.setAbbrevDisplayName("test-prop-2");
		wrapper2.setInSystem(false);

		wrapper3.setId(null);
		wrapper3.setAbbrevDisplayName("test-prop-3");
		wrapper3.setInSystem(false);

		List<PropositionWrapper> targets1 = new ArrayList<PropositionWrapper>();
		targets1.add(wrapper2);
		wrapper1.setChildren(targets1);

		List<PropositionWrapper> targets2 = new ArrayList<PropositionWrapper>();
		targets2.add(wrapper1);
		wrapper2.setChildren(targets2);

		List<PropositionWrapper> targets3 = new ArrayList<PropositionWrapper>();
		targets3.add(wrapper1);
		wrapper3.setChildren(targets3);

		List<PropositionWrapper> propositions = new ArrayList
			<PropositionWrapper>();
		propositions.add(wrapper1);
		propositions.add(wrapper2);

		PropositionValidator validator = new PropositionValidatorImpl();
		validator.setConfiguration(confDao.getByUserId(USER_ID));
		validator.setPropositions(propositions);
		validator.setTargetProposition(wrapper3);
		boolean result;
		try {
			result = validator.validate();
		} catch (PropositionValidatorException e) {
			result = false;
		}

		Assert.assertFalse(result);
	}

}
