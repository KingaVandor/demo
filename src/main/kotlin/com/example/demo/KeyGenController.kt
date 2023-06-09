package com.example.demo

import com.example.demo.model.EncryptType.LICENCE
import com.example.demo.model.EncryptType.PASSWORD
import com.example.demo.model.RequestType.KEYGEN
import com.example.demo.model.RequestType.KEYVAL
import com.example.demo.utils.Encryptor
import com.example.demo.validators.KeyValidator
import com.example.demo.validators.ParameterParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class KeyGenController(
    private val keyValidator: KeyValidator,
    private val encryptor: Encryptor,
    private val paramValidator: ParameterParser
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/licenceapi/getLicenceKey")
    fun getLicenceKey(
        @RequestHeader("secret") secretKey: String,
        @RequestParam(name = "userFirstName") userFirstName: String,
        @RequestParam(name = "userLastName") userLastName: String,
        @RequestParam(name = "softwarePackageName") softwarePackageName: String,
    ): ResponseEntity<String> {
        return try {
            val request = paramValidator.validateAndParseParameters(userFirstName, userLastName, secretKey, softwarePackageName, KEYGEN)
            keyValidator.validate(request, PASSWORD)

            val softwareAbbreviation = request.software.abbreviation
            val stringToEncrypt = "${request.firstName}-${request.firstName}-${request.software}"
            val licenceKey = encryptor.encryptString(stringToEncrypt, LICENCE) + softwareAbbreviation

            ResponseEntity(licenceKey, HttpStatus.OK)

        } catch (e: Exception) {
            logger.info(e.toString())
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
    }

    @GetMapping("/licenceapi/validateLicenceKey")
    fun validateLicenceKey(
        @RequestHeader("secret") licenceKey: String,
        @RequestParam(name = "userFirstName") userFirstName: String,
        @RequestParam(name = "userLastName") userLastName: String,
    ): ResponseEntity<String> {
        return try {
            val request = paramValidator.validateAndParseParameters(userFirstName, userLastName, licenceKey, null, KEYVAL)
            keyValidator.validate(request, LICENCE)
            ResponseEntity(HttpStatus.NO_CONTENT)

        } catch (e: Exception) {
            logger.info(e.toString())
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}