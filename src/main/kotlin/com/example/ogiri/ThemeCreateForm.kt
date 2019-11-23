package com.example.ogiri

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class ThemeCreateForm {
    @NotBlank
    @Size(max = 255)
    var content: String? = null

}