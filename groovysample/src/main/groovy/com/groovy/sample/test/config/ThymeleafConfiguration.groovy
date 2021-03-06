package com.groovy.sample.test.config

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
class ThymeleafConfiguration {
	final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 emails")
	ClassLoaderTemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("mails/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode("HTML5");
		emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		emailTemplateResolver.setOrder(1);
		emailTemplateResolver;
	}

	@Bean
	@Description("Spring mail message resolver")
	MessageSource emailMessageSource() {
		log.info("loading non-reloadable mail messages resources");
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/mails/messages/messages");
		messageSource.setDefaultEncoding(CharEncoding.UTF_8);
		messageSource;
	}
}
