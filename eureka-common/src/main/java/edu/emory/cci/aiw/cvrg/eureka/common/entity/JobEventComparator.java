package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Andrew Post
 */
public class JobEventComparator implements Comparator<JobEvent>,
		Serializable {

	private static final long serialVersionUID = -1597150892714722679L;
	private static final Map<JobStatus, Integer> order = 
			new EnumMap<>(JobStatus.class);

	static {
		order.put(JobStatus.VALIDATING, 0);
		order.put(JobStatus.VALIDATED, 1);
		order.put(JobStatus.STARTED, 2);
		order.put(JobStatus.WARNING, 3);
		order.put(JobStatus.ERROR, 4);
		order.put(JobStatus.COMPLETED, 5);
		order.put(JobStatus.FAILED, 6);
	}

	@Override
	public int compare(JobEvent a, JobEvent b) {
		return order.get(a.getStatus()).compareTo(order.get(b.getStatus()));
	}
}
