package edu.emory.cci.aiw.cvrg.eureka.etl;


import edu.emory.cci.aiw.i2b2etl.I2B2QueryResultsHandler;
import org.protempa.*;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.InvalidConfigurationException;
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
    private KnowledgeSource knowledgeSource = null;

    public void runProtempa(String userName) throws FinderException,
            QueryBuildException, ConfigurationsLoadException,
            BackendProviderSpecLoaderException, InvalidConfigurationException,
            ProtempaStartupException, BackendInitializationException,
            BackendNewInstanceException {
        String confXML = "/opt/cvrg_users/" + userName + "/conf.xml";

        /*
         * We synchronize this because getNewProtempa uses the user.home
         * property to decide where to look for configuration files. If another
         * thread changes user.home, we're screwed.
         */
        Protempa protempa;
        synchronized (getClass()) {
            System.setProperty("user.home", "/opt/cvrg_users/" + userName);
            protempa = getNewProtempa("erat-diagnoses-direct");
        }

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

    private Protempa getNewProtempa(String fileName) throws
            ConfigurationsLoadException, BackendProviderSpecLoaderException,
            InvalidConfigurationException, ProtempaStartupException,
            BackendInitializationException, BackendNewInstanceException {
        
        Protempa protempa;
        SourceFactory sf = new SourceFactory(fileName);
        if (knowledgeSource == null) {

            protempa = new Protempa(sf.newDataSourceInstance(), sf.newKnowledgeSourceInstance(), sf.newAlgorithmSourceInstance(), sf.newTermSourceInstance());
            knowledgeSource = protempa.getKnowledgeSource();
        } else {

            protempa = new Protempa(sf.newDataSourceInstance(), knowledgeSource, sf.newAlgorithmSourceInstance(), sf.newTermSourceInstance());
        }
        return protempa;
    }
}
