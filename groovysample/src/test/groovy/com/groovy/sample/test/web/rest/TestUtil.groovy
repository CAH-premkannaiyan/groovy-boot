package com.groovy.sample.test.web.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType

import java.io.IOException
import java.nio.charset.Charset

/**
 * Utility class for testing REST controllers.
 */
class TestUtil {

	/** MediaType for JSON UTF8 */
	static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
	MediaType.APPLICATION_JSON.getType(),
	MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"))

	/**
	 * Convert an object to JSON byte array.
	 *
	 * @param object
	 *            the object to convert
	 * @return the JSON byte array
	 * @throws IOException
	 */
	static byte[] convertObjectToJsonBytes(Object object)
	throws IOException {
		ObjectMapper mapper = new ObjectMapper()
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		mapper.writeValueAsBytes(object)
	}
}
