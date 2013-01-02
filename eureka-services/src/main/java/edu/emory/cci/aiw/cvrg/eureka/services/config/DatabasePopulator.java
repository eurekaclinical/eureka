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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;
import java.util.HashMap;
import java.util.Map;

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
	private final ThresholdsOperatorDao 
			thresholdOperatorDao;

	@Inject
	DatabasePopulator(TimeUnitDao inTimeUnitDao, RoleDao inRoleDao, 
			UserDao inUserDao, ValueComparatorDao inValueComparatorDao, 
			RelationOperatorDao inRelationOperatorDao,
			ThresholdsOperatorDao inMatchOperatorDao) {
		this.timeUnitDao = inTimeUnitDao;
		this.roleDao = inRoleDao;
		this.userDao = inUserDao;
		this.valueComparatorDao = inValueComparatorDao;
		this.relationOperatorDao = inRelationOperatorDao;
		this.thresholdOperatorDao = inMatchOperatorDao;
	}

	void doPopulateIfNeeded() {
		populateDefaultRolesAndUserIfNeeded();
		populateTimeUnitsIfNeeded();
		populateRelationOperatorsIfNeeded();
		populateValueComparatorsIfNeeded();
		populateThresholdOperatorsIfNeeded();
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
					Arrays.asList(researcherRole, adminRole, superuserRole));
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
	
	private static class ValueComparatorHolder {
		final ValueComparator valueComparator;
		final String complementName;
		final boolean requiresComplement;

		public ValueComparatorHolder(ValueComparator valueComparator, String complementName, boolean requiresComplement) {
			this.valueComparator = valueComparator;
			this.complementName = complementName;
			this.requiresComplement = requiresComplement;
		}
		
		
	}

	private void populateValueComparatorsIfNeeded() {
		Map<String, ValueComparatorHolder> vcMap = 
				new HashMap<String, ValueComparatorHolder>();
		for (ValueComparator vc : this.valueComparatorDao.getAll()) {
			vcMap.put(vc.getName(), 
					new ValueComparatorHolder(vc, 
					vc.getComplement().getName(), false));
		}
		this.createValueComparatorIfNeeded(vcMap, "=", "equals", 
				ValueComparator.Threshold.BOTH,
				Long.valueOf(3), "not=");
		this.createValueComparatorIfNeeded(vcMap, "not=", "not equals", 
				ValueComparator.Threshold.BOTH,
				Long.valueOf(4), "=");
		this.createValueComparatorIfNeeded(vcMap, ">", "greater than", 
				ValueComparator.Threshold.LOWER_ONLY,
				Long.valueOf(5), "<=");
		this.createValueComparatorIfNeeded(vcMap, ">=",
				"greater than or equal to", 
				ValueComparator.Threshold.LOWER_ONLY,
				Long.valueOf(6), "<");
		this.createValueComparatorIfNeeded(vcMap, "<", "less than", 
				ValueComparator.Threshold.UPPER_ONLY, 
				Long.valueOf(1), ">=");
		this.createValueComparatorIfNeeded(vcMap, "<=",
				"less than or equal to", 
				ValueComparator.Threshold.UPPER_ONLY,
				Long.valueOf(2), ">");
		
		for (ValueComparatorHolder vc : vcMap.values()) {
			ValueComparator valueComparator = vc.valueComparator;
			if (vc.requiresComplement) {
				valueComparator.setComplement(
						vcMap.get(vc.complementName).valueComparator);
			}
			this.valueComparatorDao.update(valueComparator);
		}
	}

	private void createValueComparatorIfNeeded(
			Map<String, ValueComparatorHolder> vcMap, 
			String name, String desc, 
			ValueComparator.Threshold side, Long rank, String complementName) {
		ValueComparatorHolder vc = vcMap.get(name);
		if (vc == null) {
			ValueComparator valueComparator = new ValueComparator();
			valueComparator.setName(name);
			valueComparator.setDescription(desc);
			valueComparator.setThreshold(side);
			valueComparator.setRank(rank);
			this.valueComparatorDao.create(valueComparator);
			vcMap.put(name, 
					new ValueComparatorHolder(valueComparator, complementName, 
					true));
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

	private void populateThresholdOperatorsIfNeeded() {
		createThresholdOperatorIfNeeded("any", "any");
		createThresholdOperatorIfNeeded("all", "all");
	}

	private void createThresholdOperatorIfNeeded(String name, 
			String desc) {
		ThresholdsOperator op = this.thresholdOperatorDao.getByName(name);
		if (op == null) {
			op = new ThresholdsOperator();
			op.setName(name);
			op.setDescription(desc);
			this.thresholdOperatorDao.create(op);
		}
	}
}
