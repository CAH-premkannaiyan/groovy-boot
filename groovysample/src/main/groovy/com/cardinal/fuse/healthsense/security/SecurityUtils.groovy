package com.cardinal.fuse.healthsense.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

import java.util.Collection

/**
 * Utility class for Spring Security.
 */
final class SecurityUtils {

	private SecurityUtils() {
	}

	/**
	 * Get the login of the current user.
	 */
	static String getCurrentLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext()
		Authentication authentication = securityContext.getAuthentication()
		UserDetails springSecurityUser = null
		String userName = null
		if(authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				springSecurityUser = (UserDetails) authentication.getPrincipal()
				userName = springSecurityUser.getUsername()
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal()
			}
		}
		userName
	}

	/**
	 * Check if a user is authenticated.
	 *
	 * @return true if the user is authenticated, false otherwise
	 */
	static boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext()
		Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities()
		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
					return false
				}
			}
		}
		true
	}


	/**
	 * If the current user has a specific security role.
	 */
	static boolean isUserInRole(String role) {
		SecurityContext securityContext = SecurityContextHolder.getContext()
		Authentication authentication = securityContext.getAuthentication()
		if(authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal()
				return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(role))
			}
		}
		false
	}
}
