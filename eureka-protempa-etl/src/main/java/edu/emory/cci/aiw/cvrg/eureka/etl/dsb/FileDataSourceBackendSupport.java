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
import java.io.FilenameFilter;
import org.arp.javautil.io.FileUtil;
import org.drools.util.StringUtils;
import org.protempa.DataSourceBackendCloseException;

/**
 *
 * @author Andrwe Post
 */
class FileDataSourceBackendSupport {

	private String dataFileDirectoryName;
	private String configurationsId;
	private final String nameForErrors;
	private String[] mimetypes;

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
	
	public String[] getMimetypes() {
		return mimetypes.clone();
	}

	public void setMimetypes(String[] mimetypes) {
		if (mimetypes == null) {
			this.mimetypes = StringUtils.EMPTY_STRING_ARRAY;
		} else {
			this.mimetypes = mimetypes.clone();
		}
	}

	File[] getUploadedFiles() {
		File dataFileDirectory = new EtlProperties().uploadedDirectory(this.configurationsId, this.dataFileDirectoryName);
		File[] dataFiles = dataFileDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String string) {
				return string.endsWith(".uploaded");
			}
		});
		return dataFiles;
	}

	void markProcessed(File dataFile) throws DataSourceBackendCloseException {
		try {
			if (!dataFile.renameTo(FileUtil.replaceExtension(dataFile, ".processed"))) {
				throw new DataSourceBackendCloseException("Error in data source backend " + this.nameForErrors + ": failed to mark data file " + dataFile.getAbsolutePath() + " as processed");
			}
		} catch (SecurityException se) {
			throw new DataSourceBackendCloseException("Error in data source backend " + this.nameForErrors + ": failed to mark data file " + dataFile.getAbsolutePath() + " as processed", se);
		}
	}

	void markFailed(File dataFile) throws DataSourceBackendCloseException {
		try {
			if (!dataFile.renameTo(FileUtil.replaceExtension(dataFile, ".failed"))) {
				throw new DataSourceBackendCloseException("Error in data source backend " + this.nameForErrors + ": could not mark data file " + dataFile.getAbsolutePath() + " as failed");
			}
		} catch (SecurityException se) {
			throw new DataSourceBackendCloseException("Error in data source backend " + this.nameForErrors + ": failed to mark data file " + dataFile.getAbsolutePath() + " as failed", se);
		}
	}
}
