package edu.emory.cci.aiw.cvrg.eureka.common.comm;

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

/**
 *
 * @author Andrew Post
 */
public class SourceConfig {

	public static final Section[] EMPTY_SECTION_ARRAY = new Section[0];

	public static class Option {

		private String key;
		private Object value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	public static class Section {

		private String id;
		private String displayName;
		private Option[] options;

		public Section() {
			this.options = new Option[0];
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
		
		public Option[] getOptions() {
			return options.clone();
		}

		public void setOptions(Option[] options) {
			if (options == null) {
				this.options = new Option[0];
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
}
