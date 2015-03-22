package com.cardinal.fuse.healthsense.config

import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.fasterxml.jackson.datatype.joda.ser.JacksonJodaFormat;

@Configuration
class JacksonConfiguration {
	
	@Bean
	JodaModule jacksonJodaModule() {
		JodaModule module = new JodaModule();
		DateTimeFormatterFactory formatterFactory = new DateTimeFormatterFactory();
		formatterFactory.setIso(DateTimeFormat.ISO.DATE);
		module.addSerializer(DateTime.class, new DateTimeSerializer(
				new JacksonJodaFormat(formatterFactory.createDateTimeFormatter()
				.withZoneUTC())));
		module;
	}
}