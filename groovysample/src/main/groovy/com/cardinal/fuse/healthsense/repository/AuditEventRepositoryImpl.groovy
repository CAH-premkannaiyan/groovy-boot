package com.cardinal.fuse.healthsense.repository

import java.util.Date
import java.util.List

import javax.inject.Inject

import org.joda.time.LocalDateTime
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import com.cardinal.fuse.healthsense.config.audit.AuditEventConverter
import com.cardinal.fuse.healthsense.domain.PersistentAuditEvent

class AuditEventRepositoryImpl implements AuditEventRepository{

	@Inject
	private PersistenceAuditEventRepository persistenceAuditEventRepository

	@Inject
	private AuditEventConverter auditEventConverter

	@Override
	public List<AuditEvent> find(String principal, Date after) {
		Iterable<PersistentAuditEvent> persistentAuditEvents
		if (principal == null && after == null) {
			persistentAuditEvents = persistenceAuditEventRepository
					.findAll()
		} else if (after == null) {
			persistentAuditEvents = persistenceAuditEventRepository
					.findByPrincipal(principal)
		} else {
			persistentAuditEvents = persistenceAuditEventRepository
					.findByPrincipalAndAuditEventDateAfter(principal,
					new LocalDateTime(after))
		}
		return auditEventConverter
				.convertToAuditEvent(persistentAuditEvents)
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void add(AuditEvent event) {
		PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent()
		persistentAuditEvent.setPrincipal(event.getPrincipal())
		persistentAuditEvent.setAuditEventType(event.getType())
		persistentAuditEvent.setAuditEventDate(new LocalDateTime(event
				.getTimestamp()))
		persistentAuditEvent.setData(auditEventConverter
				.convertDataToStrings(event.getData()))

		persistenceAuditEventRepository.save(persistentAuditEvent)
	}
}
