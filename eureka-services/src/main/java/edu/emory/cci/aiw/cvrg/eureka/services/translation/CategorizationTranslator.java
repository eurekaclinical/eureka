package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.ArrayList;
import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;

final class CategorizationTranslator implements PropositionTranslator<Categorization, CategoricalElement> {

	private final PropositionDao dao;
	
	public CategorizationTranslator(PropositionDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Categorization translate(CategoricalElement element) {
		Categorization result = new Categorization();
		
		List<Proposition> inverseIsA = new ArrayList<Proposition>();
		for (DataElement de : element.getChildren()) {
			inverseIsA.add(dao.retrieve(de.getId()));
		}
		result.setInverseIsA(inverseIsA);
		
		return result;
	}

}
