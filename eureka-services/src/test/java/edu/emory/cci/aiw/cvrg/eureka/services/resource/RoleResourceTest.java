/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;
import org.eurekaclinical.common.comm.Role;

/**
 * Tests for the {@link RoleResource} class.
 *
 * @author hrathod
 *
 */
public class RoleResourceTest extends AbstractServiceResourceTest {

	/**
	 * Simply call super().
	 */
	public RoleResourceTest() {
		super();
	}

	/**
	 * Test that proper number of roles are returned from the resource.
	 */
	@Test
	public final void testRoleList() {
		WebResource webResource = this.resource();
		List<Role> roles = webResource.path("/api/protected/roles")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Role>>() {
					// Nothing to implement, used to hold returned data.
				});
		Assert.assertEquals(2, roles.size());
	}
}
