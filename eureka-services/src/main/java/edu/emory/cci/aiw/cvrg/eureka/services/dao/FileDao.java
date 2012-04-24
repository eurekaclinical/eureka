package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;

/**
 * A data access object interface to work with uploaded file information in the
 * data store.
 *
 * @author hrathod
 *
 */
public interface FileDao extends Dao<FileUpload, Long> {

	/**
	 * Persist an uploaded file.
	 *
	 * @param fileUpload The uploaded file to persist.
	 */
//	public void save(FileUpload fileUpload);

	/**
	 * Get the uploaded file referenced by the unique identifier.
	 *
	 * @param inId The unique identifier for the upload file to fetch.
	 * @return The uploaded file, if a file with the given unique identifier
	 *         exists (with '200/OK' status code), or a '406/bad request' status
	 *         code otherwise.
	 */
//	public FileUpload get(Long inId);

	/**
	 * Refreshes the given file upload object from the database.
	 *
	 * @param fileUpload The object to refresh from the data base.
	 */
//	public void refresh(FileUpload fileUpload);

	/**
	 * Get all the file uploads for the user corresponding to the given unique
	 * identifier.
	 *
	 * @param userId The unique identifier for the user.
	 *
	 * @return A list of jobs corresponding to the user.
	 */
	public List<FileUpload> getByUserId(Long userId);
}
