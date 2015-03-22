package com.cardinal.fuse.healthsense.config

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cardinal.fuse.healthsense.async.ExceptionHandlingAsyncTaskExecutor;

@Configuration
@EnableAsync
@EnableScheduling
@Profile(Constants.SPRING_PROFILE_ASYNC)
class AsyncConfiguration implements AsyncConfigurer, EnvironmentAware {
	final Logger log = LoggerFactory
	.getLogger(AsyncConfiguration.class);

	private RelaxedPropertyResolver propertyResolver;

	@Override
	void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment,
				"async.");
	}

	@Override
	@Bean
	Executor getAsyncExecutor() {
		log.debug("Creating Async Task Executor");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(propertyResolver.getProperty("corePoolSize",
				Integer.class, 2));
		executor.setMaxPoolSize(propertyResolver.getProperty("maxPoolSize",
				Integer.class, 50));
		executor.setQueueCapacity(propertyResolver.getProperty("queueCapacity",
				Integer.class, 10000));
		executor.setThreadNamePrefix("healthsense-Executor-");
		return new ExceptionHandlingAsyncTaskExecutor(executor);
	}

	@Override
	AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}
}