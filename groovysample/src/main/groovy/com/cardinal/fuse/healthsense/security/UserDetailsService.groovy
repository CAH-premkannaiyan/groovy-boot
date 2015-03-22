package com.cardinal.fuse.healthsense.security

import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import com.cardinal.fuse.healthsense.domain.User
import com.cardinal.fuse.healthsense.repository.UserRepository

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(UserDetailsService.class)

	@Inject
	private UserRepository userRepository

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {
		log.debug("Authenticating {}", login)
		String lowercaseLogin = login.toLowerCase()
		User user =  userRepository.findOneByLogin(lowercaseLogin)
		if (user ==null) {
			throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")
		}
		if (!user?.activated) {
			throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated")
		}
		List<GrantedAuthority> grantedAuthorities = user.authorities.collect({userData -> new SimpleGrantedAuthority( userData.name )})
		return new org.springframework.security.core.userdetails.User(lowercaseLogin,user.password,grantedAuthorities)
	}
}