/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.sun.xml.bind.CycleRecoverable;

/**
 * An abstract base class to hold information found about an uploaded file
 * during the validation process.
 * 
 * @author hrathod
 * 
 */
@MappedSuperclass
abstract public class AbstractFileInfo implements CycleRecoverable {

	/**
	 * The unique identifier for the file information.
	 */
	@Id
	@SequenceGenerator(name = "FILEINFO_SEQ_GENERATOR",
			sequenceName = "FILEINFO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "FILEINFO_SEQ_GENERATOR")
	private Long id;
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
	/**
	 * The file upload object with which the information is associated.
	 */
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = FileUpload.class)
	@JoinColumn(name = "fileupload_id")
	protected FileUpload fileUpload;

	/**
	 * Simple constructor, call super();
	 */
	public AbstractFileInfo() {
		super();
	}

	/**
	 * Get the unique identifier for the information.
	 * 
	 * @return The unique identifier for the information.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the information.
	 * 
	 * @param inId The unique identifier for the information.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the line where the information occurred.
	 * 
	 * @return The line where the information occurred.
	 */
	public Long getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * Set the line where the information occurred.
	 * 
	 * @param inLineNumber The line where the information occurred.
	 */
	public void setLineNumber(Long inLineNumber) {
		this.lineNumber = inLineNumber;
	}

	/**
	 * Get the text for the information.
	 * 
	 * @return The text for the information.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Set the text for the information.
	 * 
	 * @param inText The text for the information.
	 */
	public void setText(String inText) {
		this.text = inText;
	}

	/**
	 * Get the type of the information.
	 * 
	 * @return The type of the information.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set the type of the information.
	 * 
	 * @param inType The type of the information.
	 */
	public void setType(String inType) {
		this.type = inType;
	}

	/**
	 * Get the file upload with which the information is associated.
	 * 
	 * @return The file upload with which the information is associated.
	 */
	@JsonBackReference("fileupload-messages")
	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	/**
	 * Set the file upload object with which the information is associated.
	 * 
	 * @param inFileUpload THe file upload with which the information is
	 *            associated.
	 */
	abstract public void setFileUpload(FileUpload inFileUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.xml.bind.CycleRecoverable#onCycleDetected(com.sun.xml.bind.
	 * CycleRecoverable.Context)
	 */
	@Override
	public Object onCycleDetected(Context inContext) {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.getType()).append("|")
				.append(this.getLineNumber()).append("|")
				.append(this.getText());
		return sBuilder.toString();
	}
}