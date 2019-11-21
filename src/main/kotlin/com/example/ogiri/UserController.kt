package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("user")
class UserController(private val userRepository: UserRepository) {
    @GetMapping("")
    fun index(model: Model, @CookieValue("token") token: String?): String {
        if (token != null) {
            val name = userRepository.findByToken(token)
            model.addAttribute("name", name)
        }
        return "user/index"
    }

    @GetMapping("post-theme")
    fun postTheme(model: Model): String {
        return "user/post-theme"
    }

    @GetMapping("theme-list")
    fun themeList(model: Model): String {
        return "user/theme-list"
    }

    @RequestMapping("debug-add-token")
    fun addToken(response: HttpServletResponse) {
        response.addCookie(Cookie("token", "token"))
        println("add cookie")
    }
}
