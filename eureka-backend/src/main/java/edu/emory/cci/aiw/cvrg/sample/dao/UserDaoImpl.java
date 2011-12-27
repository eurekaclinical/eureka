package edu.emory.cci.aiw.cvrg.sample.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.sample.entity.User;

/**
 * An implementation of the {@link UserDao} interface.
 * 
 * @author hrathod
 * 
 */
public class UserDaoImpl implements UserDao {

    /**
     * Used for data access.
     */
    private final EntityManager entityManager;

    /**
     * Create a new User DAO with the given entity manager.
     * 
     * @param manager The entity manager to be used for data access.
     */
    @Inject
    public UserDaoImpl(EntityManager manager) {
        this.entityManager = manager;

        /*
         * Bootstrap with a couple of user objects to retrieve later.
        this.entityManager.getTransaction().begin();

        final User u1 = new User();
        u1.setName("John Doe");
        this.entityManager.persist(u1);

        final User u2 = new User();
        u2.setName("Jane Doe");
        this.entityManager.persist(u2);

        this.entityManager.getTransaction().commit();
         */
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.emory.cci.aiw.cvrg.sample.dao.UserDao#getUsers()
     */
    @Override
    public List<User> getUsers() {
        final Query query = this.entityManager
                .createQuery("select u from User u");
        @SuppressWarnings("unchecked")
        final List<User> resultList = query.getResultList();
        return resultList;
    }

}
