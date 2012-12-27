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

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.protempa.proposition.value.AbsoluteTimeUnit;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueDefinitionMatchOperator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueDefinitionMatchOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

/**
 * Performs first-time populating of Eureka's database tables. It also tries to
 * replace deleted rows.
 *
 * @author Andrew Post
 */
class DatabasePopulator {
	
	private final TimeUnitDao timeUnitDao;
	private final RoleDao roleDao;
	private final UserDao userDao;
	private final ValueComparatorDao valueComparatorDao;
	private final RelationOperatorDao relationOperatorDao;
	private final ValueDefinitionMatchOperatorDao 
			valueDefinitionMatchOperatorDao;

	@Inject
	DatabasePopulator(TimeUnitDao inTimeUnitDao, RoleDao inRoleDao, 
			UserDao inUserDao, ValueComparatorDao inValueComparatorDao, 
			RelationOperatorDao inRelationOperatorDao,
			ValueDefinitionMatchOperatorDao inMatchOperatorDao) {
		this.timeUnitDao = inTimeUnitDao;
		this.roleDao = inRoleDao;
		this.userDao = inUserDao;
		this.valueComparatorDao = inValueComparatorDao;
		this.relationOperatorDao = inRelationOperatorDao;
		this.valueDefinitionMatchOperatorDao = inMatchOperatorDao;
	}

	void doPopulateIfNeeded() {
		populateDefaultRolesAndUserIfNeeded();
		populateTimeUnitsIfNeeded();
		populateRelationOperatorsIfNeeded();
		populateValueComparatorsIfNeeded();
		populateValueDefinitionMatchOperatorsIfNeeded();
	}

	private void populateTimeUnitsIfNeeded() {
		Set<String> namesSet = new HashSet<String>();
		for (TimeUnit timeUnit : this.timeUnitDao.getAll()) {
			namesSet.add(timeUnit.getName());
		}
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.DAY.getName(), AbsoluteTimeUnit.DAY
				.getPluralName());
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.HOUR.getName(), 
				AbsoluteTimeUnit.HOUR.getPluralName());
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.MINUTE.getName(), 
				AbsoluteTimeUnit.MINUTE.getPluralName());
	}

	private void populateDefaultRolesAndUserIfNeeded() {
		Role researcherRole = this.createOrGetRole("researcher");
		Role adminRole = this.createOrGetRole("admin");
		Role superuserRole = this.createOrGetRole("superuser");

		String superuserEmail = "super.user@emory.edu";
		User superuser = this.userDao.getByName(superuserEmail);
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
	}

	private void createTimeUnitIfNeeded(Set<String> namesSet, String name, 
			String desc) {
		if (!namesSet.contains(name)) {
			TimeUnit timeUnit =	new TimeUnit();
			timeUnit.setName(name);
			timeUnit.setDescription(desc);
			this.timeUnitDao.create(timeUnit);
		}
	}

	private void populateValueComparatorsIfNeeded() {
		Set<String> namesSet = new HashSet<String>();
		for (ValueComparator vc : this.valueComparatorDao.getAll()) {
			namesSet.add(vc.getName());
		}
		this.createValueComparatorIfNeeded(namesSet, "=", "equals");
		this.createValueComparatorIfNeeded(namesSet, "not=", "not equals");
		this.createValueComparatorIfNeeded(namesSet, ">", "greater than");
		this.createValueComparatorIfNeeded(namesSet, ">=",
				"greater than or equal to");
		this.createValueComparatorIfNeeded(namesSet, "<", "less than");
		this.createValueComparatorIfNeeded(namesSet, "<=",
				"less than or equal to");

		ValueComparator eq = this.valueComparatorDao.getByName("=");
		ValueComparator ne = this.valueComparatorDao.getByName("not=");
		eq.setComplement(ne);
		ne.setComplement(eq);
		this.valueComparatorDao.update(eq);
		this.valueComparatorDao.update(ne);

		ValueComparator gt = this.valueComparatorDao.getByName(">");
		ValueComparator lte = this.valueComparatorDao.getByName("<=");
		gt.setComplement(lte);
		lte.setComplement(gt);
		this.valueComparatorDao.update(gt);
		this.valueComparatorDao.update(lte);

		ValueComparator lt = this.valueComparatorDao.getByName("<");
		ValueComparator gte = this.valueComparatorDao.getByName(">=");
		lt.setComplement(gte);
		gte.setComplement(lt);
		this.valueComparatorDao.update(lt);
		this.valueComparatorDao.update(gte);
	}

	private void createValueComparatorIfNeeded(Set<String> namesSet, 
			String name, String desc) {
		if (!namesSet.contains(name)) {
			ValueComparator valueComparator = new ValueComparator();
			valueComparator.setName(name);
			valueComparator.setDescription(desc);
			this.valueComparatorDao.create(valueComparator);
		}
	}

	private Role createOrGetRole(String name) {
		Role role = this.roleDao.getRoleByName(name);
		if (role == null) {
			role = new Role();
			role.setName(name);
			this.roleDao.create(role);
		}
		return role;
	}

	private void populateRelationOperatorsIfNeeded() {
		this.createRelationOperatorIfNeeded("before", "before");
		this.createRelationOperatorIfNeeded("after", "after");
	}

	private void createRelationOperatorIfNeeded(String name, String desc) {
		RelationOperator relOp = this.relationOperatorDao.getByName(name);
		if (relOp == null) {
			relOp = new RelationOperator();
			relOp.setName(name);
			relOp.setDescription(desc);
			this.relationOperatorDao.create(relOp);
		}
	}

	private void populateValueDefinitionMatchOperatorsIfNeeded() {
		createValueDefinitionMatchOperatorIfNeeded("any", "any");
		createValueDefinitionMatchOperatorIfNeeded("all", "all");
	}

	private void createValueDefinitionMatchOperatorIfNeeded(String name, 
			String desc) {
		ValueDefinitionMatchOperator op = this
				.valueDefinitionMatchOperatorDao.getByName(name);
		if (op == null) {
			op = new ValueDefinitionMatchOperator();
			op.setName(name);
			op.setDescription(desc);
			this.valueDefinitionMatchOperatorDao.create(op);
		}
	}
}
