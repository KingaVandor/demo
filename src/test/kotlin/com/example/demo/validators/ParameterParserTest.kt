package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import com.example.demo.model.RequestParameters
import com.example.demo.model.RequestType
import com.example.demo.model.SoftwarePackage
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ParameterParserTest {

    private val softwareValidator = mockk<SoftwarePackageValidator>()
    private val stringValidator = mockk<StringParameterValidator>()
    private val parser = ParameterParser(softwareValidator, stringValidator)

    @BeforeEach
    fun `init`() {
        every { softwareValidator.parseSoftwarePackageName(any()) } returns SoftwarePackage.SOFTWARE_A
        every { softwareValidator.getSoftwarePackageNameFromKey(any()) } returns SoftwarePackage.SOFTWARE_A
        every { stringValidator.validateString("Bob") } returns "Bob"
        every { stringValidator.validateString("Smith") } returns "Smith"
        every { stringValidator.validateString("SOFTWARE_A") } returns "SOFTWARE_A"
        every { stringValidator.validateString("blah", 200, false) } returns "blah"
    }

    @Test
    fun `when all validators pass, parser returns request object`() {
        val expectedRequest = RequestParameters("Bob", "Smith", SoftwarePackage.SOFTWARE_A, "blah")
        val actualRequest = parser.validateAndParseParameters("Bob", "Smith", "blah","SOFTWARE_A", RequestType.KEYGEN)

        assertEquals(expectedRequest, actualRequest)
    }

    @Test
    fun `when string validator throws, parser throws further`() {
        every { stringValidator.validateString(any()) } throws ValidationException("wrong param")
        val exception = shouldThrow<ValidationException> {  parser.validateAndParseParameters("Bob", "Smith", "blah","SOFTWARE_A", RequestType.KEYGEN) }

        assertEquals("wrong param", exception.message)
    }

    @Test
    fun `when software validator throws, parser throws further`() {
        every { softwareValidator.parseSoftwarePackageName(any()) } throws ValidationException("wrong param")
        every { softwareValidator.getSoftwarePackageNameFromKey(any()) } throws ValidationException("wrong param")
        val exception1 = shouldThrow<ValidationException> {  parser.validateAndParseParameters("Bob", "Smith", "blah","SOFTWARE_A", RequestType.KEYGEN) }
        val exception2 = shouldThrow<ValidationException> {  parser.validateAndParseParameters("Bob", "Smith", "blah","SOFTWARE_A", RequestType.KEYVAL) }

        assertEquals("wrong param", exception1.message)
        assertEquals("wrong param", exception2.message)
    }

    @Test
    fun `when RequestType is KEYVAL, parser drops last letter of key in request object`() {
        val expectedRequest = RequestParameters("Bob", "Smith", SoftwarePackage.SOFTWARE_A, "bla")
        val actualRequest = parser.validateAndParseParameters("Bob", "Smith", "blah",null, RequestType.KEYVAL)

        assertEquals(expectedRequest, actualRequest)
    }

    @Test
    fun `when RequestType is KEYVAL, parser calls relevant method key validator`() {
        parser.validateAndParseParameters("Bob", "Smith", "blah",null, RequestType.KEYVAL)

        verify(exactly = 1) {softwareValidator.getSoftwarePackageNameFromKey(any()) }

    }


}