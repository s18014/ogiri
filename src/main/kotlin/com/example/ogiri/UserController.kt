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
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Controller
@RequestMapping("user")
class UserController(private val userRepository: UserRepository,
                     private val themeRepository: ThemeRepository,
                     private val tagRepository: TagRepository) {

    @GetMapping("")
    fun index(model: Model,
              @CookieValue("token") token: String?): String {
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                model.addAttribute("name", user.name)
                return "user/index"
            }
        }
        // ログインしていなければログイン画面に飛ばす
        return "redirect:/login"
    }

    @GetMapping("post-theme")
    fun postTheme(form: ThemeCreateForm): String {
        return "user/post-theme"
    }

    @PostMapping("post-theme")
    fun createTheme(@CookieValue("token") token: String?,
                    @Validated form: ThemeCreateForm,
                    bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return "user/post-theme"
        if (token != null) {
            val user = userRepository.findByToken(token)
            if (user != null) {
                val theme = themeRepository.create(user.userID, requireNotNull(form.content))
                val names: List<String>? = form.tags?.trim()?.split("( |　)+".toRegex())
                if (names != null) {
                    for (name in names) {
                        val tag = tagRepository.create(name)
                        tagRepository.connectTagWithTheme(theme.themeID, tag.id)
                    }
                }
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

    class ThemeCreateForm {
        @NotBlank
        @Size(max = 255)
        var content: String? = null
        var tags: String? = null
    }
}
