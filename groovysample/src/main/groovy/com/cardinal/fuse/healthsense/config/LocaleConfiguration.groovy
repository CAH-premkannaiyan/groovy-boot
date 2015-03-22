package com.cardinal.fuse.healthsense.config

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.cardinal.fuse.healthsense.config.locale.AngularCookieLocaleResolver;

class LocaleConfiguration  extends WebMvcConfigurerAdapter implements EnvironmentAware {

	private RelaxedPropertyResolver propertyResolver;

	@Override
	void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.messageSource.");
	}

	@Bean(name = "localeResolver")
	LocaleResolver localeResolver() {
		AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
		cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
		cookieLocaleResolver;
	}

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(propertyResolver.getProperty("cacheSeconds", Integer.class, 1));
		messageSource;
	}

	@Override
	void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		registry.addInterceptor(localeChangeInterceptor);
	}
}
