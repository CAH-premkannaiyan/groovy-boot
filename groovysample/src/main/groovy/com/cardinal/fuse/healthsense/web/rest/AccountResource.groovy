package com.cardinal.fuse.healthsense.web.rest

import java.util.LinkedList
import java.util.Optional
import java.util.stream.Collectors

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.runtime.InvokerHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.cardinal.fuse.healthsense.domain.Authority
import com.cardinal.fuse.healthsense.domain.User
import com.cardinal.fuse.healthsense.repository.UserRepository
import com.cardinal.fuse.healthsense.security.SecurityUtils
import com.cardinal.fuse.healthsense.service.MailService
import com.cardinal.fuse.healthsense.service.UserService
import com.cardinal.fuse.healthsense.web.rest.dto.UserDTO
import com.codahale.metrics.annotation.Timed

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class)

	@Inject
	private UserRepository userRepository

	@Inject
	private UserService userService

	@Inject
	private MailService mailService

	/**
	 * POST  /register -> register the user.
	 */
	@RequestMapping(value = "/register",
	method = RequestMethod.POST,
	produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
		Optional<User> user = Optional.ofNullable(userRepository.findOneByLogin(userDTO.login))
		user.map(user, new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
				.orElseGet((userRepository.findOneByEmail(userDTO.email)
				.map(user, new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
				.orElseGet({
					User newUser = userService.createUserInformation(userDTO.login, userDTO.password,
							userDTO.firstName, userDTO.lastName, userDTO.email?.toLowerCase(),
							userDTO.langKey)
					String baseUrl = request.getScheme() + // "http"
							"://" +                                // "://"
							request.getServerName() +              // "myhost"
							":" +                                  // ":"
							request.getServerPort()               // "80"

					mailService.sendActivationEmail(newUser, baseUrl)
					return new ResponseEntity<>(HttpStatus.CREATED)
				})
				))
	}
	/**
	 * GET  /activate -> activate the registered user.
	 */
	@RequestMapping(value = "/activate",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
		return Optional.ofNullable(userService.activateRegistration(key))
				.map(new ResponseEntity<String>(HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR))
	}

	/**
	 * GET  /authenticate -> check if the user is authenticated, and return its login.
	 */
	@RequestMapping(value = "/authenticate",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated")
		return request.getRemoteUser()
	}

	/**
	 * GET  /account -> get the current user.
	 */
	@RequestMapping(value = "/account",
	method = RequestMethod.GET,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> getAccount() {
		User user = userService.getUserWithAuthorities()
		if (user != null) {
			UserDTO userDTO = new UserDTO()
			InvokerHelper.setProperties(userDTO, user.properties)
			return new ResponseEntity<UserDTO>(UserDTO,HttpStatus.OK)
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND)
	}

	/**
	 * POST  /account -> update the current user information.
	 */
	@RequestMapping(value = "/account",
	method = RequestMethod.POST,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
		return Optional.of(userRepository
				.findOneByLogin(userDTO?.login))
				.filter(userDTO?.login?.equals(SecurityUtils.getCurrentLogin()))
				.map(userDTO, {
					userService.updateUserInformation(userDTO.firstName, userDTO.lastName, userDTO.email)
					return new ResponseEntity<String>(HttpStatus.OK)
				})
				.orElseGet(new ResponseEntity<>(HttpStatus.NOT_FOUND))
	}

	/**
	 * POST  /change_password -> changes the current user's password
	 */
	@RequestMapping(value = "/account/change_password",
	method = RequestMethod.POST,
	produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> changePassword(@RequestBody String password) {
		if (StringUtils.isEmpty(password) || password.length() < 5 || password.length() > 50) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST)
		}
		userService.changePassword(password)
		return new ResponseEntity<>(HttpStatus.OK)
	}
}
