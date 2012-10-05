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

	static {
		if (LOGGER.isErrorEnabled()) {
			if (!CONF_DIR.exists()) {
				LOGGER.error(
						"Configuration directory "
						+ CONF_DIR.getAbsolutePath()
						+ " does not exist. Proposition finding will not work without it. Please create it and try again.");
			} else if (!CONF_DIR.isDirectory()) {
				LOGGER.error(
						"Path "
						+ CONF_DIR.getAbsolutePath()
						+ " is not a directory. Proposition finding requires it to be a directory.");
			}
		}
	}
	private static final File DEFAULT_CONF_DIR =
			new File(CONF_DIR, "etlconfigdefaults");
	private static final String CONF_PREFIX = "config";
	private static final String DEFAULT_CONF_FILE = "defaults.ini";
	private final KnowledgeSource knowledgeSource;

	public PropositionFinder(Configuration inConfiguration) throws
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
			KnowledgeSource ks = sf.newKnowledgeSourceInstance();
			if (ks == null && DEFAULT_CONF_DIR.exists()) {
				File defaultConfFile = new File(DEFAULT_CONF_DIR,
						DEFAULT_CONF_FILE);
				if (defaultConfFile.exists()) {
					Configurations defaultConfigs =
							new INICommonsConfigurations(DEFAULT_CONF_DIR);
					SourceFactory defaultSF = new SourceFactory(defaultConfigs,
							DEFAULT_CONF_FILE);
					ks = defaultSF.newKnowledgeSourceInstance();
				}
			}
			this.knowledgeSource = ks;
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
