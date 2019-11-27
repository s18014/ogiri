package com.example.ogiri

// tagとtag_mappingを管理
interface TagRepository {
    fun create(name: String): Tag
    fun findById(id: Long): Tag?
    fun findByThemeId(themeID: Long): List<Tag>?
    fun connectTagWithTheme(themeID: Long, tagID: Long)
}