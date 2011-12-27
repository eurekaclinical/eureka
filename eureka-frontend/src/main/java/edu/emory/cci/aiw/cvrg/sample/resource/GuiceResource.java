package edu.emory.cci.aiw.cvrg.sample.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.sample.dao.UserDao;
import edu.emory.cci.aiw.cvrg.sample.entity.User;
import edu.emory.cci.aiw.cvrg.sample.model.InjectedMessage;

/**
 * Test the Guice constructor injection.
 * 
 * @author hrathod
 */
@Path("/guice")
public class GuiceResource {

    /**
     * Hold the injected instance of a message.
     */
    private final InjectedMessage injectedMessage;

    /**
     * Hold the injected data access object.
     */
    private final UserDao userDao;

    /**
     * Constructor using a Guice injection of a message.
     * 
     * @param message Message injected by Guice.
     * @param dao the data access object used to interact with the data store
     *            for operations dealing with {@link User} objects.
     */
    @Inject
    public GuiceResource(InjectedMessage message, UserDao dao) {
        this.injectedMessage = message;
        this.userDao = dao;
    }

    /**
     * Return the message contained in the current instance.
     * 
     * @return A string containing the message.
     */
    @GET
    @Path("/message")
    public String getMessage() {
        return this.injectedMessage.getMessage();
    }

    /**
     * Return a list of users found in the database.
     * 
     * @return A comma separated list of users.
     */
    @GET
    @Path("/users")
    public String getUsers() {
        List<User> users = this.userDao.getUsers();
        StringBuilder stringBuilder = new StringBuilder();
        for (User u : users) {
            stringBuilder.append(u.getName()).append(", ");
        }
        return stringBuilder.toString();
    }
}
