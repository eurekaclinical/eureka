/*
 * #%L
 * Eureka Services
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
