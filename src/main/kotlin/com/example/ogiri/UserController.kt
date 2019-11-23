package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("user")
class UserController(private val userRepository: UserRepository,
                     private val themeRepository: ThemeRepository) {

    @GetMapping("")
    fun index(model: Model,
              @CookieValue("token") token: String?): String {
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                model.addAttribute("name", user.name)
                println(String.format("%s found", user.name))
            }
        }
        return "user/index"
    }

    @GetMapping("post-theme")
    fun postTheme(form: ThemeCreateForm): String {
        return "user/post-theme"
    }

    @PostMapping("post-theme")
    fun createTheme(@Validated form: ThemeCreateForm,
                    @CookieValue("token") token: String?,
                    bindingResult: BindingResult): String {
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                themeRepository.create(user.userID, requireNotNull(form.content))
            }
        }
        return "redirect:/user/theme-list"
    }

    @GetMapping("theme-list")
    fun themeList(model: Model,
                  @CookieValue("token") token: String?): String {
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                val themes = themeRepository.findByUserId(user.userID)
                model.addAttribute("themes", themes)
            }
        }
        return "user/theme-list"
    }

    // テストコード
    @RequestMapping("debug-add-token")
    fun addToken(response: HttpServletResponse): String {
        response.addCookie(Cookie("token", "token"))
        println("add cookie")
        return "redirect:/user"
    }

    @RequestMapping("debug-add-token2")
    fun addToken2(response: HttpServletResponse): String {
        response.addCookie(Cookie("token", "token2"))
        println("add cookie")
        return "redirect:/user"
    }
}
