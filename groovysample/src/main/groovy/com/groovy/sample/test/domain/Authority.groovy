package com.groovy.sample.test.domain;

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "T_AUTHORITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(includeNames=true)
class Authority implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 740852356659742305L;
	@NotNull
	@Size(min = 0, max = 50)
	@Id
	@Column(length = 50)
	private String name;
}