package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains information about warnings found in an uploaded file.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "file_warnings")
public class FileWarning extends AbstractFileInfo {
	/**
	 * Simple constructor, calls super()
	 */
	public FileWarning() {
		super();
	}

	@Override
	public void setFileUpload(FileUpload inFileUpload) {
		this.fileUpload = inFileUpload;
		if (!this.getFileUpload().getWarnings().contains(this)) {
			this.getFileUpload().addWarning(this);
		}
	}
}
