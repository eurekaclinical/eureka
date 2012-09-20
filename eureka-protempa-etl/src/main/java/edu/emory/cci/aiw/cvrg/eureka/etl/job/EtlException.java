package edu.emory.cci.aiw.cvrg.eureka.etl.job;

public class EtlException extends Exception {
	public EtlException (Throwable inThrowable) {
		super(inThrowable.getMessage(), inThrowable);
	}
}
