package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class JdbcTagRepository(private val jdbcTemplate: JdbcTemplate): TagRepository {

    private val rowMapper = RowMapper<Tag> { rs, _ ->
        Tag(rs.getLong("tag_id"), rs.getString("name"))
    }

    override fun create(name: String): Tag {
        // すでに同じ名前があると返す
        val tag = jdbcTemplate.query("SELECT tag_id, name FROM tags WHERE name = ?", rowMapper, name).firstOrNull()
        if (tag != null) return tag
        jdbcTemplate.update("INSERT INTO tags(name) values(?)", name)
        val newTag = jdbcTemplate.query("SELECT tag_id, name FROM tags WHERE name = ?", rowMapper, name).firstOrNull()
        newTag ?: throw NullPointerException()
        return newTag
    }

    override fun findById(id: Long): Tag? {
        return jdbcTemplate.query("SELECT tag_id, name FROM tags WHERE tag_id = ?", rowMapper, id).firstOrNull()
    }

    override fun findByName(name: String): Tag? {
        return jdbcTemplate.query("SELECT tag_id, name FROM tags WHERE name = ?", rowMapper, name).firstOrNull()
    }

    override fun findByThemeId(themeID: Long): List<Tag>? {
        return jdbcTemplate.query("""
            SELECT tags.tag_id, tags.name FROM tags
            INNER JOIN tag_mappings
            ON tags.tag_id = tag_mappings.tag_id
            WHERE theme_id = ?
            """, rowMapper, themeID)
    }

    override fun connectTagWithTheme(themeID: Long, tagID: Long) {
        jdbcTemplate.update("INSERT INTO tag_mappings (theme_id, tag_id) values(?, ?)", themeID, tagID)
    }

    override fun disconnectTagWithTheme(themeID: Long, tagID: Long) {
        jdbcTemplate.update("DELETE FROM tag_mappings WHERE theme_id = ? AND tag_id = ?", themeID, tagID)
    }
}