package com.groovy.sample.test.service

import static org.assertj.core.api.Assertions.assertThat

import javax.inject.Inject

import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import com.groovy.sample.test.ApplicationInitializer
import com.groovy.sample.test.domain.User
import com.groovy.sample.test.repository.UserRepository

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
@Profile("dev")
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes=ApplicationInitializer.class)
class UserServiceTest extends Specification {

	@Inject
	private UserRepository userRepository

	@Inject
	private UserService userService

	@Shared
	@AutoCleanup
	ConfigurableApplicationContext context

	void setupSpec() {
//		ApplicationInitializer.main(null)
	}

	@Test
	void "find not activated users by creation date before"() {
		userService.removeNotActivatedUsers()
		DateTime now = new DateTime()
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3))
		assertThat(users).isEmpty()
	}
}
