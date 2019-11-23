package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcThemeRepository(private val jdbcTemplate: JdbcTemplate ): ThemeRepository {

    private val rowMapper = RowMapper<String> { rs, _ ->
        rs.getString("content")
    }

    override fun findByUserId(id: String): MutableList<String> {
        return jdbcTemplate.query("SELECT content from themes where user_id = ?", rowMapper, id)
    }

    override fun create(userID: String, content: String) {
        jdbcTemplate.update("INSERT INTO themes (user_id, time_stamp, content) values(?, curdate(), ?)", userID, content)
    }
}