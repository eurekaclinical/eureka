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

/**
 * The different types of job events that are recorded. They are defined as
 * follows:
 * <dl>
 *   <dt>VALIDATING</dt>
 *	 <dd>Job validation started.</dd>
 *   <dt>VALIDATED</dt>
 *   <dd>Job validation completed without errors detected.</dd>
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
public enum JobEventType {
	VALIDATING,
	VALIDATED,
	PROCESSING,
	COMPLETED,
	WARNING,
	ERROR,
	FAILED
    
}
