package com.example.demo.utils

import com.example.demo.model.EncryptType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class EncryptorTest {
    private val encryptor = Encryptor("lic", "pas", "sal")

    @Test
    fun `a string can be encrypted and decrypted if the same encryption secret is used`() {
        val stringToEncrypt = "Bob-Smith-SOFTWARE_A"
        val encrypted = encryptor.encryptString(stringToEncrypt, EncryptType.LICENCE)
        assertEquals("m9RxMQh/aVJED8DVaSKh5mEESRb7iRStKXRlsVv7Wss=", encrypted)

        val plainText = encryptor.decryptString(encrypted, EncryptType.LICENCE)
        assertEquals(stringToEncrypt, plainText)
    }

    @Test
    fun `different strings return different values when encrypted`() {
        val stringToEncrypt1 = "Bob-Smith-SOFTWARE_A"
        val stringToEncrypt2 = "Bob-Smith-SOFTWARE_B"
        val encrypted1 = encryptor.encryptString(stringToEncrypt1, EncryptType.LICENCE)
        val encrypted2 = encryptor.encryptString(stringToEncrypt2, EncryptType.LICENCE)
        assertNotEquals(encrypted1, encrypted2)
    }

    @Test
    fun `different encryption secrets return different encrypted strings`() {
        val stringToEncrypt = "Bob-Smith-SOFTWARE_A"
        val encrypted1 = encryptor.encryptString(stringToEncrypt, EncryptType.LICENCE)
        val encrypted2 = encryptor.encryptString(stringToEncrypt, EncryptType.PASSWORD)
        assertNotEquals(encrypted1, encrypted2)
    }
}