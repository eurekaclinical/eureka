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

import javax.persistence.MappedSuperclass;

/**
 *
 * @author Andrew Post
 */
@MappedSuperclass
public abstract class GroupMembership {
	private boolean groupRead;
	
	private boolean groupWrite;
	
	private boolean groupExecute;
	
	public abstract String configName();
	
	public boolean isGroupRead() {
		return groupRead;
	}

	public void setGroupRead(boolean groupRead) {
		this.groupRead = groupRead;
	}

	public boolean isGroupWrite() {
		return groupWrite;
	}

	public void setGroupWrite(boolean groupWrite) {
		this.groupWrite = groupWrite;
	}

	public boolean isGroupExecute() {
		return groupExecute;
	}

	public void setGroupExecute(boolean groupExecute) {
		this.groupExecute = groupExecute;
	}
}
