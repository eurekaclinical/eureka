package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.protempa.backend.BackendPropertyType;

/*
 * #%L
 * Eureka Common
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

/**
 *
 * @author Andrew Post
 */
public class SourceConfig {

	public static final Section[] EMPTY_SECTION_ARRAY = new Section[0];


	public static class Section {

		private String id;
		private String displayName;
		private SourceConfigOption[] options;

		public Section() {
			this.options = new SourceConfigOption[0];
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
		public SourceConfigOption[] getOptions() {
			return options.clone();
		}

		public void setOptions(SourceConfigOption[] options) {
			if (options == null) {
				this.options = new SourceConfigOption[0];
			} else {
				this.options = options.clone();
			}
		}
	}
	private String id;
	private String displayName;
	private String ownerUsername;
	private Section[] dataSourceBackends;
	private Section[] knowledgeSourceBackends;
	private Section[] algorithmSourceBackends;
	private Section[] termSourceBackends;
	private boolean read;
	private boolean write;
	private boolean execute;

	public SourceConfig() {
		this.dataSourceBackends = EMPTY_SECTION_ARRAY;
		this.knowledgeSourceBackends = EMPTY_SECTION_ARRAY;
		this.algorithmSourceBackends = EMPTY_SECTION_ARRAY;
		this.termSourceBackends = EMPTY_SECTION_ARRAY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isExecute() {
		return execute;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	public Section[] getDataSourceBackends() {
		return dataSourceBackends;
	}

	public void setDataSourceBackends(Section[] dataSourceBackends) {
		if (dataSourceBackends == null) {
			this.dataSourceBackends = EMPTY_SECTION_ARRAY;
		} else {
			this.dataSourceBackends = dataSourceBackends.clone();
		}
	}

	public Section[] getKnowledgeSourceBackends() {
		return knowledgeSourceBackends;
	}

	public void setKnowledgeSourceBackends(Section[] knowledgeSourceBackends) {
		if (knowledgeSourceBackends == null) {
			this.knowledgeSourceBackends = EMPTY_SECTION_ARRAY;
		} else {
			this.knowledgeSourceBackends = knowledgeSourceBackends.clone();
		}
	}

	public Section[] getAlgorithmSourceBackends() {
		return algorithmSourceBackends;
	}

	public void setAlgorithmSourceBackends(Section[] algorithmSourceBackends) {
		if (algorithmSourceBackends == null) {
			this.algorithmSourceBackends = EMPTY_SECTION_ARRAY;
		} else {
			this.algorithmSourceBackends = algorithmSourceBackends.clone();
		}
	}

	public Section[] getTermSourceBackends() {
		return termSourceBackends;
	}

	public void setTermSourceBackends(Section[] termSourceBackends) {
		if (termSourceBackends == null) {
			this.termSourceBackends = EMPTY_SECTION_ARRAY;
		} else {
			this.termSourceBackends = termSourceBackends.clone();
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
