package edu.emory.cci.aiw.cvrg.eureka.servlet.filter;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
 * A naive algorithm for splitting a person's name. It simply splits a full
 * name by whitespace.
 * 
 * @author Andrew Post
 */
public class PersonNameSplitter {

	private final String fullName;
	private String firstName;
	private String lastName;

	public PersonNameSplitter(String fullName) {
		this.fullName = fullName;
		if (fullName != null) {
			String[] fullNameSplit = this.fullName.split(" ", 2);
			if (fullNameSplit.length >= 1) {
				firstName = fullNameSplit[0];
			}
			if (fullNameSplit.length == 2) {
				lastName = fullNameSplit[1];
			}
		}
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}
}
