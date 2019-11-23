package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcUserRepository(private val jdbcTemplate: JdbcTemplate): UserRepository {

    private val rowMapper = RowMapper<User> { rs, _ ->
        User(rs.getString("user_id"), rs.getString("password"), rs.getString("token"), rs.getString("name"))
    }

    override fun findByToken(token: String): User? {
        return jdbcTemplate.query("SELECT user_id, password, token, name FROM users WHERE token = ?", rowMapper, token).firstOrNull()
    }

    override fun findByUserID(userID: String): User? {
        return jdbcTemplate.query("SELECT user_id, password, token, name FROM users WHERE user_id = ?", rowMapper, userID).firstOrNull()
    }
}