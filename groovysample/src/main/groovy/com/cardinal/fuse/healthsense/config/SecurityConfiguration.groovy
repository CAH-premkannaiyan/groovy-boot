package com.cardinal.fuse.healthsense.config

import com.cardinal.fuse.healthsense.security.*
import com.cardinal.fuse.healthsense.security.xauth.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.data.repository.query.spi.EvaluationContextExtension
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.inject.Inject

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Inject
	private Http401UnauthorizedEntryPoint authenticationEntryPoint

	@Inject
	private UserDetailsService userDetailsService

	@Inject
	private TokenProvider tokenProvider

	@Bean
	PasswordEncoder passwordEncoder() {
		new BCryptPasswordEncoder()
	}

	@Inject
	void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder())
	}

	@Override
	void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/scripts/**/*.{js,html}")
				.antMatchers("/bower_components/**")
				.antMatchers("/i18n/**")
				.antMatchers("/assets/**")
				.antMatchers("/swagger-ui/**")
				.antMatchers("/test/**")
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.csrf()
				.disable()
				.headers()
				.frameOptions()
				.disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/api/register").permitAll()
				.antMatchers("/api/activate").permitAll()
				.antMatchers("/api/authenticate").permitAll()
				.antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/api/**").authenticated()
				.antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
				.antMatchers("/protected/**").authenticated()
				.and()
				.apply(securityConfigurerAdapter())
	}

	@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
	private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
	}

	private XAuthTokenConfigurer securityConfigurerAdapter() {
		new XAuthTokenConfigurer(userDetailsService, tokenProvider)
	}

	/**
	 * This allows SpEL support in Spring Data JPA @Query definitions.
	 *
	 * See https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
	 */
	@Bean
	EvaluationContextExtension securityExtension() {
		def contextExtension = new EvaluationContextExtensionSupport() {
					@Override
					String getExtensionId() {
						"security"
					}

					@Override
					SecurityExpressionRoot getRootObject() {
						new SecurityExpressionRoot(SecurityContextHolder.getContext().getAuthentication()) {}
					}
				}
	}
}
