/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.DatabaseSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs first-time populating of Eureka's database tables. It also tries to
 * replace deleted rows.
 *
 * @author Andrew Post
 */
class DatabasePopulator {

	private static Logger LOGGER =
			LoggerFactory.getLogger(DatabasePopulator.class);
	private final EntityManager entityManager;
	private final DatabaseSupport dbSupport;

	DatabasePopulator(EntityManager entityManager) {
		assert entityManager != null : "entityManager cannot be null";
		this.entityManager = entityManager;
		this.dbSupport = new DatabaseSupport(this.entityManager);
	}

	void doPopulateIfNeeded() {
		this.entityManager.getTransaction().begin();
		try {
			repairFileUploadsIfNeeded();
			this.entityManager.getTransaction().commit();
		} catch (Throwable t) {
			this.entityManager.getTransaction().rollback();
		}
	}

	private void repairFileUploadsIfNeeded() {
		Date now = new Date();
		int numUploadsRepaired = 0;
		for (FileUpload fileUpload : this.dbSupport.getAll(FileUpload.class)) {
			if (!fileUpload.isCompleted()) {
				if (numUploadsRepaired == 0) {
					LOGGER.warn(
							"Repairing file uploads table, probably because the "
							+ "application shut down during a processing run.");
				}
				LOGGER.warn("Repairing file upload {}", fileUpload.toString());
				fileUpload.setCompleted(true);
				fileUpload.setTimestamp(now);
				FileError fileError = new FileError();
				fileError.setText("Eureka! shut down during job.");
				fileError.setType("unknown");
				List<FileError> errors = new ArrayList<FileError>();
				errors.add(fileError);
				fileUpload.setErrors(errors);
				this.entityManager.merge(fileUpload);
				LOGGER.warn("After repair, the file upload's status is {}",
						fileUpload.toString());
				numUploadsRepaired++;
			}
		}
		if (numUploadsRepaired > 0) {
			LOGGER.warn("Repaired {} file upload(s).", numUploadsRepaired);
		}
	}
}
