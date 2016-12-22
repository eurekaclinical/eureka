package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.patientset.client.EurekaClinicalPatientSetClient;

/**
 *
 * @author Andrew Post
 */
public class PatientSetSender {

	private final EtlProperties etlProperties;
	private final FileSupport fileSupport;
	private final EurekaClinicalPatientSetClient patientSetClient;
	private final PatientSetSenderDestinationEntity patientSetSenderDestinationEntity;

	PatientSetSender(EtlProperties inEtlProperties, PatientSetSenderDestinationEntity inPatientSetSenderDestinationEntity) {
		assert inEtlProperties != null : "inEtlProperties cannot be null";
		assert inPatientSetSenderDestinationEntity != null : "inPatientSetSenderDestinationEntity cannot be null";
		this.fileSupport = new FileSupport();
		this.etlProperties = inEtlProperties;
		this.patientSetSenderDestinationEntity = inPatientSetSenderDestinationEntity;
		this.patientSetClient = new EurekaClinicalPatientSetClient(this.patientSetSenderDestinationEntity.getPatientSetService());
	}

	void doSend() throws IOException {
		File file = new File(this.etlProperties.outputFileDirectory(this.patientSetSenderDestinationEntity.getName()), this.fileSupport.getOutputName(this.patientSetSenderDestinationEntity));
		try (InputStream in = new FileInputStream(file)) {
			patientSetClient.postStreaming(in);
		} catch (ClientException ex) {
			throw new IOException(ex);
		}
	}
	
}
