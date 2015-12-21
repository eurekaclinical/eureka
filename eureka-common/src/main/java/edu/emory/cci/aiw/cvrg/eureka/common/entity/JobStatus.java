package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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

/**
 * The different types of job events that are recorded. They are defined as
 * follows:
 * <dl>
 *	 <dt>STARTING</dt>
 *   <dd>Job just submitted</dd>
 *   <dt>VALIDATING</dt>
 *	 <dd>Job validation started (deprecated, no longer used).</dd>
 *   <dt>VALIDATED</dt>
 *   <dd>Job validation completed without errors detected (deprecated, no longer used).</dd>
 *   <dt>PROCESSING</dt>
 *   <dd>Job processing started.</dd>
 *   <dt>COMPLETED</dt>
 *   <dd>Job processing completed without errors detected.</dd>
 *   <dt>WARNING</dt>
 *	 <dd>A warning occurred during validation or processing. Does not by itself cause a job to fail.</dd>
 *   <dt>ERROR</dt>
 *   <dd>An error occurred during validation or processing. A user-facing description is provided in the message field. A stack trace is recorded, if one is generated, for debugging.</dd>
 *   <dt>FAILED</dt>
 *   <dd>The job failed due to one or more errors.</dd>
 * </dl>
 * @author Andrew Post
 */
public enum JobStatus {
	STARTING,
	VALIDATING,
	VALIDATED,
	STARTED,
	COMPLETED,
	WARNING,
	ERROR,
	FAILED
    
}
