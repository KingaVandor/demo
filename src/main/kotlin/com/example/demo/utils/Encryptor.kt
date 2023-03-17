package com.example.demo.utils

import com.example.demo.model.EncryptType
import com.example.demo.model.EncryptType.LICENCE
import com.example.demo.model.EncryptType.PASSWORD
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


@Component
class Encryptor(@Value("\${licence}") private val licence: String, @Value("\${password}") private val password: String, @Value("\${salt}") private val salt: String) {

    fun encryptString(input: String, type: EncryptType): String {
        return encrypt(input, getSecret(type))
    }

    fun decryptString(input: String, type: EncryptType): String {
        return decrypt(input, getSecret(type))
    }

    private fun encrypt(input: String, secret: String): String {
        val key = getKeyFromSecret(secret, salt)
        val algorithm = key.algorithm
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val cipherText = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(cipherText)
    }

    private fun decrypt(cipherText: String, secret: String): String {
        val key = getKeyFromSecret(secret, salt)
        val algorithm = key.algorithm
        val cipher = Cipher.getInstance(algorithm)

        cipher.init(Cipher.DECRYPT_MODE, key)
        val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(plainText)
    }

    private fun getKeyFromSecret(secret: String, salt: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(secret.toCharArray(), salt.toByteArray(), 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    private fun getSecret(type: EncryptType): String {
        return  when (type) {
            PASSWORD -> password
            LICENCE -> licence }
    }
}