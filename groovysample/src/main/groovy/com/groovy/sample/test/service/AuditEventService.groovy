package com.groovy.sample.test.service

import com.groovy.sample.test.config.audit.AuditEventConverter
import com.groovy.sample.test.domain.PersistentAuditEvent
import com.groovy.sample.test.repository.PersistenceAuditEventRepository
import org.joda.time.LocalDateTime
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.inject.Inject
import java.util.List

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator
 * AuditEventRepository
 * </p>
 */
@Service
@Transactional
class AuditEventService {

	@Inject
	private PersistenceAuditEventRepository persistenceAuditEventRepository

	@Inject
	private AuditEventConverter auditEventConverter

	List<AuditEvent> findAll() {
		auditEventConverter
				.convertToAuditEvent(persistenceAuditEventRepository.findAll())
	}

	List<AuditEvent> findByDates(LocalDateTime fromDate,
			LocalDateTime toDate) {
		List<PersistentAuditEvent> persistentAuditEvents = persistenceAuditEventRepository
				.findAllByAuditEventDateBetween(fromDate, toDate)

		auditEventConverter.convertToAuditEvent(persistentAuditEvents)
	}
}
