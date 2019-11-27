package com.example.ogiri

interface ThemeRepository {
    fun findByUserId(id: String): List<Theme>?
    fun findById(id: Long): Theme?
    fun create(userID: String, content: String): Theme
    fun findLatest(max: Int): List<Theme>?
}