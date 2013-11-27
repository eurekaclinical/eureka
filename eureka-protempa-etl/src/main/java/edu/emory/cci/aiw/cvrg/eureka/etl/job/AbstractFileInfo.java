/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.net.URI;
import java.text.MessageFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An abstract base class to hold information found about an uploaded file
 * during the validation process.
 * 
 * @author hrathod
 * 
 */
abstract public class AbstractFileInfo {
	static final MessageFormat USER_MSG_NO_LINE_NUM = 
			new MessageFormat("{0} during {1}: {2}");
	
	static final MessageFormat USER_MSG_WITH_LINE_NUM =
			new MessageFormat("{0} during {1}, line {2}: {3}");

	/**
	 * The line number where the information occurred.
	 */
	private Long lineNumber;
	/**
	 * The information text.
	 */
	private String text;
	/**
	 * The type for the information (i.e. "patient", "encounter", etc.)
	 */
	private String type;
	
	private URI uri;

	/**
	 * Simple constructor, call super();
	 */
	AbstractFileInfo() {
		super();
	}

	/**
	 * Get the line where the information occurred.
	 * 
	 * @return The line where the information occurred.
	 */
	Long getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Set the line where the information occurred.
	 * 
	 * @param inLineNumber The line where the information occurred.
	 */
	void setLineNumber(Long inLineNumber) {
		this.lineNumber = inLineNumber;
	}

	/**
	 * Get the text for the information.
	 * 
	 * @return The text for the information.
	 */
	String getText() {
		return this.text;
	}

	/**
	 * Set the text for the information.
	 * 
	 * @param inText The text for the information.
	 */
	void setText(String inText) {
		this.text = inText;
	}

	/**
	 * Get the type of the information.
	 * 
	 * @return The type of the information.
	 */
	String getType() {
		return this.type;
	}

	/**
	 * Set the type of the information.
	 * 
	 * @param inType The type of the information.
	 */
	void setType(String inType) {
		this.type = inType;
	}

	/**
	 * Get the file upload with which the information is associated.
	 * 
	 * @return The file upload with which the information is associated.
	 */
	URI getURI() {
		return this.uri;
	}

	/**
	 * Set the file upload object with which the information is associated.
	 * 
	 * @param inFileUpload THe file upload with which the information is
	 *            associated.
	 */
	void setURI(URI uri) {
		this.uri = uri;
	}
	
	abstract String toUserMessage();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
