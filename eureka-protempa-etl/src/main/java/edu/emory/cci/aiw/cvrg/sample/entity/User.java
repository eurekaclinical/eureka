package edu.emory.cci.aiw.cvrg.sample.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * An example JPA entity, representing a user in the system.
 * 
 * @author hrathod
 * 
 */
@Entity
public class User {

    /**
     * Contains the unique identifier for the user.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Contains the name of the user.
     */
    private String name;

    /**
     * Get the unique identifier for the user.
     * 
     * @return the unique identifier
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set the user's unique identifier.
     * 
     * @param id the user's unique identifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the user's name.
     * 
     * @return a string containing the user's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the user's name.
     * 
     * @param name a string containing the user's name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
