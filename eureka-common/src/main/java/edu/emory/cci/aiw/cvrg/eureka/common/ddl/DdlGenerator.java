package edu.emory.cci.aiw.cvrg.eureka.common.ddl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Generates the SQL statements needed to create the tables used to persist the
 * model for the application.
 * 
 * @author hrathod
 * 
 */
public class DdlGenerator {

	/**
	 * Given a list of classes annotated with the {@link Entity} annotation, a
	 * Hibernate database dialect, and an output file name, generates the SQL
	 * needed to persist the given entities to a database and writes the SQL out
	 * to the given file.
	 * 
	 * @param classes A list of classes to generate SQL for.
	 * @param dialect The dialect (database type) to tailor the SQL for.
	 * @param outFile The location of the output file.
	 */
	private static void generate(List<Class<?>> classes, String dialect,
			String outFile) {
		final Configuration configuration = new Configuration();
		configuration.setProperty("hibernate.dialect", dialect);
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");

		for (Class<?> c : classes) {
			Entity annotation = c.getAnnotation(Entity.class);
			if (annotation != null) {
				configuration.addAnnotatedClass(c);
			} else {
				System.out.println("Class " + c.getName()
						+ " is not annotated as an @Entity");
			}
		}

		SchemaExport export = new SchemaExport(configuration);
		export.setDelimiter(";");
		export.setOutputFile(outFile);
		export.execute(true, false, false, false);
	}

	/**
	 * Generate the SQL for entities used in ETL layer classes.
	 */
	private static void generateBackendDdl() {
		final List<Class<?>> backendClasses = new ArrayList<Class<?>>();
		backendClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration.class);
		backendClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.Job.class);
		backendClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent.class);
		generate(backendClasses, "org.hibernate.dialect.Oracle10gDialect",
				"/tmp/test2.sql");
	}

	/**
	 * Generate the SQL for entities used in service layer classes.
	 */
	private static void generateServiceDdl() {
		final List<Class<?>> serviceClasses = new ArrayList<Class<?>>();
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.User.class);
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload.class);
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError.class);
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning.class);
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.Role.class);
		serviceClasses
				.add(edu.emory.cci.aiw.cvrg.eureka.common.entity.AbstractFileInfo.class);

		generate(serviceClasses, "org.hibernate.dialect.Oracle10gDialect",
				"/tmp/test.sql");
	}

	/**
	 * @param args Command line arguments to the application. <b>Note:</b> these
	 *            arguments are currently not utilized.
	 */
	public static void main(String[] args) {
		generateServiceDdl();
		generateBackendDdl();
	}
}
