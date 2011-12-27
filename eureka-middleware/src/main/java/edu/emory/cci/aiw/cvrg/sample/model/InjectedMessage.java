package edu.emory.cci.aiw.cvrg.sample.model;

/**
 * An interface to test the Guice injection into a Jersey Resource.
 * 
 * @author hrathod
 * 
 */
public interface InjectedMessage {

    /**
     * Get the message from this object.
     * 
     * @return The message contained by the object.
     */
    public String getMessage();

}