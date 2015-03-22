package com.groovy.sample.test.repository

import java.util.List

import org.joda.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository

import com.groovy.sample.test.domain.PersistentAuditEvent

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
interface PersistenceAuditEventRepository extends
JpaRepository<PersistentAuditEvent, String> {

	List<PersistentAuditEvent> findByPrincipal(String principal)

	List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(
	String principal, LocalDateTime after)

	List<PersistentAuditEvent> findAllByAuditEventDateBetween(
	LocalDateTime fromDate, LocalDateTime toDate)
}
