package com.cardinal.fuse.healthsense.config

import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import com.cardinal.fuse.healthsense.aop.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
class LoggingAspectConfiguration {

	@Bean
	@Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
	LoggingAspect loggingAspect() {
		new LoggingAspect();
	}
}
