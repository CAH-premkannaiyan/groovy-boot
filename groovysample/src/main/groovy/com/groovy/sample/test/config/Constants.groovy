package com.groovy.sample.test.config

final class Constants {
	private Constants() {
	}

	static final String SPRING_PROFILE_DEVELOPMENT = "dev";
	static final String SPRING_PROFILE_PRODUCTION = "prod";
	static final String SPRING_PROFILE_FAST = "fast";
	static final String SPRING_PROFILE_ASYNC = "!fast";
	static final String SPRING_PROFILE_CLOUD = "cloud";
	static final String SYSTEM_ACCOUNT = "system";
}
