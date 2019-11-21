package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcUserRepository(private val jdbcTemplate: JdbcTemplate): UserRepository {
    private val rowMapper = RowMapper<String> { rs, _ ->
        rs.getString("name")
    }

    override fun findByToken(token: String): String? {
        return jdbcTemplate.query("SELECT name from users where token = ?", rowMapper, token).firstOrNull()
    }
}