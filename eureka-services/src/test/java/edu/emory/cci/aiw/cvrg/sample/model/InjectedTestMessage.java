package edu.emory.cci.aiw.cvrg.sample.model;

/**
 * An implementation of the {@link InjectedMessage} interface used for testing.
 * 
 * @author hrathod
 * 
 */
public class InjectedTestMessage implements InjectedMessage {

    @Override
    public String getMessage() {
        return "TEST";
    }

}
