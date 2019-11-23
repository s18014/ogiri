package com.example.ogiri

interface UserRepository {
    fun findByToken(token: String): User?
    fun findByUserID(userID: String): User?
}