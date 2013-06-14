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
package edu.emory.cci.aiw.cvrg.eureka.common.ddl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

/**
 * Generates the SQL statements needed to create the tables used to persist
 * the
 * model for the application.
 *
 * @author hrathod
 */
public final class DdlGenerator {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER =
			LoggerFactory.getLogger(DdlGenerator.class);
	/**
	 * The dialect to produce the SQL for.
	 */
	private static final String DIALECT =
			"org.hibernate.dialect.Oracle10gDialect";
//	private static final String DIALECT =
//		"org.hibernate.dialect.H2Dialect";

	/**
	 * Prevent the utility class from being instantiated.
	 */
	private DdlGenerator() {
		// prevent instantiation.
	}

	/**
	 * Given a list of classes annotated with the {@link Entity}
	 * annotation, a
	 * Hibernate database dialect, and an output file name, generates the SQL
	 * needed to persist the given entities to a database and writes the
	 * SQL out to
	 * the given file.
	 *
	 * @param classes A list of classes to generate SQL for.
	 * @param outFile The location of the output file.
	 */
	private static void generate(List<Class<?>> classes, String outFile) {
		final Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.dialect", DIALECT);
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");

		for (Class<?> c : classes) {
			Entity annotation = c.getAnnotation(Entity.class);
			if (annotation != null) {
				configuration.addAnnotatedClass(c);
			} else {
				LOGGER.warn("{} is not annotated with @Entity", c.getName());
			}
		}

		SchemaExport export = new SchemaExport(configuration);
		export.setDelimiter(";");
		export.setOutputFile(outFile);
		export.execute(true, false, false, false);
	}

	/**
	 * Generate the SQL for entities used in ETL layer classes.
	 *
	 * @param outputFile The location of file where the SQL should be
	 *                   written.
	 */
	private static void generateBackendDdl(final String outputFile) {
		final List<Class<?>> backendClasses = new ArrayList<Class<?>>();
		backendClasses.add(JobEntity.class);
		backendClasses.add(JobEvent.class);
		backendClasses.add(EtlUser.class);
		backendClasses.add(EtlGroup.class);
		backendClasses.add(SourceConfigEntity.class);
		backendClasses.add(DestinationEntity.class);
		backendClasses.add(SourceConfigGroupMembership.class);
		backendClasses.add(DestinationGroupMembership.class);
		generate(backendClasses, outputFile);
	}

	/**
	 * Generate the SQL for entities used in service layer classes.
	 *
	 * @param outputFile The location of file where the SQL should be
	 *                   written.
	 */
	private static void generateServiceDdl(final String outputFile) {
		final List<Class<?>> serviceClasses = new ArrayList<Class<?>>();
		serviceClasses.add(User.class);
		serviceClasses.add(Role.class);
		serviceClasses.add(DataElementEntity.class);
		serviceClasses.add(SequenceEntity.class);
		serviceClasses.add(CategoryEntity.class);
		serviceClasses.add(SystemProposition.class);
		serviceClasses.add(Relation.class);
		serviceClasses.add(TimeUnit.class);
		serviceClasses.add(ExtendedDataElement.class);
		serviceClasses.add(PropertyConstraint.class);
		serviceClasses.add(ValueComparator.class);
		serviceClasses.add(RelationOperator.class);
		serviceClasses.add(CategoryEntity.class);
		serviceClasses.add(ValueThresholdEntity.class);
		serviceClasses.add(FrequencyEntity.class);
		serviceClasses.add(FrequencyType.class);
		serviceClasses.add(ThresholdsOperator.class);
		serviceClasses.add(ValueThresholdGroupEntity.class);
		generate(serviceClasses, outputFile);
	}

	/**
	 * @param args The first param is the file where the service layer DDL
	 *             should go, and the second parameter is where the ETL
	 *             layer DDL should go.
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println(
					"Please provide two parameters, " +
							"first for the service layer file and the " +
							"second" +
							" " +
							"for the ETL layer file.");
		} else {
			generateServiceDdl(args[0]);
			generateBackendDdl(args[1]);
		}
	}
}
