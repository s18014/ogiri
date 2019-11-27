package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotBlank

@Controller
class LoginController(private val userRepository: UserRepository) {
    @GetMapping("login")
    fun loginForm(form: LoginForm): String {
        return "/login"
    }

    @PostMapping("login")
    fun login(res: HttpServletResponse,
              @Validated form: LoginForm,
              bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return "/login"

        // 値があればトークン作成
        if (form.userID != null && form.password != null) {
            val token = userRepository.addToken(requireNotNull(form.userID), requireNotNull(form.password))
            if (token != null) {
                res.addCookie(Cookie("token", token))
                return "redirect:/user"
            }
        }
        return "redirect:/login"
    }

    @RequestMapping("logout")
    fun logout(httpServletResponse: HttpServletResponse): String {
        httpServletResponse.addCookie(Cookie("token", null))
        return "redirect:/"
    }

    class LoginForm {
        @NotBlank
        var userID: String? = null

        @NotBlank
        var password: String? = null
    }
}
