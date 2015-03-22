package com.groovy.sample.test.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
	
	public static final String PATIENT = "ROLE_PATIENT";
	
	public static final String PROVIDER = "ROLE_PROVIDER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
