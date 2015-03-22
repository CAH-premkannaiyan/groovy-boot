package com.cardinal.fuse.healthsense.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationProvider implements
org.springframework.security.authentication.AuthenticationProvider {

	private PasswordEncoder passwordEncoder

	private UserDetailsService userDetailsService

	AuthenticationProvider(UserDetailsService userDetailsService,
	PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService
		this.passwordEncoder = passwordEncoder
	}

	@Override
	Authentication authenticate(Authentication authentication)
	throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication

		String login = token.getName()
		UserDetails user = userDetailsService.loadUserByUsername(login)
		if (user == null) {
			throw new UsernameNotFoundException("User does not exists")
		}
		String password = user.getPassword()
		String tokenPassword = (String) token.getCredentials()
		if (!passwordEncoder.matches(tokenPassword, password)) {
			throw new BadCredentialsException("Invalid username/password")
		}
		new UsernamePasswordAuthenticationToken(user, password,
				user.getAuthorities())
	}

	@Override
	boolean supports(Class<?> authentication) {
		UsernamePasswordAuthenticationToken.class.equals(authentication)
	}
}