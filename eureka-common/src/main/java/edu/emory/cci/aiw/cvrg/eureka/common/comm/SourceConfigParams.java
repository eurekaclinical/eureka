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
public class SourceConfigParams {

	public static class Upload {
		private String name;
		private String sourceId;
		private String[] acceptedMimetypes;
		private String sampleUrl;
		private boolean required;

		public Upload() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getSourceId() {
			return sourceId;
		}

		public void setSourceId(String sourceId) {
			this.sourceId = sourceId;
		}

		public String[] getAcceptedMimetypes() {
			return acceptedMimetypes;
		}

		public void setAcceptedMimetypes(String[] acceptedMimetype) {
			this.acceptedMimetypes = acceptedMimetype;
		}

		public String getSampleUrl() {
			return sampleUrl;
		}

		public void setSampleUrl(String sampleUrl) {
			this.sampleUrl = sampleUrl;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}
		
	}
	private String id;
	private String name;
	private Upload[] uploads;

	public SourceConfigParams() {
		this.uploads = new Upload[0];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Upload[] getUploads() {
		return uploads.clone();
	}

	public void setUploads(Upload[] uploads) {
		if (uploads == null) {
			this.uploads = new Upload[0];
		} else {
			this.uploads = uploads.clone();
		}
	}
    
}
