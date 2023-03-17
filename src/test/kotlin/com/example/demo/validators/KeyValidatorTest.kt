package com.example.demo.validators


import com.example.demo.exceptions.ValidationException
import com.example.demo.model.EncryptType.PASSWORD
import com.example.demo.model.RequestParameters
import com.example.demo.model.SoftwarePackage.SOFTWARE_A
import com.example.demo.utils.Encryptor
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class KeyValidatorTest {

    private val encryptor = mockk<Encryptor>()
    private val secretValidator = KeyValidator(encryptor)

    @Test
    fun `when supplied key matches generated key validator passes`() {
        every { encryptor.encryptString(any(), any())} returns "blah"
        val request = RequestParameters("Bob", "Smith", SOFTWARE_A, "blah")

        secretValidator.validate(request, PASSWORD)
    }

    @Test
    fun `when supplied key does not match generated key validator failes and exception is thrown`() {
        every { encryptor.encryptString(any(), any())} returns "blahblah"
        val request = RequestParameters("Bob", "Smith", SOFTWARE_A, "blah")

        shouldThrow<ValidationException> { secretValidator.validate(request, PASSWORD) }
    }

}