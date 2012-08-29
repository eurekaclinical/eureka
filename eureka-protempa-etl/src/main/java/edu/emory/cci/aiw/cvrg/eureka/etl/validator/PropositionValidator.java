package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;

public class PropositionValidator {

	private final PropositionWrapper targetWrapper;
	private final List<PropositionWrapper> wrappers;
	private final String userId;
	private final List<String> messages;

	private enum PropositionType {
		HIGH_LEVEL_ABSTRACTION, SLICE, EVENT, PRIMITIVE_PARAMETER,
		CONSTANT, PAIR, LOW_LEVEL_ABSTRACTION, INVALID
	}

	private static class ValidatorVisitor
			implements PropositionDefinitionVisitor {

		private PropositionType type;

		public PropositionType getType() {
			return type;
		}

		public void setType(PropositionType inType) {
			type = inType;
		}

		@Override
		public void visit(
				Collection<? extends PropositionDefinition>
						propositionDefinitions) {
			throw new UnsupportedOperationException(
					"Visiting a collection is not supported.");
		}

		@Override
		public void visit(LowLevelAbstractionDefinition def) {
			this.setType(PropositionType.LOW_LEVEL_ABSTRACTION);
		}

		@Override
		public void visit(HighLevelAbstractionDefinition def) {
			this.setType(PropositionType.HIGH_LEVEL_ABSTRACTION);
		}

		@Override
		public void visit(SliceDefinition def) {
			this.setType(PropositionType.SLICE);
		}

		@Override
		public void visit(EventDefinition def) {
			this.setType(PropositionType.EVENT);
		}

		@Override
		public void visit(PrimitiveParameterDefinition def) {
			this.setType(PropositionType.PRIMITIVE_PARAMETER);
		}

		@Override
		public void visit(ConstantDefinition def) {
			this.setType(PropositionType.CONSTANT);
		}

		@Override
		public void visit(PairDefinition def) {
			this.setType(PropositionType.PAIR);
		}
	}

	public PropositionValidator(List<PropositionWrapper> inWrappers,
	                            Long inUserId) {
		this(null, inWrappers, inUserId);
	}

	public PropositionValidator(PropositionWrapper inTargetWrapper,
	                            List<PropositionWrapper> inWrappers,
	                            Long inUserId) {
		this(inTargetWrapper, inWrappers, String.valueOf(inUserId.longValue
				()));
	}

	public PropositionValidator(PropositionWrapper inTargetWrapper,
	                            List<PropositionWrapper> inWrappers,
	                            String inUserId) {
		this.targetWrapper = inTargetWrapper;
		this.wrappers = inWrappers;
		this.userId = inUserId;
		this.messages = new ArrayList<String>();
	}

	public boolean validate() throws PropositionValidatorException {
		boolean result = true;
		if (this.targetWrapper == null) {
			for (PropositionWrapper wrapper : this.wrappers) {
				if (!this.validateSingle(wrapper)) {
					result = false;
					break;
				}
			}
		} else {
			result = this.validateSingle(this.targetWrapper);
		}
		return result;
	}

	public boolean validateSingle(PropositionWrapper inWrapper)
			throws PropositionValidatorException {

		boolean result;
		List<String> systemTargets = inWrapper.getSystemTargets();
		List<Long> userTargets = inWrapper.getUserTargets();
		List<PropositionType> types = new ArrayList<PropositionType>();

		if (userTargets != null) {
			for (Long userTarget : userTargets) {
				types.add(this.getUserPropositionType(userTarget));
			}
		}

		if (systemTargets != null) {
			try {
				for (String systemTarget : systemTargets) {
					types.add(this.getSystemPropositionType(
							PropositionFinder.find(systemTarget,
									this.userId)));
				}
			} catch (PropositionFinderException e) {
				throw new PropositionValidatorException(e);
			}
		}

		if (types.contains(PropositionType.INVALID)) {
			this.addMessage("Proposition " + inWrapper.getAbbrevDisplayName()
					+ "has invalid definition.");
			result = false;
		} else {
			result = this.isSame(types);
		}
		return result;
	}

	private PropositionType getSystemPropositionType(
			PropositionDefinition inDefinition)
			throws PropositionValidatorException {
		PropositionType result;
		ValidatorVisitor visitor = new ValidatorVisitor();
		inDefinition.accept(visitor);
		result = visitor.getType();
		return result;
	}

	private PropositionType getUserPropositionType(Long inTarget)
			throws PropositionValidatorException {

		PropositionType result = PropositionType.INVALID;
		List<PropositionType> childTypes = new ArrayList<PropositionType>();
		PropositionWrapper wrapper = this.findById(inTarget);

		for (Long userTarget : wrapper.getUserTargets()) {
			childTypes.add(this.getUserPropositionType(userTarget));
		}

		try {
			for (String systemTarget : wrapper.getSystemTargets()) {
				childTypes.add(this.getSystemPropositionType(
						PropositionFinder.find(systemTarget, this.userId)));
			}
		} catch (PropositionFinderException e) {
			throw new PropositionValidatorException(e);
		}

		if (this.isSame(childTypes)) {
			if (childTypes.isEmpty()) {
				if (wrapper.getType() == PropositionWrapper.Type.AND) {
					result = PropositionType.HIGH_LEVEL_ABSTRACTION;
				} else {
					result = PropositionType.EVENT;
				}
			} else {
				result = childTypes.get(0);
			}
		}
		return result;
	}

	private PropositionWrapper findById(Long inId) {
		return findById(String.valueOf(inId.longValue()));
	}

	private PropositionWrapper findById(String inId) {
		PropositionWrapper result = null;
		for (PropositionWrapper wrapper : this.wrappers) {
			if (wrapper.getId().equals(inId)) {
				result = wrapper;
				break;
			}
		}
		return result;
	}

	private boolean isSame(List<PropositionType> inTypes) {
		boolean result = true;
		if (inTypes.size() > 0) {
			PropositionType first = inTypes.get(0);
			for (PropositionType type : inTypes) {
				if (type != first) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	private void addMessage(String inMessage) {
		this.messages.add(inMessage);
	}

	public List<String> getMessages() {
		return this.messages;
	}
}
