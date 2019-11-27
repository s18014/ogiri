package com.example.ogiri

interface AnswerRepository {
    fun create(userID: String, themeID: Long, content: String): Answer
    fun findByThemeId(themeID: Long): List<Answer>?
}