package com.cardinal.fuse.healthsense.repository

import com.cardinal.fuse.healthsense.config.audit.AuditEventConverter
import com.cardinal.fuse.healthsense.domain.PersistentAuditEvent
import org.joda.time.LocalDateTime
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.boot.actuate.audit.AuditEventRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import javax.inject.Inject
import java.util.Date
import java.util.List

/**
 * Wraps an implementation of Spring Boot's AuditEventRepository.
 */
@Repository
public class CustomAuditEventRepository {

	@Bean
	public AuditEventRepository auditEventRepository() {
		new AuditEventRepositoryImpl()
	}
}