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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 * Hold information about a user's file upload.
 * 
 * @author Andrew Post
 */
public class FileUpload {
	private String location;
	private Long userId;

	/**
	 * Get the on-disk location of the file upload.
	 *
	 * @return The on-disk location of the file upload.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Set the on-desk location of the file upload.
	 *
	 * @param location The on-disk location of the file upload.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Get the user to which the file upload belongs.
	 *
	 * @return The user to which the file upload belongs.
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user to which the file upload belongs.
	 *
	 * @param userId The user to which the file upload belongs.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
