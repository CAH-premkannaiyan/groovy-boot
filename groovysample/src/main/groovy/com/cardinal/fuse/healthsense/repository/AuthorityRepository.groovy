package com.cardinal.fuse.healthsense.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.cardinal.fuse.healthsense.domain.Authority

interface AuthorityRepository extends JpaRepository<Authority, String> {
}