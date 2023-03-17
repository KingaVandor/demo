package com.example.demo.validators

import com.example.demo.exceptions.ValidationException
import com.example.demo.model.EncryptType
import com.example.demo.model.RequestParameters
import com.example.demo.utils.Encryptor
import org.springframework.stereotype.Component

@Component
class KeyValidator(private val encryptor: Encryptor) {

    fun validate(request: RequestParameters, type: EncryptType) {
        val stringToEncrypt = "${request.firstName}-${request.firstName}-${request.software}"

        val generatedSecret = generateSecretKey(stringToEncrypt, type)
        println(generatedSecret)
        if (generatedSecret != request.key) throw ValidationException("Key not valid for parameters $stringToEncrypt")
    }

    private fun generateSecretKey(stringToEncrypt: String, type: EncryptType): String {
       return encryptor.encryptString(stringToEncrypt, type)
    }
}