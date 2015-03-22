package com.groovy.sample.test.security.xauth

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class XAuthTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private TokenProvider tokenProvider

	private UserDetailsService detailsService

	XAuthTokenConfigurer(UserDetailsService detailsService, TokenProvider tokenProvider) {
		this.detailsService = detailsService
		this.tokenProvider = tokenProvider
	}

	@Override
	void configure(HttpSecurity http) throws Exception {
		XAuthTokenFilter customFilter = new XAuthTokenFilter(detailsService, tokenProvider)
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
	}
}