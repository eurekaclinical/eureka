package edu.emory.cci.aiw.cvrg.eureka.etl.dsb;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.DataSourceBackendInitializationException;
import org.protempa.backend.annotations.BackendInfo;
import org.protempa.backend.annotations.BackendProperty;
import org.protempa.backend.dsb.file.DelimitedFileDataSourceBackend;
import org.protempa.proposition.value.AbsoluteTimeGranularityUtil;

/**
 *
 * @author Andrew Post
 */
@BackendInfo(displayName = "Eureka Delimited File Data Source Backend")
public class EurekaDelimitedFileDataSourceBackend extends DelimitedFileDataSourceBackend implements EurekaFileDataSourceBackend {

	private final FileDataSourceBackendSupport fileDataSourceBackendSupport;
	private boolean defaultTimestampIsFileUpdated;
	private Date defaultDate;
	

	public EurekaDelimitedFileDataSourceBackend() {
		this.fileDataSourceBackendSupport = new FileDataSourceBackendSupport(nameForErrors());
		this.fileDataSourceBackendSupport.setDataFileDirectoryName("filename");
	}
	
	@BackendProperty(propertyName="defaultTimestamp")
	public void parseDefaultTimestamp(String timestampString) throws ParseException {
		if ("FILE_LAST_MODIFIED".equals(timestampString)) {
			this.defaultTimestampIsFileUpdated = true;
			this.defaultDate = null;
		} else {
			this.defaultTimestampIsFileUpdated = false;
			this.defaultDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).parse(timestampString);
		}
	}
	
	@Override
	protected Long getDefaultPositionPerFile(File file) throws IOException {
		if (this.defaultTimestampIsFileUpdated) {
			BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
            BasicFileAttributes readAttributes = fileAttributeView.readAttributes();
            return AbsoluteTimeGranularityUtil.asPosition(new Date(readAttributes.lastModifiedTime().toMillis()));
		} else if (this.defaultDate != null) {
			return this.defaultDate.getTime();
		} else {
			return super.getDefaultPositionPerFile(file);
		}
	}

	@BackendProperty(
			displayName = "Delimited file",
			validator = TextPlainBackendPropertyValidator.class,
			required = true
	)
	@Override
	public void setFilename(String filename) {
		this.fileDataSourceBackendSupport.setFilename(filename);
	}

	@Override
	public String getFilename() {
		return this.fileDataSourceBackendSupport.getFilename();
	}

	@Override
	public void initialize(BackendInstanceSpec config) throws BackendInitializationException {
		super.initialize(config);
		this.fileDataSourceBackendSupport.setConfigurationsId(getConfigurationsId());
		try {
			setFiles(this.fileDataSourceBackendSupport.getUploadedFiles());
		} catch (IOException ex) {
			throw new DataSourceBackendInitializationException("Error initializing data source backend " + nameForErrors(), ex);
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

}
