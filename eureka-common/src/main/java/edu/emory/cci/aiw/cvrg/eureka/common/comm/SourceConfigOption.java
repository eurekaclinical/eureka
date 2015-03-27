package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.protempa.backend.BackendPropertyType;

/**
 *
 * @author Andrew Post
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DefaultSourceConfigOption.class, name = "DEFAULT"),
        @JsonSubTypes.Type(value = FileSourceConfigOption.class, name = "FILE"),
		@JsonSubTypes.Type(value = UriSourceConfigOption.class, name = "URI")
})
public abstract class SourceConfigOption {
	private String name;
	private Object value;
	private String displayName;
	private String description;
	private boolean required;
	private BackendPropertyType propertyType;
	private boolean prompt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public BackendPropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(BackendPropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrompt(boolean prompt) {
		this.prompt = prompt;
	}
	
	public boolean isPrompt() {
		return this.prompt;
	}
	
}
