package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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
	private static final Map<JobEventType, Integer> order = 
			new EnumMap<JobEventType, Integer>(JobEventType.class);

	static {
		order.put(JobEventType.VALIDATING, 0);
		order.put(JobEventType.VALIDATED, 1);
		order.put(JobEventType.STARTED, 2);
		order.put(JobEventType.WARNING, 3);
		order.put(JobEventType.ERROR, 4);
		order.put(JobEventType.COMPLETED, 5);
		order.put(JobEventType.FAILED, 6);
	}

	@Override
	public int compare(JobEvent a, JobEvent b) {
		return order.get(a.getState()).compareTo(order.get(b.getState()));
	}
}
