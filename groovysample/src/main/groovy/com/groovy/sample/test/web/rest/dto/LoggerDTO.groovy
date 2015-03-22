package com.groovy.sample.test.web.rest.dto

import groovy.transform.ToString
import ch.qos.logback.classic.Logger

import com.fasterxml.jackson.annotation.JsonCreator

@ToString(includeNames=true)
public class LoggerDTO {

	private String name

	private String level

	public LoggerDTO(Logger logger) {
		this.name = logger.getName()
		this.level = logger.getEffectiveLevel().toString()
	}

	@JsonCreator
	public LoggerDTO() {
	}
}
