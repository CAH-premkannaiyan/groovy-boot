/*
 * 
 */
package com.groovy.sample.test

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer

import com.groovy.sample.test.config.Constants;

/**
 * The Class ApplicationServletInitializer.
 */
class ApplicationServletInitializer extends SpringBootServletInitializer {
	final Logger log = LoggerFactory.getLogger(ApplicationServletInitializer.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		 application.profiles(addDefaultProfile())
				.showBanner(false)
				.sources(ApplicationInitializer.class);
	}

	/**
	 * Set a default profile if it has not been set.
	 * <p/>
	 * <p>
	 * Please use -Dspring.profiles.active=dev
	 * </p>
	 */
	private String addDefaultProfile() {
		String profile = System.getProperty("spring.profiles.active");
		if (profile != null) {
			log.info("Running with Spring profile(s) : {}", profile);
			 profile;
		}

		log.warn("No Spring profile configured, running with default configuration");
		Constants.SPRING_PROFILE_DEVELOPMENT;
	}
}