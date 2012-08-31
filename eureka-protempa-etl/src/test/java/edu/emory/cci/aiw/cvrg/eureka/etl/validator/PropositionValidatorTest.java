package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.stanford.smi.protege.util.Assert;

import static org.junit.Assert.assertTrue;

public class PropositionValidatorTest {

	private static Long USER_ID = Long.valueOf(1L);

	@Test
	public void testNoPropositions() {
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();
		PropositionValidatorImpl validator = new PropositionValidatorImpl();
		validator.setPropositions(wrappers);
		validator.setUserId(USER_ID);

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
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();
		PropositionWrapper wrapper = new PropositionWrapper();
		wrapper.setAbbrevDisplayName("Test");
		wrapper.setDisplayName("Wrapper for unit tests.");
		wrappers.add(wrapper);
		PropositionValidatorImpl validator = new PropositionValidatorImpl();
		validator.setUserId(USER_ID);
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
		PropositionWrapper wrapper1 = new PropositionWrapper();
		PropositionWrapper wrapper2 = new PropositionWrapper();
		PropositionWrapper wrapper3 = new PropositionWrapper();

		wrapper1.setId(Long.valueOf(1L));
		wrapper1.setAbbrevDisplayName("test-prop-1");

		wrapper2.setId(Long.valueOf(2L));
		wrapper2.setAbbrevDisplayName("test-prop-2");

		wrapper3.setId(null);
		wrapper3.setAbbrevDisplayName("test-prop-3");

		List<Long> targets1 = new ArrayList<Long>();
		targets1.add(Long.valueOf(2L));
		wrapper1.setUserTargets(targets1);

		List<Long> targets2 = new ArrayList<Long>();
		targets2.add(Long.valueOf(1L));
		wrapper2.setUserTargets(targets2);

		List<Long> targets3 = new ArrayList<Long>();
		targets3.add(Long.valueOf(1L));
//		targets3.add(Long.valueOf(2L));
		wrapper3.setUserTargets(targets3);

		List<PropositionWrapper> propositions = new ArrayList
			<PropositionWrapper>();
		propositions.add(wrapper1);
		propositions.add(wrapper2);

		PropositionValidator validator = new PropositionValidatorImpl();
		validator.setUserId(USER_ID);
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
