/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

/**
 *
 * @author hrathod
 */
public class PropositionDaoTest extends AbstractServiceTest {

	@Test
	public void testDao() {
		PropositionDao dao = this.getInstance(PropositionDao.class);
		List<Proposition> propositions = dao.getAll();
		Assert.assertEquals(3, propositions.size());

		for (Proposition proposition : propositions) {
			System.out.println(proposition.getUser());
		}
	}
}
