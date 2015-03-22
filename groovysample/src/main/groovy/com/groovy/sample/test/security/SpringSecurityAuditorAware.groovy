package com.groovy.sample.test.security

import com.groovy.sample.test.config.Constants
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
class SpringSecurityAuditorAware implements AuditorAware<String> {

	String getCurrentAuditor() {
		SecurityUtils.getCurrentLogin()?:Constants.SYSTEM_ACCOUNT
	}
}
