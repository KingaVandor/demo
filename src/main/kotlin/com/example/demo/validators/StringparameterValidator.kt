package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import org.springframework.stereotype.Component

@Component
class StringParameterValidator {

    fun validateString(param: String, maxLength: Int = 20, lettersOnly: Boolean = true): String {
        validateNotEmpty(param)
        validateLength(param, maxLength)
        if (lettersOnly) validateChars(param)
        return param.trim()
    }

    private fun validateNotEmpty(param: String) {
        if (param.trim().isEmpty()) throw ValidationException("parameter $param should not be empty")

    }

    private fun validateChars(param: String) {
        if (!param.trim().all { it.isLetter() }) throw ValidationException("parameter $param should contain letters only")
    }

    private fun validateLength(param: String, maxLength: Int) {
        if (param.length > maxLength) throw ValidationException("parameter $param too long")
    }
}