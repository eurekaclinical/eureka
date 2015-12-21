/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import java.io.File;

import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.ToConfigFile;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Contains methods to fetch configuration information for the application.
 *
 * @author hrathod
 *
 */
@Singleton
public class EtlProperties extends AbstractProperties {
	
	/**
	 * Gets the size of the thread pool created to run Protempa tasks.
	 *
	 * @return The size of the thread pool.
	 */
	public int getTaskThreadPoolSize() {
		return this.getIntValue("eureka.etl.threadpool.size", 4);
	}
	
	public File getSourceConfigDirectory() throws IOException {
		File file = new File(getConfigDir(), "sourceconfig");
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File getDestinationConfigDirectory() throws IOException {
		File file = new File(getConfigDir(), "destconfig");
		Files.createDirectories(file.toPath());
		return file;
	}

	public File getUploadedDirectory() throws IOException {
		File file = new File(getConfigDir(), "etluploaded");
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File uploadedDirectory(String sourceId, String fileTypeId) throws IOException {
		File file = new File(new File(getUploadedDirectory(), 
				ToConfigFile.fromSourceConfigId(sourceId)), fileTypeId);
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File destinationConfigFile(String destId) throws IOException {
		return new File(getDestinationConfigDirectory(),
				ToConfigFile.fromDestId(destId));
	}
	
	public File getOutputDirectory() throws IOException {
		File file = new File(getConfigDir(), "etloutput");
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File outputDirectory(String destId, String fileTypeId) throws IOException {
		File file = new File(new File(getOutputDirectory(), 
				ToConfigFile.fromDestId(destId)), fileTypeId);
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File outputFileDirectory(String destId) throws IOException {
		File file = new File(new File(getOutputDirectory(), 
				ToConfigFile.fromDestId(destId)), "outputfile");
		Files.createDirectories(file.toPath());
		return file;
	}
	
	public File outputTempDirectory(String destId) throws IOException {
		File file = new File(new File(getOutputDirectory(), 
				ToConfigFile.fromDestId(destId)), "outputtemp");
		Files.createDirectories(file.toPath());
		return file;
	}

	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.etl.callbackserver");
	}

    public int getSearchLimit()
    {
        return this.getIntValue("eureka.jstree.searchlimit",200);
    }
}
