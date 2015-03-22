package com.cardinal.fuse.healthsense.config

import javax.sql.DataSource

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.config.java.AbstractCloudConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
class CloudDatabaseConfiguration extends AbstractCloudConfig {
	final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

	@Bean
	DataSource dataSource() {
		log.info("Configuring JDBC datasource from a cloud provider");
		connectionFactory().dataSource();
	}
}