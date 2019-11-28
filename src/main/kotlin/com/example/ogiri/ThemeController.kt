package com.example.ogiri

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Controller
@RequestMapping("themes")
class ThemeController(private val themeRepository: ThemeRepository,
                      private val answerRepository: AnswerRepository,
                      private val userRepository: UserRepository,
                      private val tagRepository: TagRepository) {
    @GetMapping("{id}")
    fun index(model: Model,
              @CookieValue("token") token: String?,
              @PathVariable("id") id: Long,
              form: AnswerCreateForm): String {
        val user = if (token != null) userRepository.findByToken(token) else null
        val theme = themeRepository.findById(id)
        val answers = answerRepository.findByThemeId(id)
        val tags = tagRepository.findByThemeId(id)
        model.addAttribute("theme", theme)
        model.addAttribute("answers", answers)
        model.addAttribute("tags", tags)

        model.addAttribute("isMyTheme", false)
        if (user != null && theme != null) {
            if (user.userID == theme.userID) model.addAttribute("isMyTheme", true)
        }
        return "themes/index"
    }

    @PatchMapping("{id}")
    fun addAnswer(model: Model,
                  @CookieValue("token") token: String?,
                  @PathVariable("id") id: Long,
                  @Validated form: AnswerCreateForm,
                  bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return index(model, token, id, form)
        }
        if (token != null) {
           val user = userRepository.findByToken(token)
            if (user != null) {
                answerRepository.create(user.userID, id, requireNotNull(form.content))
            }
        }
        return "redirect:/themes/$id"
    }

    @GetMapping("{id}/edit")
    fun edit(model: Model,
             @PathVariable("id") id: Long,
             form: ThemeEditForm): String {
        val tags = tagRepository.findByThemeId(id)
        val theme = themeRepository.findById(id)
        println(tags)
        model.addAttribute("theme", theme)
        model.addAttribute("tags", tags)
        return "themes/edit"
    }

    @PatchMapping("{id}/edit")
    fun update(model: Model,
               @PathVariable("id") id: Long,
               form: ThemeEditForm,
               bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return edit(model, id, form)

        // タグの削除
        val tagCheckBox = form.tagCheckBox
        if (tagCheckBox != null) {
            for (tagName in tagCheckBox) {
                val tag = tagRepository.findByName(tagName)
                if (tag != null) tagRepository.disconnectTagWithTheme(id, tag.id)
            }
        }

        // タグを追加
        val names: List<String>? = form.tags?.trim()?.split("( |　)+".toRegex())?.filter { it != ""}
        if (names != null) {
            for (name in names) {
                val tag = tagRepository.create(name)
                tagRepository.connectTagWithTheme(id, tag.id)
            }
        }


        return "redirect:/themes/$id/edit"
    }


    class AnswerCreateForm {
        @NotBlank
        @Size(max = 255)
        var content: String? = null
    }

    class ThemeEditForm {
        var tags: String? = null
        var tagCheckBox: List<String>? = null
    }
}