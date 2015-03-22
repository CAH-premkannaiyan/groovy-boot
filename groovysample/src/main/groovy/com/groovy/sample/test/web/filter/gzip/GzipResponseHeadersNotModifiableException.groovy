package com.groovy.sample.test.web.filter.gzip

import javax.servlet.ServletException

class GzipResponseHeadersNotModifiableException extends ServletException {

	GzipResponseHeadersNotModifiableException(String message) {
		super(message)
	}
}
