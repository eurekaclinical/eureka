/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.DatabaseSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.protempa.proposition.value.AbsoluteTimeUnit;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs first-time populating of Eureka's database tables. It also tries to
 * replace deleted rows.
 *
 * @author Andrew Post
 */
class DatabasePopulator {

	private static Logger LOGGER =
			LoggerFactory.getLogger(DatabasePopulator.class);
	private final EntityManager entityManager;
	private final DatabaseSupport dbSupport;

	DatabasePopulator(EntityManager entityManager) {
		assert entityManager != null : "entityManager cannot be null";
		this.entityManager = entityManager;
		this.dbSupport = new DatabaseSupport(this.entityManager);
	}

	void doPopulateIfNeeded() {
//		this.entityManager.getTransaction().begin();
//		try {
//			populateDefaultRolesAndUserIfNeeded();
//			this.entityManager.getTransaction().commit();
//		} catch (Throwable t) {
//			this.entityManager.getTransaction().rollback();
//		}
//		this.entityManager.getTransaction().begin();
//		try {
//			populateTimeUnitsIfNeeded();
//			this.entityManager.getTransaction().commit();
//		} catch (Throwable t) {
//			this.entityManager.getTransaction().rollback();
//		}
//		this.entityManager.getTransaction().begin();
//		try {
//			populateRelationOperatorsIfNeeded();
//			this.entityManager.getTransaction().commit();
//		} catch (Throwable t) {
//			this.entityManager.getTransaction().rollback();
//		}
//		this.entityManager.getTransaction().begin();
//		try {
//			populateValueComparatorsIfNeeded();
//			this.entityManager.getTransaction().commit();
//		} catch (Throwable t) {
//			this.entityManager.getTransaction().rollback();
//		}
//		this.entityManager.getTransaction().begin();
//		try {
//			populateThresholdOperatorsIfNeeded();
//			this.entityManager.getTransaction().commit();
//		} catch (Throwable t) {
//			this.entityManager.getTransaction().rollback();
//		}
		this.entityManager.getTransaction().begin();
		try {
			repairFileUploadsIfNeeded();
			this.entityManager.getTransaction().commit();
		} catch (Throwable t) {
			this.entityManager.getTransaction().rollback();
		}
	}

	private void populateTimeUnitsIfNeeded() {
		Set<String> namesSet = new HashSet<String>();
		for (TimeUnit timeUnit : this.dbSupport.getAll(TimeUnit.class)) {
			namesSet.add(timeUnit.getName());
		}
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.DAY.getName(), AbsoluteTimeUnit.DAY
				.getPluralName(), 3);
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.HOUR.getName(),
				AbsoluteTimeUnit.HOUR.getPluralName(), 2);
		this.createTimeUnitIfNeeded(
				namesSet, AbsoluteTimeUnit.MINUTE.getName(),
				AbsoluteTimeUnit.MINUTE.getPluralName(), 1);
	}

	private void repairFileUploadsIfNeeded() {
		Date now = new Date();
		int numUploadsRepaired = 0;
		for (FileUpload fileUpload : this.dbSupport.getAll(FileUpload.class)) {
			if (!fileUpload.isCompleted()) {
				if (numUploadsRepaired == 0) {
					LOGGER.warn(
							"Repairing file uploads table, probably because the "
							+ "application shut down during a processing run.");
				}
				LOGGER.warn("Repairing file upload {}", fileUpload.toString());
				fileUpload.setCompleted(true);
				fileUpload.setTimestamp(now);
				FileError fileError = new FileError();
				fileError.setText("Eureka! shut down during job.");
				fileError.setType("unknown");
				List<FileError> errors = new ArrayList<FileError>();
				errors.add(fileError);
				fileUpload.setErrors(errors);
				this.entityManager.merge(fileUpload);
				LOGGER.warn("After repair, the file upload's status is {}",
						fileUpload.toString());
				numUploadsRepaired++;
			}
		}
		if (numUploadsRepaired > 0) {
			LOGGER.warn("Repaired {} file upload(s).", numUploadsRepaired);
		}
	}

	private void populateDefaultRolesAndUserIfNeeded() {
		Role researcherRole = this.createOrGetRole("researcher");
		Role adminRole = this.createOrGetRole("admin");
		Role superuserRole = this.createOrGetRole("superuser");

		String superuserEmail = "super.user@emory.edu";
		User superuser =
				this.dbSupport.getUniqueByAttribute(User.class, User_.email,
				superuserEmail);
		if (superuser == null) {
			superuser = new User();
			superuser.setActive(true);
			superuser.setEmail(superuserEmail);
			superuser.setFirstName("Super");
			superuser.setLastName("User");
			superuser.setOrganization("N/A");
			superuser.setVerified(true);
			superuser.setPasswordExpiration(Calendar.getInstance().getTime());
			try {
				superuser.setPassword(StringUtil.md5("defaultpassword"));
			} catch (NoSuchAlgorithmException ex) {
				throw new AssertionError(
						"Could not hash default superuser password: "
						+ ex.getMessage());
			}
			superuser.setRoles(
					Arrays.asList(researcherRole, adminRole, superuserRole));
			this.entityManager.persist(superuser);
		}
	}

	private void createTimeUnitIfNeeded(Set<String> namesSet, String name,
			String desc, int rank) {
		if (!namesSet.contains(name)) {
			TimeUnit timeUnit = new TimeUnit();
			timeUnit.setName(name);
			timeUnit.setDescription(desc);
			timeUnit.setRank(rank);
			this.entityManager.persist(timeUnit);
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
		for (ValueComparator vc :
				this.dbSupport.getAll(ValueComparator.class)) {
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
			this.entityManager.merge(valueComparator);
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
			this.entityManager.persist(valueComparator);
			vcMap.put(name,
					new ValueComparatorHolder(valueComparator, complementName,
					true));
		}
	}

	private Role createOrGetRole(String name) {
		Role role = this.dbSupport.getUniqueByAttribute(
				Role.class, Role_.name, name);
		if (role == null) {
			role = new Role();
			role.setName(name);
			this.entityManager.persist(role);
		}
		return role;
	}

	private void populateRelationOperatorsIfNeeded() {
		this.createRelationOperatorIfNeeded("before", "before");
		this.createRelationOperatorIfNeeded("after", "after");
	}

	private void createRelationOperatorIfNeeded(String name, String desc) {
		RelationOperator relOp = this.dbSupport.getUniqueByAttribute(
				RelationOperator.class, RelationOperator_.name, name);
		if (relOp == null) {
			relOp = new RelationOperator();
			relOp.setName(name);
			relOp.setDescription(desc);
			this.entityManager.persist(relOp);
		}
	}

	private void populateThresholdOperatorsIfNeeded() {
		createThresholdOperatorIfNeeded("any", "any");
		createThresholdOperatorIfNeeded("all", "all");
	}

	private void createThresholdOperatorIfNeeded(String name,
			String desc) {
		ThresholdsOperator op = this.dbSupport.getUniqueByAttribute(
				ThresholdsOperator.class, ThresholdsOperator_.name, name);
		if (op == null) {
			op = new ThresholdsOperator();
			op.setName(name);
			op.setDescription(desc);
			this.entityManager.persist(op);
		}
	}
}
