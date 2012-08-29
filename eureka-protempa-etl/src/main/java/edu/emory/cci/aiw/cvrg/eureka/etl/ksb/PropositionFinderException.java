package edu.emory.cci.aiw.cvrg.eureka.etl.ksb;

public class PropositionFinderException extends Exception {

	public PropositionFinderException(Throwable inThrowable) {
		super(inThrowable);
	}

	public PropositionFinderException (String inMessage) {
		super(inMessage);
	}
}
