package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;

/**
 * A data access object interface to work with uploaded file information in the
 * data store.
 * 
 * @author hrathod
 * 
 */
public interface FileDao {

	/**
	 * Persist an uploaded file.
	 * 
	 * @param fileUpload The uploaded file to persist.
	 */
	public void save(FileUpload fileUpload);

	/**
	 * Get the uploaded file referenced by the unique identifier.
	 * 
	 * @param inId The unique identifier for the upload file to fetch.
	 * @return The uploaded file, if a file with the given unique identifier
	 *         exists (with '200/OK' status code), or a '406/bad request' status
	 *         code otherwise.
	 */
	public FileUpload get(Long inId);
}
