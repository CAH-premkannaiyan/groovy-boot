package com.cardinal.fuse.healthsense.security.xauth

/**
 * The security token.
 */
class Token {

	String token
	long expires

	public Token(String token, long expires){
		this.token = token
		this.expires = expires
	}

	String getToken() {
		token
	}

	long getExpires() {
		expires
	}
}
