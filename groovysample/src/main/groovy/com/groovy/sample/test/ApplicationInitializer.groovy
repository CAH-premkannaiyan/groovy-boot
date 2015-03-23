/*
 * 
 */
package com.groovy.sample.test

import java.net.InetAddress;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.groovy.sample.test.config.Constants;
import com.google.common.base.Joiner;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationInitializer.
 */
@ComponentScan(basePackages="com.groovy.sample")
@EnableAutoConfiguration(exclude = [MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class])
class ApplicationInitializer {

	/** The Constant log. */
	static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

	/** The env. */
	@Inject
	private Environment env;


	/**
	 * Main.
	 *
	 * @param args the args
	 * @return the java.lang. object
	 */
	static main(String[] args) {
		SpringApplication app = new SpringApplication(ApplicationInitializer.class);
		app.setShowBanner(false);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

		// Check if the selected profile has been set as argument.
		// if not the development profile will be added
		addDefaultProfile(app, source);
		addLiquibaseScanPackages();
		Environment env = app.run(args).getEnvironment();
		log.info("Access URLs:\n----------------------------------------------------------\n\t" +
				"Local: \t\thttp://127.0.0.1:{}\n\t" +
				"External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("server.port"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));

	}

	/**
	 * Set a default profile if it has not been set.
	 *
	 * @param app the app
	 * @param source the source
	 * @return the java.lang. object
	 */
	private static addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active")) {
			app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
		}
	}

	/**
	 * Set the liquibases.scan.packages to avoid an exception from ServiceLocator.
	 *
	 * @return the java.lang. object
	 */
	private static addLiquibaseScanPackages() {
		System.setProperty("liquibase.scan.packages", Joiner.on(",").join(
				"liquibase.change", "liquibase.database", "liquibase.parser",
				"liquibase.precondition", "liquibase.datatype",
				"liquibase.serializer", "liquibase.sqlgenerator", "liquibase.executor",
				"liquibase.snapshot", "liquibase.logging", "liquibase.diff",
				"liquibase.structure", "liquibase.structurecompare", "liquibase.lockservice",
				"liquibase.ext", "liquibase.changelog"));
	}
}