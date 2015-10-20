package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;

/**
 *
 * @author Andrew Post
 */
public class PatientSetSenderSupport {
	public PatientSetSenderSupport() {
	}
	
	public String getOutputName(DestinationEntity inDestinationEntity) {
		String outputName = inDestinationEntity.getOutputName();
		if (outputName == null) {
			outputName = outputName(inDestinationEntity.getName());
		}
		return outputName;
	}
	
	public String getOutputName(String destinationId) {
		return outputName(destinationId);
	}
	
	private String outputName(String destinationName) {
		return destinationName + "_out";
	}
}
