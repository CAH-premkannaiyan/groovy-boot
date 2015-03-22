package com.cardinal.fuse.healthsense.domain.util

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {

	private static DateTimeFormatter formatter = DateTimeFormat
	.forPattern("yyyy-MM-dd");

	@Override
	void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeString(formatter.print(value));
	}
}
