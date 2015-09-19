package edu.emory.cci.aiw.cvrg.eureka.common.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Andrew Post
 */
public abstract class BooleanValueMixin {
	
	@JsonCreator
	public BooleanValueMixin(@JsonProperty("boolean") Boolean val) {
		
	}

	@JsonIgnore
	public abstract Boolean getBoolean();
}
