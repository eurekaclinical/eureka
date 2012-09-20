package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.io.File;
import java.lang.reflect.Proxy;

import org.protempa.AlgorithmSource;
import org.protempa.DataSource;
import org.protempa.FinderException;
import org.protempa.KnowledgeSource;
import org.protempa.PropositionDefinition;
import org.protempa.Protempa;
import org.protempa.ProtempaStartupException;
import org.protempa.Source;
import org.protempa.SourceFactory;
import org.protempa.TermSource;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.InvalidConfigurationException;
import org.protempa.bconfigs.commons.INICommonsConfigurations;
import org.protempa.query.DefaultQueryBuilder;
import org.protempa.query.Query;
import org.protempa.query.QueryBuildException;
import org.protempa.query.handler.QueryResultsHandler;

import edu.emory.cci.aiw.i2b2etl.I2B2QueryResultsHandler;

/**
 * This class actually runs Protempa.
 * <p/>
 * There are two configuration files for each Protempa job. One, an INI file,
 * configures Protempa. Another, an XML file, configures the i2b2 query results
 * handler. Each Protempa configuration (an INI file) has associated with it one
 * i2b2 query results handler configuration file (an XML file). They are
 * associated by name. An INI file named my_config.ini has an associated XML
 * file my_config.xml. The INI and XML files go into a directory specified in
 * this class' constructor. The default is
 * <code>/etc/eureka/etlconfig</code>.
 *
 * @author Andrew Post
 */
public class ETL {

	private static final File EUREKA_CONFIG_DIR = new File("/etc/eureka");
	private final File configDefaultsDirectory;
	private final File configDirectory;
	private PreventCloseKnowledgeSource knowledgeSource;
	private PreventCloseDataSource dataSource;
	private PreventCloseAlgorithmSource algorithmSource;
	private PreventCloseTermSource termSource;

	public ETL() {
		this.configDirectory = new File(EUREKA_CONFIG_DIR, "etlconfig");
		this.configDefaultsDirectory =
			new File(EUREKA_CONFIG_DIR, "etlconfigdefaults");
	}

	public void run(String configId, PropositionDefinition[]
		inPropositionDefinitions)
		throws EtlException {
		Protempa protempa = null;

		try {
			protempa = getNewProtempa(configId + ".ini");
			DefaultQueryBuilder q = new DefaultQueryBuilder();
			q.setPropositionDefinitions(inPropositionDefinitions);
			Query query = protempa.buildQuery(q);
			File i2b2Config = new File(this.configDirectory,
				configId + ".xml");
			QueryResultsHandler qrh = new I2B2QueryResultsHandler(i2b2Config);
			protempa.execute(query, qrh);
		} catch (InvalidConfigurationException e) {
			throw new EtlException(e);
		} catch (QueryBuildException e) {
			throw new EtlException(e);
		} catch (BackendProviderSpecLoaderException e) {
			throw new EtlException(e);
		} catch (ConfigurationsLoadException e) {
			throw new EtlException(e);
		} catch (FinderException e) {
			throw new EtlException(e);
		} catch (BackendInitializationException e) {
			throw new EtlException(e);
		} catch (ProtempaStartupException e) {
			throw new EtlException(e);
		} catch (BackendNewInstanceException e) {
			throw new EtlException(e);
		} finally {
			if (protempa != null) {
				protempa.close();
			}
		}
	}

	private Protempa getNewProtempa(String configFilename)
		throws ConfigurationsLoadException,
		BackendProviderSpecLoaderException, InvalidConfigurationException,
		ProtempaStartupException, BackendInitializationException,
		BackendNewInstanceException {

		Configurations defaultConfigs =
			new INICommonsConfigurations(this.configDefaultsDirectory);
		SourceFactory defaultsSF =
			new SourceFactory(defaultConfigs, "defaults.ini");

		Configurations configurations =
			new INICommonsConfigurations(this.configDirectory);
		SourceFactory sf = new SourceFactory(configurations, configFilename);

		KnowledgeSource ks = sf.newKnowledgeSourceInstance();
		if (ks.getBackends().length == 0) {
			if (this.knowledgeSource == null) {
				this.knowledgeSource =
					createProxy(PreventCloseKnowledgeSource.class,
						defaultsSF.newKnowledgeSourceInstance());
			}
			ks = this.knowledgeSource;
		}

		DataSource ds = sf.newDataSourceInstance();
		if (ds.getBackends().length == 0) {
			if (this.dataSource == null) {
				this.dataSource =
					createProxy(PreventCloseDataSource.class,
						defaultsSF.newDataSourceInstance());
			}
			ds = this.dataSource;
		}

		AlgorithmSource as = sf.newAlgorithmSourceInstance();
		if (as.getBackends().length == 0) {
			if (this.algorithmSource == null) {
				this.algorithmSource =
					createProxy(PreventCloseAlgorithmSource.class,
						defaultsSF.newAlgorithmSourceInstance());
			}
			as = this.algorithmSource;
		}

		TermSource ts = sf.newTermSourceInstance();
		if (ts.getBackends().length == 0) {
			if (this.termSource == null) {
				this.termSource =
					createProxy(PreventCloseTermSource.class,
						defaultsSF.newTermSourceInstance());
			}
			ts = this.termSource;
		}

		return new Protempa(ds, ks, as, ts);
	}

	public void close() {
		if (this.knowledgeSource != null) {
			this.knowledgeSource.reallyClose();
		}
		if (this.dataSource != null) {
			this.dataSource.reallyClose();
		}
		if (this.algorithmSource != null) {
			this.algorithmSource.reallyClose();
		}
		if (this.termSource != null) {
			this.termSource.reallyClose();
		}
	}

	private static <E extends Source<?, ?, ?>> E createProxy(Class<E> clz,
		Source<?, ?, ?> proxied) {
		return clz.cast(Proxy.newProxyInstance(clz.getClassLoader(),
			new Class[]{clz}, new PreventCloseInvocationHandler(proxied)));
	}
}
