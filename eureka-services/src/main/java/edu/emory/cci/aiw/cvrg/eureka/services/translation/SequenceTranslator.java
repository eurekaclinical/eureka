package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelatedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;

public class SequenceTranslator implements
        PropositionTranslator<HighLevelAbstraction, Sequence> {

	private Map<Long, ExtendedProposition> extendedProps;
	private final PropositionDao dao;

	public SequenceTranslator(PropositionDao dao) {
		this.dao = dao;
		this.extendedProps = new HashMap<Long, ExtendedProposition>();
	}

	@Override
	public HighLevelAbstraction translate(Sequence element) {
		HighLevelAbstraction result = new HighLevelAbstraction();

		List<Proposition> abstractedFrom = new ArrayList<Proposition>();
		createExtendedProposition(element.getPrimaryDataElement());
		for (RelatedDataElement rde : element.getRelatedDataElements()) {
			createExtendedProposition(rde.getDataElement());
			abstractedFrom.add(dao.retrieve(rde.getDataElement().getDataElement()));
		}
		result.setAbstractedFrom(abstractedFrom);
		List<Relation> relations = new ArrayList<Relation>();
		for (RelatedDataElement rde : element.getRelatedDataElements()) {
			relations.add(createRelation(rde));
		}
		result.setRelations(relations);

		return result;
	}

	private void createExtendedProposition(DataElement dataElement) {
		if (!this.extendedProps.containsKey(dataElement.getDataElement())) {
			ExtendedProposition ep = new ExtendedProposition();
			ep.setId(dataElement.getDataElement());
			if (dataElement.getHasDuration()) {
				ep.setMinDuration(dataElement.getMinDuration());
				ep.setMaxDuration(dataElement.getMaxDuration());
			}
			if (dataElement.getHasPropertyConstraint()) {
				PropertyConstraint pc = new PropertyConstraint();
				pc.setPropertyName(dataElement.getProperty());
				pc.setValue(dataElement.getPropertyValue());
				ValueComparator vc = new ValueComparator();
				vc.setName("EQUAL_TO");
				ep.setPropertyConstraint(pc);
			}

			this.extendedProps.put(ep.getId(), ep);
		}
	}

	private Relation createRelation(RelatedDataElement relatedDataElementField) {
		Relation rel = new Relation();

		ExtendedProposition dataElement = this.extendedProps.get(relatedDataElementField.getDataElement());
		ExtendedProposition relatedDataElement = this.extendedProps
		        .get(relatedDataElementField.getRhsDataElement());

		rel.setMinf1s2(relatedDataElementField.getRelationMinCount());
		TimeUnit minTimeUnit = new TimeUnit();
		minTimeUnit.setName(relatedDataElementField.getRelationMinUnits());
		rel.setMinf1s2TimeUnit(minTimeUnit);
		rel.setMaxf1s2(relatedDataElementField.getRelationMaxCount());
		TimeUnit maxTimeUnit = new TimeUnit();
		maxTimeUnit.setName(relatedDataElementField.getRelationMaxUnits());
		rel.setMaxf1s2TimeUnit(maxTimeUnit);

		if (relatedDataElementField.getRelationOperator() == RelationOperator.BEFORE) {
			rel.setLhsExtendedProposition(dataElement);
			rel.setRhsExtendedProposition(relatedDataElement);
		} else if (relatedDataElementField.getRelationOperator() == RelationOperator.AFTER) {
			rel.setLhsExtendedProposition(relatedDataElement);
			rel.setRhsExtendedProposition(dataElement);
		}

		return rel;
	}
}
