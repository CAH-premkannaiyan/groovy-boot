package com.cardinal.fuse.healthsense.security

import com.cardinal.fuse.healthsense.config.Constants
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
