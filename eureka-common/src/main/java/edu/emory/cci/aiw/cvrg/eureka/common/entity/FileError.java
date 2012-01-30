package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds information about errors found in an uploaded file.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "file_errors")
public class FileError extends AbstractFileInfo {

	/**
	 * Simple constructor, calls super();
	 */
	public FileError() {
		super();
	}

}
