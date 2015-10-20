package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

/**
 *
 * @author Andrew Post
 */
public class PatientSet {
	private String username;
	private String name;
	private List<String> patients;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPatients() {
		return patients;
	}

	public void setPatients(List<String> patients) {
		this.patients = patients;
	}
	
}
