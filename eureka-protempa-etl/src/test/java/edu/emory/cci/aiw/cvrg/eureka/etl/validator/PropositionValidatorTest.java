package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

import static org.junit.Assert.assertTrue;

public class PropositionValidatorTest {

	private static Long USER_ID = Long.valueOf(1L);

	@Test
	public void testNoPropositions() {
		List<PropositionWrapper> wrappers = new
				ArrayList<PropositionWrapper>();
		PropositionValidatorImpl validator =
				new PropositionValidatorImpl();
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
		PropositionValidatorImpl validator =
				new PropositionValidatorImpl();
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

}
