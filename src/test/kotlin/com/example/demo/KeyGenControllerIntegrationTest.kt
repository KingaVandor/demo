package com.example.demo

import com.example.demo.model.EncryptType
import com.example.demo.model.RequestParameters
import com.example.demo.model.RequestType
import com.example.demo.model.SoftwarePackage
import com.example.demo.utils.Encryptor
import com.example.demo.validators.KeyValidator
import com.example.demo.validators.ParameterParser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest
internal class KeyGenControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var encryptor: Encryptor
    @MockkBean
    lateinit var secretValidator: KeyValidator
    @MockkBean
    lateinit var  parameterParser: ParameterParser

    private val requestKeyGen = RequestParameters("Bob", "Smith", SoftwarePackage.SOFTWARE_A, "password")
    private val requestKeyVal = RequestParameters("Bob", "Smith", SoftwarePackage.SOFTWARE_A, "licence")


    @BeforeEach
    fun `init`() {
        every { encryptor.encryptString(any(), eq(EncryptType.PASSWORD) ) } returns "password"
        every { encryptor.encryptString(any(), eq(EncryptType.LICENCE) ) } returns "licence"
        every { parameterParser.validateAndParseParameters(any(), any(), eq("password"), any(), eq(RequestType.KEYGEN)) } returns requestKeyGen
        every { parameterParser.validateAndParseParameters(any(), any(), eq("licenceA"), null, eq(RequestType.KEYVAL)) } returns requestKeyVal
        justRun { secretValidator.validate(any(), any()) }

    }

    @Test
    fun `When params are ok Then getLicenceKey sends back licence key with status code 200`() {
        val mvcResult: MvcResult = getLicenceRequestResponse("Bob", "Smith", "SOFTWARE_A", "password")
        assertEquals(200, mvcResult.response.status)
        assertEquals("licenceA", mvcResult.response.contentAsString)
    }

    @Test
    fun `When password is wrong Then getLicenceKey sends back 401`() {
        val mvcResult: MvcResult = getLicenceRequestResponse("Bob", "Smith", "SOFTWARE_A", "wrong_password")
        assertEquals(401, mvcResult.response.status)
    }

    @Test
    fun `When params are ok Then validateLicenceKey sends back status code 204`() {
        val mvcResult: MvcResult = getValidationRequestResponse("Bob", "Smith",  "licenceA")
        assertEquals(204, mvcResult.response.status)
    }

    @Test
    fun `When licence is wrong Then validateLicenceKey sends back 404`() {
        val mvcResult: MvcResult = getValidationRequestResponse("Bob", "Smith","wrong_licence")
        assertEquals(404, mvcResult.response.status)
    }


    private fun getLicenceRequestResponse(firstName: String, lastName: String, software: String, key: String): MvcResult {
        return mockMvc.perform(
            get("/licenceapi/getLicenceKey")
                .header("auth", key)
                .param("userFirstName", firstName)
                .param("userLastName", lastName)
                .param("softwarePackageName", software))
            .andReturn()
    }

    private fun getValidationRequestResponse(firstName: String, lastName: String, key: String): MvcResult {
        return mockMvc.perform(
            get("/licenceapi/validateLicenceKey")
                .header("auth", key)
                .param("userFirstName", firstName)
                .param("userLastName", lastName))
            .andReturn()
    }
}