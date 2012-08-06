package edu.emory.cci.aiw.cvrg.eureka.etl;


import edu.emory.cci.aiw.i2b2etl.I2B2QueryResultsHandler;
import java.io.File;
import org.protempa.*;
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

/**
 *
 * @author Andrew Post
 */
public class ETL {

    private static final String[] PROP_IDS = {
        "Patient",
        "PatientAll",
        "Encounter",
        "AttendingPhysician",
        "ProcedureCode",
        "LaboratoryTest",
        "Procedure",
        "VitalSign",
        "ICD9:Procedures",
        "ICD9:Diagnoses",
        "MED:medications",
        "LAB:LabTest",
        "CPTCode"
    };
    
    private static final File DEFAULT_CONF_DIR = new File("/opt/cvrg_users");
    
    private KnowledgeSource knowledgeSource = null;

    public void runProtempa(String userName) throws FinderException,
            QueryBuildException, ConfigurationsLoadException,
            BackendProviderSpecLoaderException, InvalidConfigurationException,
            ProtempaStartupException, BackendInitializationException,
            BackendNewInstanceException {
        File userDir = new File(DEFAULT_CONF_DIR, userName);
        File confXML = new File(userDir, "conf.xml");
        
        Protempa protempa = getNewProtempa(userDir, "erat-diagnoses-direct");

        try {
            DefaultQueryBuilder q = new DefaultQueryBuilder();
            q.setPropositionIds(PROP_IDS);
            Query query = protempa.buildQuery(q);
            QueryResultsHandler tdqrh = new I2B2QueryResultsHandler(confXML);
            protempa.execute(query, tdqrh);
        } finally {
            protempa.clear();
        }
    }

    private Protempa getNewProtempa(File userDir, String configId) throws
            ConfigurationsLoadException, BackendProviderSpecLoaderException,
            InvalidConfigurationException, ProtempaStartupException,
            BackendInitializationException, BackendNewInstanceException {
        
        Protempa protempa;
        Configurations configurations = new INICommonsConfigurations(userDir);
        SourceFactory sf = new SourceFactory(configurations, configId);
        if (knowledgeSource == null) {
            protempa = new Protempa(sf.newDataSourceInstance(), 
                    sf.newKnowledgeSourceInstance(), 
                    sf.newAlgorithmSourceInstance(), 
                    sf.newTermSourceInstance());
            knowledgeSource = protempa.getKnowledgeSource();
        } else {
            protempa = new Protempa(sf.newDataSourceInstance(), 
                    knowledgeSource, 
                    sf.newAlgorithmSourceInstance(), 
                    sf.newTermSourceInstance());
        }
        return protempa;
    }
}
