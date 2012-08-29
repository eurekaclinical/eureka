package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;
import org.protempa.SourceFactory;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.InvalidConfigurationException;
import org.protempa.bconfigs.commons.INICommonsConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;

/**
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(PropositionResource.class);

	@GET
	@Path("/{userId}/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getProposition(
			@PathParam("userId") String inUserId,
			@PathParam("key") String inKey) {

		PropositionWrapper wrapper = null;
		try {
			PropositionDefinition definition =
					PropositionFinder.find(inKey, inUserId);
			WrapperVisitor visitor = new WrapperVisitor();
			definition.accept(visitor);
			wrapper = visitor.getWrapper();
		} catch (PropositionFinderException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return wrapper;
	}

	private static class WrapperVisitor
			implements PropositionDefinitionVisitor {

		PropositionWrapper wrapper = new PropositionWrapper();

		public PropositionWrapper getWrapper() {
			return this.wrapper;
		}

		private void getInfo(PropositionDefinition inDefinition) {
			this.wrapper.setKey(inDefinition.getId());
			this.wrapper.setInSystem(true);
			this.wrapper.setAbbrevDisplayName(
					inDefinition.getAbbreviatedDisplayName());
			this.wrapper.setDisplayName(inDefinition.getDisplayName());
			this.wrapper.setSystemTargets(
					Arrays.asList(inDefinition.getChildren()));
		}

		@Override
		public void visit(Collection<? extends PropositionDefinition>
				propositionDefinitions) {
			// This class does not handle a list of propositions, yet.
			throw new UnsupportedOperationException(
					"PropositionDefinition lists are not supported, yet.");
		}

		@Override
		public void visit(LowLevelAbstractionDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(HighLevelAbstractionDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(SliceDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(EventDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(PrimitiveParameterDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(ConstantDefinition def) {
			this.getInfo(def);
		}

		@Override
		public void visit(PairDefinition def) {
			this.getInfo(def);
		}
	}
}
