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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

/**
 * Holds information about errors found in an uploaded file.
 * 
 * @author hrathod
 * 
 */
class FileError extends AbstractFileInfo {

	/**
	 * Simple constructor, calls super();
	 */
	FileError() {
		super();
	}
	
	@Override
	String toUserMessage() {
		Long lineNumber = getLineNumber();
		if (lineNumber != null) {
			return USER_MSG_WITH_LINE_NUM.format(new Object[]{"Error", getType(), lineNumber, getText()});
		} else {
			return USER_MSG_NO_LINE_NUM.format(new Object[]{"Error", getType(), getText()});
		}
	}
}
