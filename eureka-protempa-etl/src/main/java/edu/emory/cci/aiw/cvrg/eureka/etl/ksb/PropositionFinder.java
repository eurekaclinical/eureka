package edu.emory.cci.aiw.cvrg.eureka.etl.ksb;

import java.io.File;

import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.PropositionDefinition;
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

public class PropositionFinder {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(PropositionFinder.class);
	private static final File CONF_DIR = new File("/etc/eureka/etlconfig");
	private static final String CONF_PREFIX = "config";
	private final KnowledgeSource knowledgeSource;

	public PropositionFinder (Configuration inConfiguration) throws
		PropositionFinderException {
		Long confId = inConfiguration.getId();
		try {
			String idStr = String.valueOf(confId.longValue());
			String confFileName = CONF_PREFIX + idStr + ".ini";
			Configurations configurations =
				new INICommonsConfigurations(CONF_DIR);
			SourceFactory sf =
				new SourceFactory(configurations,
					confFileName);
			this.knowledgeSource = sf.newKnowledgeSourceInstance();
		} catch (BackendProviderSpecLoaderException e) {
			throw new PropositionFinderException(e);
		} catch (InvalidConfigurationException e) {
			throw new PropositionFinderException(e);
		} catch (ConfigurationsLoadException e) {
			throw new PropositionFinderException(e);
		} catch (BackendNewInstanceException e) {
			throw new PropositionFinderException(e);
		} catch (BackendInitializationException e) {
			throw new PropositionFinderException(e);
		}
	}

	public PropositionDefinition find(String inKey)
			throws PropositionFinderException {
		PropositionDefinition definition = null;
		try {
			definition = this.knowledgeSource.readPropositionDefinition(inKey);
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return definition;
	}
}
