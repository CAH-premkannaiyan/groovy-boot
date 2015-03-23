package com.groovy.sample.test.service

import java.util.HashSet
import java.util.List
import java.util.Optional
import java.util.Set

import javax.inject.Inject

import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.groovy.sample.test.domain.Authority
import com.groovy.sample.test.domain.User
import com.groovy.sample.test.repository.AuthorityRepository
import com.groovy.sample.test.repository.UserRepository
import com.groovy.sample.test.security.SecurityUtils
import com.groovy.sample.test.service.util.RandomUtil

/**
 * Service class for managing users.
 */
@Service
@Transactional
class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class)

	@Inject
	private PasswordEncoder passwordEncoder

	@Inject
	protected UserRepository userRepository

	@Inject
	private AuthorityRepository authorityRepository

	Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key)
		Optional<User> userFromDatabase = userRepository.findOneByActivationKey(key)
		if (userFromDatabase.isPresent()){
			User user = userFromDatabase.get()
			user.activated(true)
			user.activationKey(true)
			user.activated(true)
			log.debug("Activated user: {}", user)
			return user
		}
		return Optional.empty()
	}

	User createUserInformation(String login, String password, String firstName, String lastName, String email,
			String langKey) {
		User newUser = new User()
		Authority authority = authorityRepository.findOne("ROLE_USER")
		Set<Authority> authorities = new HashSet<>()
		String encryptedPassword = passwordEncoder.encode(password)
		newUser.login = login
		// new user gets initially a generated password
		newUser.password = encryptedPassword
		newUser.firstName = firstName
		newUser.lastName = lastName
		newUser.email = email
		newUser.langKey = langKey
		// new user is not active
		newUser.activated = false
		// new user gets registration key
		newUser.activationKey(RandomUtil.generateActivationKey())
		authorities.add(authority)
		newUser.authorities = authorities
		userRepository.save(newUser)
		log.debug("Created Information for User: {}", newUser)
		return newUser
	}

	void updateUserInformation(String firstName, String lastName, String email) {
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
		if (user != null) {
			user.firstName = firstName
			user.lastName = lastName
			user.email = email
			userRepository.save(user)
			log.debug("Changed Information for User: {}", user)
		}
	}

	void changePassword(String password) {
		User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
		if (user != null) {
			String encryptedPassword = passwordEncoder.encode(password)
			user.password = encryptedPassword
			userRepository.save(user)
			log.debug("Changed password for User: {}", user)
		}
	}

	@Transactional(readOnly = true)
	User getUserWithAuthorities() {
		User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
		currentUser.authorities?.size() // TODO - eagerly load the association
		currentUser
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p/>
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	void removeNotActivatedUsers() {
		DateTime now = new DateTime()
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3))
		users.each {
			log.debug("Deleting not activated user {}", it.login)
			userRepository.delete(it)
		}
	}
}