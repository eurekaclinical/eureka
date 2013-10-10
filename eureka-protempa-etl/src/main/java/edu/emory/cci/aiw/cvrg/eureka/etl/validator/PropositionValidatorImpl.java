/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionDefinitionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.ContextDefinition;
import org.protempa.SequentialTemporalPatternDefinition;

public class PropositionValidatorImpl implements PropositionValidator {

	private enum PropositionType {

		HIGH_LEVEL_ABSTRACTION, SLICE, EVENT, PRIMITIVE_PARAMETER, CONSTANT, 
		SEQUENTIAL_TEMPORAL_PATTERN, LOW_LEVEL_ABSTRACTION, CONTEXT, INVALID,
		COMPOUND_LOW_LEVEL
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
		public void visit(
				Collection<? extends PropositionDefinition> propositionDefinitions) {
			throw new UnsupportedOperationException("Visiting a collection "
					+ "" + "is not supported.");
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
		public void visit(SequentialTemporalPatternDefinition def) {
			this.setType(PropositionType.SEQUENTIAL_TEMPORAL_PATTERN);
		}

		@Override
		public void visit(CompoundLowLevelAbstractionDefinition def) {
			setType(PropositionType.COMPOUND_LOW_LEVEL);
		}

		@Override
		public void visit(ContextDefinition def) {
			setType(PropositionType.CONTEXT);
		}
		
		
	}
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionValidatorImpl.class);
	private PropositionDefinition targetProposition;
	private List<PropositionDefinition> propositions;
	private List<PropositionDefinition> userPropositions;
	private String configId;
	private final List<String> messages;
	private final EtlProperties etlProperties;

	@Inject
	public PropositionValidatorImpl(EtlProperties inEtlProperties) {
		this.messages = new ArrayList<String>();
		this.propositions = new ArrayList<PropositionDefinition>();
		this.etlProperties = inEtlProperties;
		this.userPropositions = new ArrayList<PropositionDefinition>();
	}

	@Override
	public boolean validate() throws PropositionValidatorException {
		boolean result = true;

		if (this.targetProposition != null) {
			LOGGER.debug("Checking proposition {}", this.targetProposition);
			result = this.validateSingle(this.targetProposition);
		} else if (this.propositions != null) {
			if (detectNullId()) {
				this.addMessage("Found proposition with NULL id");
				result = false;
			} else {
				for (PropositionDefinition propDef : this.propositions) {
					LOGGER.debug("Checking proposition {}", propDef);
					if (!this.validateSingle(propDef)) {
						result = false;
						break;
					}
				}
			}
		}

		return result;
	}

	private boolean validateSingle(PropositionDefinition inProposition)
			throws PropositionValidatorException {

		boolean result;
		Stack<String> cycleStack = new Stack<String>();
		boolean cycle = this.detectCycle(inProposition, cycleStack);

		if (cycle) {
			this.addMessage(this.createCycleMessage(inProposition, cycleStack));
			result = false;
		} else if (inProposition.getChildren() != null) {
			if (this.etlProperties.getConfigDir() == null) {
				throw new PropositionValidatorException(
						"No Protempa configuration directory is "
						+ "specified in application.properties. "
						+ "Proposition finding will not work without it. "
						+ "Please create it and try again.");
			}
			List<PropositionType> types = new ArrayList<PropositionType>();
			try {
				PropositionDefinitionFinder propositionFinder = new PropositionDefinitionFinder(
						this.configId, this.etlProperties);
				for (String child : inProposition.getChildren()) {
					if (isSystemProp(child)) {
						try {
							PropositionDefinition found = propositionFinder
																 .find(child);
							if (found == null) {
								throw new PropositionValidatorException(
										"Invalid child proposition: " + child);
							}
							types.add(this.getSystemPropositionType(found));
						} catch (PropositionFinderException e) {
							throw new PropositionValidatorException(e);
						}
					} else {
						types.add(this.getUserPropositionType(child));
					}
				}
			} catch (PropositionFinderException e) {
				throw new PropositionValidatorException(e);
			}
			if (types.contains(PropositionType.INVALID)) {
				this.addMessage("Proposition "
						+ inProposition.getAbbreviatedDisplayName()
						+ "has invalid definition.");
				result = false;
			} else {
				result = this.isSame(types);
			}
		} else {
			result = true;
		}

		return result;
	}

