package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

public class PropositionValidatorException extends Exception {
	public PropositionValidatorException(String  inMessage) {
		super(inMessage);
	}
	public PropositionValidatorException(Throwable inThrowable) {
		super(inThrowable);
	}
}
