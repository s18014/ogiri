package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Controller
@RequestMapping("themes")
class ThemeController(private val themeRepository: ThemeRepository,
                      private val answerRepository: AnswerRepository,
                      private val userRepository: UserRepository) {
    @GetMapping("{id}")
    fun index(model: Model,
              @PathVariable("id") id: Long,
              form: AnswerCreateForm): String {
        val theme = themeRepository.findById(id)
        model.addAttribute("theme", theme)
        val answers = answerRepository.findByThemeId(id)
        model.addAttribute("answers", answers)
        return "themes/index"
    }

    @PatchMapping("{id}")
    fun addAnswer(model: Model,
                  @CookieValue("token") token: String?,
                  @PathVariable("id") id: Long,
                  @Validated form: AnswerCreateForm,
                  bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            val theme = themeRepository.findById(id)
            model.addAttribute("theme", theme)
            return "themes/index"
        }
        if (token != null) {
           val user = userRepository.findByToken(token)
            if (user != null) {
                answerRepository.create(user.userID, id, requireNotNull(form.content))
            }
        }
        return "redirect:/themes/$id"
    }

    class AnswerCreateForm {
        @NotBlank
        @Size(max = 255)
        var content: String? = null
    }
}