package edu.emory.cci.aiw.cvrg.eureka.etl.dsb;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;

/**
 *
 * @author Andrwe Post
 */
class FileDataSourceBackendSupport {

	private String dataFileDirectoryName;
	private String configurationsId;
	private String filename;
	private final String nameForErrors;

	FileDataSourceBackendSupport(String nameForErrors) {
		this.nameForErrors = nameForErrors;
	}

	String getConfigurationsId() {
		return configurationsId;
	}

	void setConfigurationsId(String configurationsId) {
		this.configurationsId = configurationsId;
	}

	String getDataFileDirectoryName() {
		return dataFileDirectoryName;
	}

	void setDataFileDirectoryName(String dataFileDirectoryName) {
		this.dataFileDirectoryName = dataFileDirectoryName;
	}

	String getFilename() {
		return filename;
	}

	void setFilename(String filename) {
		this.filename = filename;
	}

	File[] getUploadedFiles() {
		File dataFileDirectory = new EtlProperties().uploadedDirectory(this.configurationsId, this.dataFileDirectoryName);
		return new File[]{new File(dataFileDirectory, this.filename)};
	}

}