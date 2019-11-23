package com.example.ogiri

interface ThemeRepository {
    fun findByUserId(id: String): MutableList<String>
    fun create(userID: String, content: String)
}