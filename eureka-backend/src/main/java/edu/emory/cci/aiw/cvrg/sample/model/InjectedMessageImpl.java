package edu.emory.cci.aiw.cvrg.sample.model;

/**
 * A sample class to show Guice injection into a Jersey resource.
 * @author hrathod
 *
 */
public class InjectedMessageImpl implements InjectedMessage {

    /**
     * This string will be returned by the <code>getMessage</code> method.
     */
    private String msg = "Hello, I'm an injected message!";

    /* (non-Javadoc)
     * @see edu.emory.cci.aiw.cvrg.sample.model.InjectedMessage#getMessage()
     */
    @Override
    public String getMessage() {
        return this.msg;
    }
}
