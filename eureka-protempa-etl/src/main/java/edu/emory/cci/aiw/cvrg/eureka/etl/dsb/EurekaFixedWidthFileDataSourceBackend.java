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
import java.io.File;
import java.util.Set;
import java.util.logging.Logger;
import org.protempa.BackendCloseException;
import org.protempa.DataSourceBackendCloseException;
import org.protempa.DataSourceReadException;
import org.protempa.DataStreamingEventIterator;
import org.protempa.QuerySession;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.annotations.BackendInfo;
import org.protempa.backend.annotations.BackendProperty;
import org.protempa.backend.dsb.file.FixedWidthFileDataSourceBackend;
import org.protempa.backend.dsb.filter.Filter;
import org.protempa.dest.QueryResultsHandler;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
@BackendInfo(displayName = "Eureka Fixed Width File Data Source Backend")
public class EurekaFixedWidthFileDataSourceBackend extends FixedWidthFileDataSourceBackend implements EurekaFileDataSourceBackend {

	private FileDataSourceBackendSupport fileDataSourceBackendSupport;
	private String dataFileDirectoryName;
	private Boolean required;
	private boolean exceptionOccurred;

	public EurekaFixedWidthFileDataSourceBackend() {
		this.fileDataSourceBackendSupport = new FileDataSourceBackendSupport(nameForErrors());
	}

	@Override
	public String getDataFileDirectoryName() {
		return dataFileDirectoryName;
	}

	@BackendProperty
	@Override
	public void setDataFileDirectoryName(String dataFileDirectoryName) {
		this.dataFileDirectoryName = dataFileDirectoryName;
	}

	@Override
	public String[] getMimetypes() {
		return this.fileDataSourceBackendSupport.getMimetypes();
	}

	@BackendProperty
	@Override
	public void setMimetypes(String[] mimetypes) {
		this.fileDataSourceBackendSupport.setMimetypes(mimetypes);
	}

	@Override
	public Boolean isRequired() {
		return required;
	}

	@BackendProperty
	@Override
	public void setRequired(Boolean required) {
		if (required == null) {
			this.required = Boolean.FALSE;
		} else {
			this.required = required;
		}
	}

	@Override
	public void initialize(BackendInstanceSpec config) throws BackendInitializationException {
		super.initialize(config);
		if (this.dataFileDirectoryName != null) {
			this.fileDataSourceBackendSupport.setConfigurationsId(getConfigurationsId());
			this.fileDataSourceBackendSupport.setDataFileDirectoryName(this.dataFileDirectoryName);
			setFiles(this.fileDataSourceBackendSupport.getUploadedFiles());
		}

	}

	@Override
	public DataStreamingEventIterator<Proposition> readPropositions(Set<String> keyIds, Set<String> propIds, Filter filters, QuerySession qs, QueryResultsHandler queryResultsHandler) throws DataSourceReadException {
		try {
			DataStreamingEventIterator<Proposition> result = super.readPropositions(keyIds, propIds, filters, qs, queryResultsHandler);
			this.exceptionOccurred = false;
			return result;
		} catch (DataSourceReadException ex) {
			this.exceptionOccurred = true;
			throw ex;
		}
	}

	@Override
	public String getKeyType() {
		return "Patient";
	}

	@Override
	public String getKeyTypeDisplayName() {
		return "patient";
	}

	@Override
	public void close() throws BackendCloseException {
		BackendCloseException exceptionToThrow = null;
		for (File file : getFiles()) {
			if (!this.exceptionOccurred) {
				try {
					this.fileDataSourceBackendSupport.markProcessed(file);
				} catch (DataSourceBackendCloseException se) {
					if (exceptionToThrow == null) {
						exceptionToThrow = se;
					}
				}
			} else {
				try {
					this.fileDataSourceBackendSupport.markFailed(file);
				} catch (DataSourceBackendCloseException se) {
					if (exceptionToThrow == null) {
						exceptionToThrow = se;
					}
				}
			}
		}

		if (exceptionToThrow != null) {
			throw exceptionToThrow;
		}
	}

}
