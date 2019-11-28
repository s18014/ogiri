package com.example.ogiri

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootApplication
class OgiriApplication {

	@Bean
	fun commandLineRunner(jdbcTemplate: JdbcTemplate) = CommandLineRunner {
        // users table
		jdbcTemplate.execute("""
			CREATE TABLE IF NOT EXISTS users (
				user_id VARCHAR(255) PRIMARY KEY,
				password VARCHAR(255) NOT NULL,
				token VARCHAR(255),
				name VARCHAR(255)
			)
		""")

		// themes table
		jdbcTemplate.execute("""
    		CREATE TABLE IF NOT EXISTS themes (
    			theme_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    			user_id VARCHAR(255) NOT NULL,
				time_stamp DATETIME NOT NULL DEFAULT now(),
				content VARCHAR(255) NOT NULL,
				CONSTRAINT fk_themes_users_user_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
			)
		""")

		// tags table
		jdbcTemplate.execute("""
			CREATE TABLE IF NOT EXISTS tags (
				tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				name VARCHAR(255) NOT NULL UNIQUE
			)
		""")

		// tag_mappings table
		jdbcTemplate.execute("""
			CREATE TABLE IF NOT EXISTS tag_mappings (
				tag_mapping_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				theme_id BIGINT NOT NULL,
				tag_id BIGINT NOT NULL,
				CONSTRAINT fk_tag_mappings_themes_theme_id FOREIGN KEY (theme_id) REFERENCES themes (theme_id) ON DELETE CASCADE,
				CONSTRAINT fk_tag_mappings_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags (tag_id) ON DELETE CASCADE
			)
		""")

		// answers table
		jdbcTemplate.execute("""
    		CREATE TABLE IF NOT EXISTS answers (
				answer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				user_id VARCHAR(255) NOT NULL,
				theme_id BIGINT NOT NULL,
				time_stamp DATETIME NOT NULL DEFAULT now(),
				content VARCHAR(255) NOT NULL,
				CONSTRAINT fk_answers_users_user_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
				CONSTRAINT fk_answers_themes_theme_id FOREIGN KEY (theme_id) REFERENCES themes (theme_id) ON DELETE CASCADE
			)
		""")

		// ratings
		jdbcTemplate.execute("""
			CREATE TABLE IF NOT EXISTS ratings (
				rating_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				user_id VARCHAR(255) NOT NULL,
				answer_id BIGINT NOT NULL,
				eval BOOLEAN NOT NULL,
				CONSTRAINT fk_ratings_users_user_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
				CONSTRAINT fk_ratings_answers_answer_id FOREIGN KEY (answer_id) REFERENCES answers (answer_id) ON DELETE CASCADE
			)
		""")
	}
}

fun main(args: Array<String>) {
	runApplication<OgiriApplication>(*args)
}
