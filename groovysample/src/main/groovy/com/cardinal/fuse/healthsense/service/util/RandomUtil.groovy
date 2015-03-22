package com.cardinal.fuse.healthsense.service.util

import org.apache.commons.lang.RandomStringUtils

/**
 * Utility class for generating random Strings.
 */
final class RandomUtil {

	private static final int DEF_COUNT = 20

	private RandomUtil() {
	}

	/**
	 * Generates a password.
	 *
	 * @return the generated password
	 */
	static String generatePassword() {
		RandomStringUtils.randomAlphanumeric(DEF_COUNT)
	}

	/**
	 * Generates an activation key.
	 *
	 * @return the generated activation key
	 */
	static String generateActivationKey() {
		RandomStringUtils.randomNumeric(DEF_COUNT)
	}
}
