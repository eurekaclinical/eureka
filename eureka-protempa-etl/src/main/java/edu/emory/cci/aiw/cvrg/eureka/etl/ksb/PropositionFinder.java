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

public class PropositionFinder {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(PropositionFinder.class);
	private static final String CONF_DIR = "/opt/cvrg_users";
	private static final String USER_PREFIX = "user";

	public static PropositionDefinition find(String inKey, Long inUserId)
			throws PropositionFinderException {
		PropositionDefinition definition = null;
		try {
			String idStr = String.valueOf(inUserId.longValue());
			File userDir = new File(CONF_DIR, USER_PREFIX + idStr);
			File confDir = new File(userDir, ".protempa-configs");
			Configurations configurations =
					new INICommonsConfigurations(confDir);
			SourceFactory sf =
					new SourceFactory(configurations,
							"erat-diagnoses-direct");
			KnowledgeSource knowledgeSource = sf.newKnowledgeSourceInstance();
			definition = knowledgeSource.readPropositionDefinition(inKey);
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
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return definition;
	}
}
