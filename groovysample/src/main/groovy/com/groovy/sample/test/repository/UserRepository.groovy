package com.groovy.sample.test.repository

import java.util.List
import java.util.Optional

import org.joda.time.DateTime
import org.springframework.data.jpa.repository.JpaRepository

import com.groovy.sample.test.domain.User

/**
 * Spring Data JPA repository for the User entity.
 */
interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByActivationKey(String activationKey)

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore(DateTime dateTime)

	Optional<User> findOneByEmail(String email)

	User findOneByLogin(String login)

	void delete(User t)
}
