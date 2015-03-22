package com.groovy.sample.test.repository

import com.groovy.sample.test.config.audit.AuditEventConverter
import com.groovy.sample.test.domain.PersistentAuditEvent
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