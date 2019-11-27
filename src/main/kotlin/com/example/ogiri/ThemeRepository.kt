package com.example.ogiri

interface ThemeRepository {
    fun findByUserId(id: String): List<Theme>
    fun create(userID: String, content: String): Theme
}