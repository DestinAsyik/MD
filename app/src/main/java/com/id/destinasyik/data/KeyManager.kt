package com.id.destinasyik.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

class KeyManager {
    fun createKey(context: Context) {
        val keyManager = KeyStore.getInstance("AndroidKeyStore")
        keyManager.load(null)

        // Cek apakah kunci sudah ada
        if (!keyManager.containsAlias("myKeyAlias")) {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                "AndroidKeyStore"
            )

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                "myKeyAlias",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .build()

            keyPairGenerator.initialize(keyGenParameterSpec)
            keyPairGenerator.generateKeyPair()
        }
    }

    fun encryptData(data: String): String? {
        val keyManager = KeyStore.getInstance("AndroidKeyStore")
        keyManager.load(null)

        val publicKey: PublicKey = keyManager.getCertificate("myKeyAlias").publicKey

        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val encryptedByte = cipher.doFinal(data.toByteArray())
        val encryptedString = String(encryptedByte)
        return encryptedString
    }

    fun decryptData(encryptedText: String): String? {
        val encryptedData = Base64.decode(encryptedText, Base64.DEFAULT)
        val keyManager = KeyStore.getInstance("AndroidKeyStore")
        keyManager.load(null)

        val privateKey: PrivateKey = keyManager.getKey("myKeyAlias", null) as PrivateKey

        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        return String(cipher.doFinal(encryptedData))
    }

    fun getKey(): PrivateKey? {
        val keyManager = KeyStore.getInstance("AndroidKeyStore")
        keyManager.load(null)

        return keyManager.getKey("myKeyAlias", null) as? PrivateKey
    }
}