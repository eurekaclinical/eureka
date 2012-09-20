package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.util.ArrayList;
import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;

public final class PropositionUtil {

	private PropositionUtil () {
		// do not allow instantiation.
	}

	private static PropositionWrapper.Type getType(Proposition
		inProposition) {
		if ((inProposition.getTemporalPattern() != null) || ((inProposition
			.getAbstractedFrom() != null) && (!inProposition
			.getAbstractedFrom().isEmpty()))) {
			return PropositionWrapper.Type.AND;
		} else {
			return PropositionWrapper.Type.OR;
		}
	}

	private static List<Proposition> getTargets(Proposition inProposition,
		PropositionWrapper.Type inType) {
		List<Proposition> propositions;
		List<Proposition> targets;

		if (inType == PropositionWrapper.Type.AND) {
			targets = inProposition.getAbstractedFrom();
		} else {
			targets = inProposition.getInverseIsA();
		}

		if (targets == null) {
			propositions = new ArrayList<Proposition>();
		} else {
			propositions = targets;
		}

		return propositions;
	}

	public static PropositionWrapper wrap(Proposition inProposition,
		boolean summarize) {

		PropositionWrapper wrapper = new PropositionWrapper();
		PropositionWrapper.Type type = PropositionUtil.getType(inProposition);

		if (!summarize) {
			List<PropositionWrapper> children =
				new ArrayList<PropositionWrapper>();
			wrapper.setSummarized(false);
			for (Proposition target : PropositionUtil.getTargets
				(inProposition, type)) {
				children.add(PropositionUtil.wrap(target, true));
			}
			wrapper.setChildren(children);
		} else {
			wrapper.setSummarized(true);
		}

		if (inProposition.getId() != null) {
			wrapper.setId(inProposition.getId());
		}

		if (inProposition.getUserId() != null) {
			wrapper.setUserId(inProposition.getUserId());
		}

		wrapper.setInSystem(inProposition.isInSystem());
		wrapper.setType(type);
		wrapper.setAbbrevDisplayName(inProposition.getAbbrevDisplayName());
		wrapper.setDisplayName(inProposition.getDisplayName());
		wrapper.setKey(inProposition.getKey());
		wrapper.setCreated(inProposition.getCreated());
		wrapper.setLastModified(inProposition.getLastModified());

		return wrapper;
	}

	public static List<PropositionWrapper> wrapAll (List<Proposition>
		inPropositions) {
		List<PropositionWrapper> wrappers = new ArrayList<PropositionWrapper>
			(inPropositions.size());
		for (Proposition proposition : inPropositions) {
			wrappers.add(PropositionUtil.wrap(proposition, false));
		}
		return wrappers;
	}

	public static Proposition unwrap(PropositionWrapper inWrapper,
		PropositionDao inPropositionDao) {

		Proposition proposition;
		List<Proposition> targets = new ArrayList<Proposition>();

		if (inWrapper.getId() != null) {
			proposition = inPropositionDao.retrieve(inWrapper.getId());
		} else {
			proposition = new Proposition();
		}

		if (inWrapper.getChildren() != null) {
			for (PropositionWrapper child : inWrapper.getChildren()) {
				if (child.isInSystem()) {
					Proposition p = inPropositionDao.getByKey(child.getKey
						());
					if (p == null) {
						p = new Proposition();
						p.setKey(child.getKey());
						p.setInSystem(true);
					}
					targets.add(p);
				} else {
					targets.add(inPropositionDao.retrieve(child.getId()));
				}
			}
		}

		if (inWrapper.getType() == PropositionWrapper.Type.AND) {
			proposition.setAbstractedFrom(targets);
		} else {
			proposition.setInverseIsA(targets);
		}

		proposition.setKey(inWrapper.getKey());
		proposition.setAbbrevDisplayName(inWrapper.getAbbrevDisplayName());
		proposition.setDisplayName(inWrapper.getDisplayName());
		proposition.setInSystem(inWrapper.isInSystem());

		if (inWrapper.getUserId() != null) {
			proposition.setUserId(inWrapper.getUserId());
		}
		return proposition;
	}
}
