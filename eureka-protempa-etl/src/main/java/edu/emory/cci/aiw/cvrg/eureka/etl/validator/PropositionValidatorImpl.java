package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;

public class PropositionValidatorImpl implements PropositionValidator {

	private enum PropositionType {

		HIGH_LEVEL_ABSTRACTION, SLICE, EVENT, PRIMITIVE_PARAMETER,
		CONSTANT, PAIR, LOW_LEVEL_ABSTRACTION, INVALID
	}

	private static class ValidatorVisitor implements
		PropositionDefinitionVisitor {

		private PropositionType type;

		public PropositionType getType() {
			return type;
		}

		public void setType(PropositionType inType) {
			type = inType;
		}

		@Override
		public void visit(Collection<? extends PropositionDefinition>
			propositionDefinitions) {
			throw new UnsupportedOperationException(
				"Visiting a collection " +
					"" + "is not supported.");
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

	private static final Logger LOGGER = LoggerFactory.getLogger
		(PropositionValidatorImpl.class);
	private PropositionWrapper targetProposition;
	private List<PropositionWrapper> propositions;
	private Configuration configuration;
	private final List<String> messages;

	public PropositionValidatorImpl() {
		this.messages = new ArrayList<String>();
		this.propositions = new ArrayList<PropositionWrapper>();
	}

	@Override
	public boolean validate() throws PropositionValidatorException {
		boolean result = true;

		if (this.targetProposition != null) {
			LOGGER.debug("Checking proposition {}", this.targetProposition);
			this.validateSingle(this.targetProposition);
		} else if (this.propositions != null) {
			if (detectNullId()) {
				this.addMessage("Found proposition with NULL id");
				result = false;
			} else {
				for (PropositionWrapper wrapper : this.propositions) {
					LOGGER.debug("Checking proposition {}", wrapper);
					if (! this.validateSingle(wrapper)) {
						result = false;
						break;
					}
				}
			}
		}

		return result;
	}

	private boolean validateSingle(PropositionWrapper inWrapper) throws
		PropositionValidatorException {

		boolean result;
		Stack<Long> cycleStack = new Stack<Long>();
		boolean cycle = this.detectCycle(inWrapper, cycleStack);

		if (cycle) {
			this.addMessage(this.createCycleMessage(inWrapper, cycleStack));
			result = false;
		} else if (inWrapper.getChildren() != null) {
			List<PropositionType> types = new ArrayList<PropositionType>();
			try {
				PropositionFinder propositionFinder = new PropositionFinder(
					this.configuration);
				for (PropositionWrapper child : inWrapper.getChildren()) {
					if (child.isInSystem()) {
						try {
							types.add(
								this.getSystemPropositionType
									(propositionFinder.find(child.getKey())));
						} catch (PropositionFinderException e) {
							throw new PropositionValidatorException(e);
						}
					} else {
						types.add(this.getUserPropositionType(child.getId()));
					}
				}
			} catch (PropositionFinderException e) {
				throw new PropositionValidatorException(e);
			}
			if (types.contains(PropositionType.INVALID)) {
				this.addMessage(
					"Proposition " + inWrapper.getAbbrevDisplayName() +
						"has invalid definition.");
				result = false;
			} else {
				result = this.isSame(types);
			}
		} else {
			result = true;
		}

		return result;
	}

	private String createCycleMessage(PropositionWrapper inWrapper,
		Stack<Long> cycleStack) {
		StringBuilder builder = new StringBuilder();
		while (!cycleStack.isEmpty()) {
			Long id = cycleStack.pop();
			PropositionWrapper wrapper = id == null ? null : this.findById
				(id);
			if (wrapper != null) {
				builder.append(" ").append(wrapper.getAbbrevDisplayName())
					.append(" ");
			}
		}
		return "Cycle detected in definition of " + inWrapper
			.getAbbrevDisplayName() + " [" + builder.toString() + "]";
	}

	private boolean detectCycle(PropositionWrapper inWrapper,
		Stack<Long> inSeen) throws PropositionValidatorException {

		boolean cycle = false;

		if (inWrapper.getId() == null && inWrapper != this
			.targetProposition) {
			throw new PropositionValidatorException(
				"Proposition " +
					inWrapper.getAbbrevDisplayName() + " is not the target " +
					"proposition and does not have an ID.");
		}

		if (inSeen.contains(inWrapper.getId())) {
			cycle = true;
			// do this for the error stack
			inSeen.push(inWrapper.getId());
		} else if (inWrapper.getChildren() != null) {
			inSeen.push(inWrapper.getId());
			for (PropositionWrapper child : inWrapper.getChildren()) {
				LOGGER.debug("CHILD: {}", child);
				if (!child.isInSystem()) {
					PropositionWrapper target = this.findById(child.getId());
					cycle = detectCycle(target, inSeen);
					if (cycle) {
						break;
					}
				}
			}
			if (!cycle) {
				inSeen.pop();
			}
		} else {
			cycle = false;
		}
		return cycle;
	}

	private boolean detectNullId() {
		boolean result = false;
		for (PropositionWrapper wrapper : this.propositions) {
			if (wrapper.getId() == null) {
				result = true;
			}
		}
		return result;
	}

	private PropositionType getSystemPropositionType(PropositionDefinition
		inDefinition) throws PropositionValidatorException {
		PropositionType result;
		ValidatorVisitor visitor = new ValidatorVisitor();
		inDefinition.accept(visitor);
		result = visitor.getType();
		return result;
	}

	private PropositionType getUserPropositionType(Long inTarget) throws
		PropositionValidatorException {

		PropositionType result = PropositionType.INVALID;
		List<PropositionType> childTypes = new ArrayList<PropositionType>();
		PropositionWrapper wrapper = this.findById(inTarget);

		try {
			PropositionFinder propositionFinder = new PropositionFinder(
				this.configuration);
			for (PropositionWrapper child : wrapper.getChildren()) {
				if (child.isInSystem()) {
					try {
						childTypes.add(
							this.getSystemPropositionType(
								propositionFinder.find(
									child.getKey())));
					} catch (PropositionFinderException e) {
						throw new PropositionValidatorException(e);
					}
				} else {
					childTypes.add(this.getUserPropositionType(child.getId
						()));
				}
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
		LOGGER.debug("Looking for proposition id {}", inId);
		PropositionWrapper result = null;
		for (PropositionWrapper wrapper : this.propositions) {
			if (inId.equals(wrapper.getId())) {
				result = wrapper;
				break;
			}
		}
		LOGGER.debug("Returning proposition {}", result);
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

	public PropositionWrapper getTargetProposition() {
		return this.targetProposition;
	}

	public void setTargetProposition(PropositionWrapper inTargetProposition) {
		this.targetProposition = inTargetProposition;
	}

	public List<PropositionWrapper> getPropositions() {
		return this.propositions;
	}

	public void setPropositions(List<PropositionWrapper> inPropositions) {
		this.propositions = inPropositions;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration inConfiguration) {
		configuration = inConfiguration;
	}

	private void addMessage(String inMessage) {
		this.messages.add(inMessage);
	}

	@Override
	public List<String> getMessages() {
		return this.messages;
	}
}
