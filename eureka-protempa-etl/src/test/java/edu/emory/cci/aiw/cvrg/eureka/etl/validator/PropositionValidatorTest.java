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

		List<Long> targets1 = new ArrayList<Long>();
		targets1.add(2L);

		List<Long> targets2 = new ArrayList<Long>();
		targets2.add(1L);

		wrapper1.setId(1L);
		wrapper1.setAbbrevDisplayName("test-prop-1");
		wrapper1.setUserTargets(targets1);

		wrapper2.setId(2L);
		wrapper2.setAbbrevDisplayName("test-prop-2");
		wrapper2.setUserTargets(targets2);

		List<PropositionWrapper> propositions = new ArrayList
			<PropositionWrapper>();
		propositions.add(wrapper1);
		propositions.add(wrapper2);

		PropositionValidator validator = new PropositionValidatorImpl();
		validator.setUserId(USER_ID);
		validator.setPropositions(propositions);
		boolean result;
		try {
			result = validator.validate();
		} catch (PropositionValidatorException e) {
			result = false;
		}

		Assert.assertFalse(result);
	}

}
