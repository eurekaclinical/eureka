/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.Injector;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import org.protempa.proposition.value.AbsoluteTimeUnit;

/**
 * Performs first-time populating of Eureka's database tables. It also tries to
 * replace deleted rows.
 *
 * @author Andrew Post
 */
class DatabasePopulator {

	private final Injector injector;

	DatabasePopulator(Injector injector) {
		assert injector != null : "injector cannot be null";
		this.injector = injector;
	}

	void doPopulateIfNeeded() {
		populateDefaultRolesAndUserIfNeeded();
		populateTimeUnitsIfNeeded();
		populateRelationOperatorsIfNeeded();
	}

	private void populateTimeUnitsIfNeeded() {
		Provider<EntityManager> emProvider =
				this.injector.getProvider(EntityManager.class);
		EntityManager entityManager = emProvider.get();

		entityManager.getTransaction().begin();
		TimeUnitDao timeUnitDao = this.injector.getInstance(TimeUnitDao.class);
		Set<String> namesSet = new HashSet<String>();
		for (edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit timeUnit :
				timeUnitDao.getAll()) {
			namesSet.add(timeUnit.getName());
		}
		createTimeUnitIfNeeded(namesSet,
				timeUnitDao,
				AbsoluteTimeUnit.DAY.getName(),
				AbsoluteTimeUnit.DAY.getPluralName());
		createTimeUnitIfNeeded(namesSet,
				timeUnitDao,
				AbsoluteTimeUnit.HOUR.getName(),
				AbsoluteTimeUnit.HOUR.getPluralName());
		createTimeUnitIfNeeded(namesSet,
				timeUnitDao,
				AbsoluteTimeUnit.MINUTE.getName(),
				AbsoluteTimeUnit.MINUTE.getPluralName());

		entityManager.getTransaction().commit();
	}

	private void populateDefaultRolesAndUserIfNeeded() {
		Provider<EntityManager> emProvider =
				this.injector.getProvider(EntityManager.class);
		EntityManager entityManager = emProvider.get();

		entityManager.getTransaction().begin();

		RoleDao roleDao = this.injector.getInstance(RoleDao.class);
		Role researcherRole = createOrGetRole(roleDao, "researcher");
		Role adminRole = createOrGetRole(roleDao, "admin");
		Role superuserRole = createOrGetRole(roleDao, "superuser");

		UserDao userDao = this.injector.getInstance(UserDao.class);

		String superuserEmail = "super.user@emory.edu";
		User superuser = userDao.getByName(superuserEmail);
		if (superuser == null) {
			superuser = new User();
			superuser.setActive(true);
			superuser.setEmail(superuserEmail);
			superuser.setFirstName("Super");
			superuser.setLastName("User");
			superuser.setOrganization("N/A");
			superuser.setVerified(true);
			try {
				superuser.setPassword(StringUtil.md5("defaultpassword"));
			} catch (NoSuchAlgorithmException ex) {
				throw new AssertionError(
						"Could not hash default superuser password: "
						+ ex.getMessage());
			}
			superuser.setRoles(
					Arrays.asList(
					new Role[]{researcherRole, adminRole, superuserRole}));
			userDao.create(superuser);
		}

		entityManager.getTransaction().commit();
	}

	private static void createTimeUnitIfNeeded(Set<String> namesSet,
			TimeUnitDao timeUnitDao, String name, String desc) {
		if (!namesSet.contains(name)) {
			edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit timeUnit =
					new edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit();
			timeUnit.setName(name);
			timeUnit.setDescription(desc);
			timeUnitDao.create(timeUnit);
		}
	}

	private static Role createOrGetRole(RoleDao roleDao, String name) {
		Role role = roleDao.getRoleByName(name);
		if (role == null) {
			role = new Role();
			role.setName(name);
			roleDao.create(role);
		}
		return role;
	}

	private void populateRelationOperatorsIfNeeded() {
		RelationOperatorDao relOpDao = 
				this.injector.getInstance(RelationOperatorDao.class);
		createRelationOperatorIfNeeded(relOpDao, "before", "before");
		createRelationOperatorIfNeeded(relOpDao, "after", "after");
	}

	private static void createRelationOperatorIfNeeded(
			RelationOperatorDao relOpDao, String name, String desc) {
		RelationOperator relOp = relOpDao.getByName(name);
		if (relOp == null) {
			relOp = new RelationOperator();
			relOp.setName(name);
			relOp.setDescription(desc);
			relOpDao.create(relOp);
		}
	}
}
