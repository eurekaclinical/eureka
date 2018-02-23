package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

/*
 * #%L
 * Eureka Common
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import org.eurekaclinical.eureka.client.comm.JobEvent;
import org.eurekaclinical.eureka.client.comm.JobStatus;

/**
 *
 * @author Andrew Post
 */
public class JobEventComparator implements Comparator<JobEvent>,
		Serializable {

	private static final long serialVersionUID = -1597150892714722679L;
	private static final Map<JobStatus, Integer> ORDER = 
			new EnumMap<>(JobStatus.class);

	static {
		ORDER.put(JobStatus.VALIDATING, 0);
		ORDER.put(JobStatus.VALIDATED, 1);
		ORDER.put(JobStatus.STARTED, 2);
		ORDER.put(JobStatus.WARNING, 3);
		ORDER.put(JobStatus.ERROR, 4);
		ORDER.put(JobStatus.COMPLETED, 5);
		ORDER.put(JobStatus.FAILED, 6);
	}

	@Override
	public int compare(JobEvent a, JobEvent b) {
		return ORDER.get(a.getStatus()).compareTo(ORDER.get(b.getStatus()));
	}
}
