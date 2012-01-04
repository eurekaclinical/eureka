package edu.emory.cci.aiw.cvrg.sample.resource;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import edu.emory.cci.aiw.cvrg.sample.config.GuiceTestContextListener;

/**
 * Test the ${@link GuiceResource} class.
 * 
 * @author hrathod
 * 
 */
public class GuiceResourceTest extends JerseyTest {

    /**
     * Default constructor, simply call super().
     */
    public GuiceResourceTest() {
        super((new WebAppDescriptor.Builder())
                .contextListenerClass(GuiceTestContextListener.class)
                .filterClass(GuiceFilter.class).contextPath("sample-webapp")
                .servletPath("/").build());
    }

    /**
     * Test the /api/guice/message call.
     */
    @Test
    public void testMessage() {
        WebResource resource = this.resource();
        String response = resource.path("/api/guice/message").get(String.class);
        Assert.assertEquals("TEST", response);

    }
}
