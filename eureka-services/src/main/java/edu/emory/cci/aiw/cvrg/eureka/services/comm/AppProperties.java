package edu.emory.cci.aiw.cvrg.eureka.services.comm;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2016 Emory University
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
 *
 * @author Miao Ai
 */
public class AppProperties {

	private AppPropertiesModes modes;
	private AppPropertiesLinks links;
	private AppPropertiesRegistration registration;

	/**
	 * Constructor.
	 *
	 */
	public AppProperties() {
	}

	/**
	 * Gets the AppPropertiesModes.
	 *
	 * @return modes.
	 */
	public AppPropertiesModes getAppPropertiesModes() {
		return this.modes;
	}

	/**
	 * Gets the AppPropertiesLinks.
	 *
	 * @return links.
	 */
	public AppPropertiesLinks getAppPropertiesLinks() {
		return this.links;
	}

	/**
	 * Sets the modes status.
	 *
	 * @param inModes
	 */
	public void setAppPropertiesModes(AppPropertiesModes inModes) {
		this.modes = inModes;
	}

	/**
	 * Sets the links status.
	 *
	 * @param inLinks
	 */
	public void setAppPropertiesLinks(AppPropertiesLinks inLinks) {
		this.links = inLinks;
	}

	/**
	 * 	Sets the registration status. 	
	 * 	@param inRegistration Gets the AppPropertiesRegistration. 
	 *	@return registration.
	 */
	public void setAppPropertiesRegistration(AppPropertiesRegistration inRegistration) {
		this.registration = inRegistration;
	}

	/**
	 * Gets the AppPropertiesRegistration.
	 *
	 * @return registration.
	 */
	public AppPropertiesRegistration getAppPropertiesRegistration() {
		return this.registration;
	}

}
