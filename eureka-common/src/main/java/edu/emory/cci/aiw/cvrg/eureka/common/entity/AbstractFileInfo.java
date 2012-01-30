package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * An abstract base class to hold information found about an uploaded file
 * during the validation process.
 * 
 * @author hrathod
 * 
 */
@MappedSuperclass
public class AbstractFileInfo {

	/**
	 * The unique identifier for the file information.
	 */
	@Id
	@GeneratedValue
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
	private FileUpload fileUpload;

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
	public FileUpload getFileUpload() {
		return this.fileUpload;
	}

	/**
	 * Set the file upload object with which the information is associated.
	 * 
	 * @param inFileUpload THe file upload with which the information is
	 *            associated.
	 */
	public void setFileUpload(FileUpload inFileUpload) {
		this.fileUpload = inFileUpload;
	}
}