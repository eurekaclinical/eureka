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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Date;
import java.util.List;
import org.protempa.proposition.interval.Interval.Side;

/**
 * Hold information about a user's file upload.
 * 
 * @author Andrew Post
 */
public class JobSpec {
	
	private String sourceConfigId;
	private String destinationId;
	private String dateRangeDataElementKey;
	private Date earliestDate;
	private Side earliestDateSide;
	private Date latestDate;
	private Side latestDateSide;
	private boolean updateData;
	private SourceConfig prompts;
	private List<String> propositionIds;

	public String getSourceConfigId() {
		return sourceConfigId;
	}

	public void setSourceConfigId(String sourceId) {
		this.sourceConfigId = sourceId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public String getDateRangeDataElementKey() {
		return dateRangeDataElementKey;
	}

	public void setDateRangeDataElementKey(String dataElementKey) {
		this.dateRangeDataElementKey = dataElementKey;
	}
	
	public Date getEarliestDate() {
		return earliestDate;
	}

	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = earliestDate;
	}

	public Side getEarliestDateSide() {
		return earliestDateSide;
	}

	public void setEarliestDateSide(Side earliestDateSide) {
		this.earliestDateSide = earliestDateSide;
	}
	
	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}
	
	public Side getLatestDateSide() {
		return latestDateSide;
	}

	public void setLatestDateSide(Side latestDateSide) {
		this.latestDateSide = latestDateSide;
	}

	public boolean isUpdateData() {
		return updateData;
	}

	public void setUpdateData(boolean updateData) {
		this.updateData = updateData;
	}

	public SourceConfig getPrompts() {
		return prompts;
	}

	public void setPrompts(SourceConfig prompts) {
		this.prompts = prompts;
	}

	public List<String> getPropositionIds() {
		return propositionIds;
	}

	public void setPropositionIds(List<String> propositionIds) {
		this.propositionIds = propositionIds;
	}
	
}
