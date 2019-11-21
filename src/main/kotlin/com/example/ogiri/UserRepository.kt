package com.example.ogiri

interface UserRepository {
    fun findByToken(token: String): String?
}