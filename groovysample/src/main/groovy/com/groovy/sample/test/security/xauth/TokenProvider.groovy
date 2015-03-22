package com.groovy.sample.test.security.xauth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.codec.Hex

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class TokenProvider {

	private final String secretKey
	private final int tokenValidity

	TokenProvider(String secretKey, int tokenValidity) {
		this.secretKey = secretKey
		this.tokenValidity = tokenValidity
	}

	Token createToken(UserDetails userDetails) {
		long expires = System.currentTimeMillis() + 1000L * tokenValidity
		String token = userDetails.getUsername() + ":" + expires + ":" + computeSignature(userDetails, expires)
		new Token(token, expires)
	}

	String computeSignature(UserDetails userDetails, long expires) {
		StringBuilder signatureBuilder = new StringBuilder()
		signatureBuilder.append(userDetails.getUsername()).append(":")
		signatureBuilder.append(expires).append(":")
		signatureBuilder.append(userDetails.getPassword()).append(":")
		signatureBuilder.append(secretKey)

		MessageDigest digest
		try {
			digest = MessageDigest.getInstance("MD5")
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!")
		}
		new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())))
	}

	String getUserNameFromToken(String authToken) {
		if (null == authToken) {
			return null
		}
		String[] parts = authToken.split(":")
		parts[0]
	}

	boolean validateToken(String authToken, UserDetails userDetails) {
		String[] parts = authToken.split(":")
		long expires = Long.parseLong(parts[1])
		String signature = parts[2]
		String signatureToMatch = computeSignature(userDetails, expires)
		expires >= System.currentTimeMillis() && signature.equals(signatureToMatch)
	}
}
