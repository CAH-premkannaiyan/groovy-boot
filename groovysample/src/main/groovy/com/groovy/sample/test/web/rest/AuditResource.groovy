package com.groovy.sample.test.web.rest

import com.groovy.sample.test.security.AuthoritiesConstants
import com.groovy.sample.test.service.AuditEventService
import com.groovy.sample.test.web.propertyeditors.LocaleDateTimeEditor
import org.joda.time.LocalDateTime
import org.springframework.boot.actuate.audit.AuditEvent
import org.springframework.http.MediaType
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*

import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import java.util.List

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping("/api")
class AuditResource {

	@Inject
	private AuditEventService auditEventService

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDateTime.class, new LocaleDateTimeEditor("yyyy-MM-dd", false))
	}

	@RequestMapping(value = "/audits/all",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	List<AuditEvent> findAll() {
		auditEventService.findAll()
	}

	@RequestMapping(value = "/audits/byDates",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	List<AuditEvent> findByDates(@RequestParam(value = "fromDate") LocalDateTime fromDate,
			@RequestParam(value = "toDate") LocalDateTime toDate) {
		auditEventService.findByDates(fromDate, toDate)
	}
}
