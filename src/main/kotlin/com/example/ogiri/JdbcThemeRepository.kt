package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Repository

@Repository
class JdbcThemeRepository(private val jdbcTemplate: JdbcTemplate ): ThemeRepository {

    private val rowMapper = RowMapper<Theme> { rs, _ ->
        Theme(rs.getLong("theme_id"), rs.getString("user_id"), rs.getString("time_stamp"), rs.getString("content"))
    }

    override fun findByUserId(id: String): List<Theme> {
        return jdbcTemplate.query("SELECT theme_id, user_id, time_stamp, content FROM themes WHERE user_id = ?", rowMapper, id)
    }

    override fun create(userID: String, content: String): Theme {
        jdbcTemplate.update("INSERT INTO themes (user_id, content) values(?, ?)", userID, content)
        val id: Long? = jdbcTemplate.queryForObject("SELECT identity()", Long::class.java)
        id ?: throw NullPointerException()
        val theme: Theme? = jdbcTemplate.query("SELECT theme_id, user_id, time_stamp, content FROM themes WHERE theme_id = ?", rowMapper, id).firstOrNull()
        theme ?: throw NullPointerException()
        return theme
    }
}