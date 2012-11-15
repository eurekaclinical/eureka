package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropertyConstraint;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.AbsoluteTimeUnit;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;

final class HighLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<HighLevelAbstraction, HighLevelAbstractionDefinition> {

	@Override
	public HighLevelAbstractionDefinition pack(HighLevelAbstraction proposition) {
		HighLevelAbstractionDefinition result = new HighLevelAbstractionDefinition(
		        proposition.getId().toString());

		for (Relation rel : proposition.getRelations()) {
			TemporalExtendedPropositionDefinition tepdLhs = buildExtendedProposition(rel
			        .getLhsExtendedProposition());
			TemporalExtendedPropositionDefinition tepdRhs = buildExtendedProposition(rel
			        .getRhsExtendedProposition());
			result.add(tepdLhs);
			result.add(tepdRhs);
			result.setRelation(tepdLhs, tepdRhs, buildRelation(rel));
		}

		return result;
	}

	private TemporalExtendedPropositionDefinition buildExtendedProposition(
	        ExtendedProposition ep) {
		TemporalExtendedPropositionDefinition tepd = new TemporalExtendedPropositionDefinition(
		        ep.getId().toString());

		if (ep.getPropertyConstraint() != null) {
			PropertyConstraint pc = new PropertyConstraint();
			pc.setPropertyName(ep.getPropertyConstraint().getPropertyName());
			pc.setValue(ValueType.VALUE.parse(ep.getPropertyConstraint()
			        .getValue()));
			pc.setValueComp(ValueComparator.EQUAL_TO);

			tepd.getPropertyConstraints().add(pc);
		}
		tepd.setMinLength(ep.getMinDuration());
		tepd.setMinLengthUnit(unit(ep.getMinDurationTimeUnit()));
		tepd.setMaxLength(ep.getMaxDuration());
		tepd.setMaxLengthUnit(unit(ep.getMaxDurationTimeUnit()));

		return tepd;
	}

	private static AbsoluteTimeUnit unit(TimeUnit unit) {
		return AbsoluteTimeUnit.nameToUnit(unit.getName());
	}

	private org.protempa.proposition.interval.Relation buildRelation(
	        Relation rel) {
		return new org.protempa.proposition.interval.Relation(null, null, null,
		        null, rel.getMins1f2(), unit(rel.getMins1f2TimeUnit()),
		        rel.getMaxs1f2(), unit(rel.getMaxs1f2TimeUnit()),
		        rel.getMinf1s2(), unit(rel.getMinf1s2TimeUnit()),
		        rel.getMaxf1s2(), unit(rel.getMaxf1s2TimeUnit()), null, null,
		        null, null);
	}
}
