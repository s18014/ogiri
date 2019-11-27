package com.example.ogiri

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

@Repository
class JdbcUserRepository(private val jdbcTemplate: JdbcTemplate): UserRepository {

    private val rowMapper = RowMapper<User> { rs, _ ->
        User(rs.getString("user_id"), rs.getString("password"), rs.getString("token"), rs.getString("name"))
    }

    override fun findByToken(token: String): User? {
        return jdbcTemplate.query("SELECT user_id, password, token, name FROM users WHERE token = ?", rowMapper, token).firstOrNull()
    }

    override fun findByUserID(userID: String): User? {
        return jdbcTemplate.query("SELECT user_id, password, token, name FROM users WHERE user_id = ?", rowMapper, userID).firstOrNull()
    }

    override fun addToken(userID: String, password: String): String? {
        val user = jdbcTemplate.query("SELECT user_id, password, token, name FROM users WHERE user_id = ? AND password = ?", rowMapper, userID, password).firstOrNull()
        if (user != null) {
            val value: String = password + System.currentTimeMillis()
            // TODO: トークンが重複しないようにループ処理を入れる
            try {
                val digest: MessageDigest = MessageDigest.getInstance("SHA-1")
                val result: ByteArray = digest.digest(value.toByteArray())
                val token = String.format("%040x", BigInteger(1, result))
                jdbcTemplate.update("UPDATE users SET token = ? WHERE user_id = ?", token, userID)
                return token
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
        return null
    }
}