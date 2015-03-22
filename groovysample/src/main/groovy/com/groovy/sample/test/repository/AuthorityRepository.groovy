package com.groovy.sample.test.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.groovy.sample.test.domain.Authority

interface AuthorityRepository extends JpaRepository<Authority, String> {
}