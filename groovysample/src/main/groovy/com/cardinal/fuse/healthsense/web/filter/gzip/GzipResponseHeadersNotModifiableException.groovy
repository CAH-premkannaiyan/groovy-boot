package com.cardinal.fuse.healthsense.web.filter.gzip

import javax.servlet.ServletException

class GzipResponseHeadersNotModifiableException extends ServletException {

	GzipResponseHeadersNotModifiableException(String message) {
		super(message)
	}
}
