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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A communication class used by the service layer to send filter criteria to
 * the ETL layer for job status retrieval tasks.
 *
 * @author hrathod
 */
public class JobFilter {

	/**
	 * The unique identifier for the job to retrieve.
	 */
	private final Long jobId;
	/**
	 * The unique identifier for a user to whom the jobs should belong.
	 */
	private final Long userId;
	/**
	 * The state in which the jobs should be when retrieved.
	 */
	private final String state;
	/**
	 * Find jobs with timestamp after this time.
	 */
	private final Date from;
	/**
	 * Find jobs with timestamp before this time.
	 */
	private final Date to;

	/**
	 * Create an instance with the given parameters. Null parameters are
	 * allowed. The inFrom and inTo parameters can be used to create date range
	 * to search between.
	 *
	 * @param inJobId Search for a specific job using a unique identifier.
	 * @param inUserId The id of the user who requested the job.
	 * @param inState Search for all jobs in a given state.
	 * @param inFrom Search for all jobs with a timestamp after the given time.
	 * @param inTo Search for all jobs with a timestamp before the given time.
	 */
	public JobFilter(final Long inJobId, final Long inUserId,
			final String inState, final Date inFrom,
			final Date inTo) {

		this.jobId = inJobId;
		this.userId = inUserId;
		this.state = inState;
		this.from = (null == inFrom) ? null : new Date(inFrom.getTime());
		this.to = (null == inTo) ? null : new Date(inTo.getTime());
	}
	
	/**
	 * Creates an empty JobFilter object.
	 */

	/**
	 * Creates a filter by parsing the given string for the needed values. The
	 * string should be in a pipe-delimited format, with an equal sign
	 * separating the key and value. For example,
	 * "jobId=1|userId=1|state=STARTED|from=123456|to=123456".
	 *
	 * @param queryString Parse the filter from this given string.
	 */
	public JobFilter(String queryString) {
		Map<String, String> params = new HashMap<>();
		String[] parts = queryString.split("|");
		for (String part : parts) {
			String[] data = part.split("=");
			if (data.length > 1 && data[0] != null && data[1] != null) {
				params.put(data[0], data[1]);
			}
		}
		this.jobId = params.get("jobId") == null ? null : Long.valueOf(params.
				get("jobId"));
		this.userId = params.get("userId") == null ? null : Long.valueOf(params.
				get("userId"));
		this.state = params.get("state");
		this.from = params.get("from") == null ? null : new Date(Long.valueOf(params.
				get("from")));
		this.to = params.get("to") == null ? null : new Date(Long.valueOf(params.
				get("to")));
	}

	/**
	 * @return the jobId
	 */
	public final Long getJobId() {
		return this.jobId;
	}

	/**
	 * @return the userId
	 */
	public final Long getUserId() {
		return this.userId;
	}

	/**
	 * @return the state
	 */
	public final String getState() {
		return this.state;
	}

	/**
	 * @return the from
	 */
	public final Date getFrom() {
		return this.from == null ? null : new Date(this.from.getTime());
	}

	/**
	 * @return the to
	 */
	public final Date getTo() {
		return this.to == null ? null : new Date(this.to.getTime());
	}

	/**
	 * Convert the filter object into a string appropriate to be passed as a
	 * request parameter. This string representation can then be parsed by the
	 * String value constructor on the server side.
	 *
	 * @return A string representation of the filter.
	 */
	public final String toQueryParam() {
		StringBuilder builder = new StringBuilder();
		if (this.jobId != null) {
			builder.append("jobId=").append(this.jobId);
		}
		if (this.userId != null) {
			builder.append("|userId=").append(this.userId);
		}
		if (this.state != null) {
			builder.append("|state=").append(state);
		}
		if (this.from != null) {
			builder.append("|from=").append(from.getTime());
		}
		if (this.to != null) {
			builder.append("|to=").append(to.getTime());
		}
		return builder.toString();
	}
}
