package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SoftwarePackageValidatorTest {
    private val validator = SoftwarePackageValidator()

    @Test
    fun `when software package name can be parsed to the enum, validator passes`() {
        validator.parseSoftwarePackageName("SOFTWARE_A")
        validator.parseSoftwarePackageName("SOFTWARE_B")
    }

    @Test
    fun `when software package name correct but in wrong case or contains trailing space, validator passes`() {
        validator.parseSoftwarePackageName("SofTWARE_A")
        validator.parseSoftwarePackageName("SOFTwarE_b   ")
    }

    @Test
    fun `when software package name incorrect, validator throws`() {
        val exception = shouldThrow<IllegalArgumentException> { validator.parseSoftwarePackageName("SOFTWARE_WRONG") }
        assertEquals(
            "No enum constant com.example.demo.model.SoftwarePackage.SOFTWARE_WRONG",
            exception.message
        )
    }

    @Test
    fun `when software package abbreviation can be parsed to the enum, validator passes`() {
        validator.getSoftwarePackageNameFromKey("licenceKeyA")
        validator.getSoftwarePackageNameFromKey("licenceKeyXCRTH=?B")
    }

    @Test
    fun `when software package abbreviation incorrect, validator throws`() {
       shouldThrow<ValidationException> {  validator.getSoftwarePackageNameFromKey("licenceKeyX") }
    }

}