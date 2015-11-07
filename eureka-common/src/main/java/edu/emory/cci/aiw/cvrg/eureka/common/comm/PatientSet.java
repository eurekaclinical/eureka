package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author Andrew Post
 */
public class PatientSet {
	private String username;
	private String name;
	private Set<String> patients;

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

	public Set<String> getPatients() {
		return patients;
	}

	public void setPatients(Set<String> patients) {
		this.patients = patients;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + Objects.hashCode(this.username);
		hash = 53 * hash + Objects.hashCode(this.name);
		hash = 53 * hash + Objects.hashCode(this.patients);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PatientSet other = (PatientSet) obj;
		if (!Objects.equals(this.username, other.username)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.patients, other.patients)) {
			return false;
		}
		return true;
	}
	
	
	
}