	private String createCycleMessage(PropositionDefinition inPropDef,
			Stack<String> cycleStack) {
		StringBuilder builder = new StringBuilder();
		while (!cycleStack.isEmpty()) {
			String id = cycleStack.pop();
			PropositionDefinition propDef = id == null ? null : this
					.findById(id);
			if (propDef != null) {
				builder.append(" ").append(propDef.getId()).append("(")
						.append(propDef.getAbbreviatedDisplayName()).append
						(")").append(" ");
			}
		}
		return "Cycle detected in definition of "
				+ inPropDef.getId() + " ["
				+ builder.toString() + "]";
	}

	private boolean detectCycle(PropositionDefinition inProposition,
			Stack<String> inSeen) throws PropositionValidatorException {

		boolean cycle = false;

		if (inProposition.getId() == null
				&& inProposition != this.targetProposition) {
			throw new PropositionValidatorException("Proposition "
					+ inProposition.getAbbreviatedDisplayName()
					+ " is not the target "
					+ "proposition and does not have an ID.");
		}

		if (inSeen.contains(inProposition.getId())) {
			cycle = true;
			// do this for the error stack
			inSeen.push(inProposition.getId());
		} else if (inProposition.getChildren() != null) {
			inSeen.push(inProposition.getId());
			for (String child : inProposition.getChildren()) {
				LOGGER.debug("CHILD: {}", child);
				if (!isSystemProp(child)) {
					PropositionDefinition target = this.findById(child);
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

	private boolean isSystemProp(String inPropDefId) {
		for (PropositionDefinition propDef : this.userPropositions) {
			if (inPropDefId.equals(propDef.getId())) {
				return false;
			}
		}
		return true;
	}

	private boolean detectNullId() {
		boolean result = false;
		for (PropositionDefinition propDef : this.propositions) {
			if (propDef.getId() == null) {
				result = true;
			}
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

	private PropositionType getUserPropositionType(String inTarget)
			throws PropositionValidatorException {

		if (this.etlProperties.getConfigDir() == null) {
			throw new PropositionValidatorException(
					"No Protempa configuration directory is "
					+ "specified in application.properties. "
					+ "Proposition finding will not work without it. "
					+ "Please create it and try again.");
		}

		PropositionType result = PropositionType.INVALID;
		List<PropositionType> childTypes = new ArrayList<PropositionType>();
		PropositionDefinition propDef = this.findById(inTarget);

		try {
			PropositionDefinitionFinder propositionFinder = new PropositionDefinitionFinder(
					this.configId, this.etlProperties);
			for (String child : propDef.getChildren()) {
				if (isSystemProp(child)) {
					try {
						childTypes.add(this
								.getSystemPropositionType(propositionFinder
								.find(child)));
					} catch (PropositionFinderException e) {
						throw new PropositionValidatorException(e);
					}
				} else {
					childTypes.add(this.getUserPropositionType(child));
				}
			}
		} catch (PropositionFinderException e) {
			throw new PropositionValidatorException(e);
		}

		ValidatorVisitor vv = new ValidatorVisitor();
		propDef.accept(vv);
		if (this.isSame(childTypes)) {
			if (childTypes.isEmpty()) {
				if (vv.getType() == PropositionType.HIGH_LEVEL_ABSTRACTION) {
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

	private PropositionDefinition findById(String inId) {
		LOGGER.debug("Looking for proposition id {}", inId);
		PropositionDefinition result = null;
		List<PropositionDefinition> allProps = new ArrayList
				<PropositionDefinition>();
		allProps.addAll(this.userPropositions);
		allProps.addAll(this.propositions);
		for (PropositionDefinition propDef : allProps) {
			if (inId.equals(propDef.getId())) {
				result = propDef;
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

	public PropositionDefinition getTargetProposition() {
		return this.targetProposition;
	}

	@Override
	public void setTargetProposition(PropositionDefinition inTargetProposition) {
		this.targetProposition = inTargetProposition;
	}

	public List<PropositionDefinition> getPropositions() {
		return this.propositions;
	}

	@Override
	public void setPropositions(List<PropositionDefinition> inPropositions) {
		this.propositions = inPropositions;
	}

	public List<PropositionDefinition> getUserPropositions() {
		return this.userPropositions;
	}

	@Override
	public void setUserPropositions(
			List<PropositionDefinition> inUserPropositions) {
		this.userPropositions = inUserPropositions;
	}

	public String getConfigId() {
		return configId;
	}

	@Override
	public void setConfigId(String inConfiguration) {
		configId = inConfiguration;
	}

	private void addMessage(String inMessage) {
		this.messages.add(inMessage);
	}

	@Override
	public List<String> getMessages() {
		return this.messages;
	}
}
