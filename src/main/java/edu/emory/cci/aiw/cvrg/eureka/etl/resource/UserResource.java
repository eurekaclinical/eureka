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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import com.google.inject.persist.Transactional;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.AuthorizedUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.AuthorizedRoleEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.RoleDao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eurekaclinical.common.comm.User;
import org.eurekaclinical.common.resource.AbstractUserResource;

/**
 * RESTful end-point for {@link UserEntity} related methods.
 *
 * @author hrathod
 */
@Transactional
@Path("/protected/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"admin"})
public class UserResource extends AbstractUserResource<User, AuthorizedUserEntity, AuthorizedRoleEntity> {

	private final RoleDao roleDao;
	
	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 *
	 * @param inUserDao DAO used to access {@link UserEntity} related functionality.
	 * @param inRoleDao
	 */
	@Inject
	public UserResource(AuthorizedUserDao inUserDao, RoleDao inRoleDao) {
		super(inUserDao);
		this.roleDao = inRoleDao;
	}

	@Override
    protected User toComm(AuthorizedUserEntity userEntity, HttpServletRequest request) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        List<Long> roles = new ArrayList<>();
        for (AuthorizedRoleEntity roleEntity : userEntity.getRoles()) {
            roles.add(roleEntity.getId());
        }
        user.setRoles(roles);
        return user;
    }
    
    @Override
    protected AuthorizedUserEntity toEntity(User user) {
        List<AuthorizedRoleEntity> roleEntities = this.roleDao.getAll();
        AuthorizedUserEntity userEntity = new AuthorizedUserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        List<AuthorizedRoleEntity> userRoleEntities = new ArrayList<>();
        for (Long roleId : user.getRoles()) {
            for (AuthorizedRoleEntity roleEntity : roleEntities) {
                if (roleEntity.getId().equals(roleId)) {
                    userRoleEntities.add(roleEntity);
                }
            }
        }
        userEntity.setRoles(userRoleEntities);
        return userEntity;
    }

}
