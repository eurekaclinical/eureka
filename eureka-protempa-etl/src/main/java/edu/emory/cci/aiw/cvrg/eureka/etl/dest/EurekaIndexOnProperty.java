package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

import edu.emory.cci.aiw.neo4jetl.config.IndexOnProperty;

/**
 *
 * @author Andrew Post
 */
public class EurekaIndexOnProperty implements IndexOnProperty {
	private String propertyName;
	
	EurekaIndexOnProperty(String inPropertyName) {
		if (inPropertyName == null) {
			throw new IllegalArgumentException("inPropertyName cannot be null");
		}
		this.propertyName = inPropertyName;
	}

	@Override
	public String getPropertyName() {
		return this.propertyName;
	}
	
}
