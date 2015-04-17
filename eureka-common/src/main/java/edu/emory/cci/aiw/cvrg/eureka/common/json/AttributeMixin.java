package edu.emory.cci.aiw.cvrg.eureka.common.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.protempa.proposition.value.Value;

/**
 *
 * @author Andrew Post
 */
public abstract class AttributeMixin {
	@JsonCreator
    public AttributeMixin(@JsonProperty("name") String name, @JsonProperty("value") Value value) { }
}
