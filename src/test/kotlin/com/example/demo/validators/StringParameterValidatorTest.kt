package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

internal class StringParameterValidatorTest {
    private val validator = StringParameterValidator()

    @Test
    fun `when string is valid, validator passes`() {
        validator.validateString("Bob")
    }

    @Test
    fun `when string is has trailing spaces, validator passes`() {
        validator.validateString("Bob      ")
    }

    @Test
    fun `when string is to long, validator throws`() {
        shouldThrow<ValidationException> { validator.validateString("FredFredFredFredFredFredFredIsHisName") }
    }

    @Test
    fun `when string contains non-letters, validator throws`() {
        shouldThrow<ValidationException> { validator.validateString("Fred_IsHisName") }
    }

    @Test
    fun `when string is empty, validator throws`() {
        shouldThrow<ValidationException> { validator.validateString("          ") }
    }

    @Test
    fun `when string maxLength increased, longer strings would pass`() {
        validator.validateString("FredFredFredFredFredFredFredIsHisName", 50)
    }

    @Test
    fun `when letter check disabled, non-letter characters accepted`() {
        validator.validateString("Fred_IsHisName?", 20, false)
    }




}