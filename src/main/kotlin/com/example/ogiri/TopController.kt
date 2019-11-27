package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("")
class TopController(private val userRepository: UserRepository) {
    @GetMapping("")
    fun index(model: Model,
              @CookieValue("token") token: String?): String {
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                model.addAttribute("user", user)
            }
        }
        return "/index"
    }

    @GetMapping("search")
    fun search(): String {
        return "/search"
    }
}