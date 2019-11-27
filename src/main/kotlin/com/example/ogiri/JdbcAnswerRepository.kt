package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Repository
import java.lang.NullPointerException

@Repository
class JdbcAnswerRepository(private val jdbcTemplate: JdbcTemplate): AnswerRepository {

    private val rowMapper = RowMapper<Answer> { rs, _ ->
        Answer(rs.getLong("answer_id"), rs.getString("user_id"), rs.getLong("theme_id"), rs.getString("content"))
    }

    override fun create(userID: String, themeID: Long, content: String): Answer {
        jdbcTemplate.update("INSERT INTO answers(user_id, theme_id, content) values(?, ?, ?)", userID, themeID, content)
        val id = jdbcTemplate.queryForObject("SELECT identity()", Long::class.java)
        id ?: throw NullPointerException()
        val answer = jdbcTemplate.query("SELECT * FROM answers WHERE answer_id = ?", rowMapper, id).firstOrNull()
        answer ?: throw NullPointerException()
        return answer
    }

    override fun findByThemeId(themeID: Long): List<Answer>? {
        return jdbcTemplate.query("SELECT * FROM answers WHERE theme_id = ?", rowMapper, themeID)
    }
}