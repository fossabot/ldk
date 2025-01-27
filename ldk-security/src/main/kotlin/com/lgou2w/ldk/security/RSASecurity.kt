/*
 * Copyright (C) 2016-2019 The lgou2w <lgou2w@hotmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lgou2w.ldk.security

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.interfaces.RSAKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

/**
 * ## RSASecurity (RSA 安全类)
 *
 * @author lgou2w
 * @since LDK 0.1.9
 */
@Suppress("MemberVisibilityCanBePrivate")
class RSASecurity private constructor(
        /**
         * * The key object of this RSA.
         * * 此 RSA 的密钥对象.
         */
        val key: Key,
        /**
         * * The signature algorithm used by this RSA.
         * * 此 RSA 使用的签名算法.
         */
        val signatureAlgorithm: String
) {

    @Suppress("unused")
    companion object {

        const val RSA = "RSA"

        @Deprecated("UNSAFE")
        const val BIT_512 = 0x200
        @Deprecated("UNSAFE")
        const val BIT_1024 = 0x400
        const val BIT_2048 = 0x800
        const val BIT_3072 = 0xC00
        const val BIT_4096 = 0x1000
        const val BIT_7680 = 0x1E00
        const val BIT_15360 = 0x3C00

        const val SIGNATURE_MD2 = "MD2WithRSA"
        const val SIGNATURE_MD5 = "MD5WithRSA"
        const val SIGNATURE_SHA1 = "SHA1WithRSA"
        const val SIGNATURE_SHA224 = "SHA224WithRSA"
        const val SIGNATURE_SHA256 = "SHA256WithRSA"
        const val SIGNATURE_SHA384 = "SHA384WithRSA"
        const val SIGNATURE_SHA512 = "SHA512WithRSA"

        private const val PKCS8_PRIVATE_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----"
        private const val PKCS8_PRIVATE_KEY_END = "-----END PRIVATE KEY-----"
        private const val X509_PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----"
        private const val X509_PUBLIC_KEY_END = "-----END PUBLIC KEY-----"
        private const val NEWLINE = "\r"
        private const val NEWLINE2 = "\n"
        private const val EMPTY = ""

        /**
         * * Create a security class object from the given RSA key [rsaKey].
         * * 从给定的 RSA 密钥 [rsaKey] 创建安全类对象.
         *
         * @throws [IllegalArgumentException] If [rsaKey] is not an RSA key.
         * @throws [IllegalArgumentException] 如果 [rsaKey] 不是 RSA 密钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKey(rsaKey: Key, signatureAlgorithm: String): RSASecurity {
            return RSASecurity(rsaKey, signatureAlgorithm)
        }

        /**
         * * Create a security class object from the `Base64` encoded public or private key of the given `PKCS#8` or `X.509`.
         * * 从给定的 `PKCS#8` 或 `X.509` 的 `Base64` 编码公私钥创建安全类对象.
         *
         * @throws [IllegalArgumentException] If it is not a valid public and private key.
         * @throws [IllegalArgumentException] 如果不是有效的公私钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedKey(pkcs8OrX509Base64EncodedKey: String, signatureAlgorithm: String): RSASecurity {
            val key = decodeKey(pkcs8OrX509Base64EncodedKey)
            return RSASecurity(key, signatureAlgorithm)
        }

        /**
         * * Create a security class object from the given `PKCS#8` unencrypted `Base64` encoded private key.
         * * 从给定的 `PKCS#8` 未加密 `Base64` 编码私钥创建安全类对象.
         *
         * @throws [IllegalArgumentException] If it is not a valid private key.
         * @throws [IllegalArgumentException] 如果不是有效的私钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey: String, signatureAlgorithm: String): RSASecurity {
            val privateKey = decodePrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey)
            return RSASecurity(privateKey, signatureAlgorithm)
        }

        /**
         * * Create a security class object from the given `PKCS#8` unencrypted private key.
         * * 从给定的 `PKCS#8` 未加密私钥创建安全类对象.
         *
         * @throws [IllegalArgumentException] If it is not a valid private key.
         * @throws [IllegalArgumentException] 如果不是有效的私钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPrivateKey(pkcs8EncodedPrivateKey: ByteArray, signatureAlgorithm: String): RSASecurity {
            val privateKey = decodePrivateKey(pkcs8EncodedPrivateKey)
            return RSASecurity(privateKey, signatureAlgorithm)
        }

        /**
         * * Create a security class object from the `Base64` encoded public key of the given `X.509`.
         * * 从给定的 `X.509` 的 `Base64` 编码公钥创建安全类对象.
         *
         * @throws [IllegalArgumentException] If it is not a valid public key.
         * @throws [IllegalArgumentException] 如果不是有效的公钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPublicKey(x509Base64EncodedPublicKey: String, signatureAlgorithm: String): RSASecurity {
            val publicKey = decodePublicKey(x509Base64EncodedPublicKey)
            return RSASecurity(publicKey, signatureAlgorithm)
        }

        /**
         * * Create a security class object from the given X.509 public key.
         * * 从给定的 X.509 公钥创建安全类对象.
         *
         * @throws [IllegalArgumentException] If it is not a valid public key.
         * @throws [IllegalArgumentException] 如果不是有效的公钥.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromEncodedPublicKey(x509EncodedPublicKey: ByteArray, signatureAlgorithm: String): RSASecurity {
            val publicKey = decodePublicKey(x509EncodedPublicKey)
            return RSASecurity(publicKey, signatureAlgorithm)
        }

        /**
         * * Create a security class from the given key pair [rsaKeyPair].
         * * 从给定的密钥对 [rsaKeyPair] 创建安全类.
         *
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKeyPair(rsaKeyPair: KeyPair, signatureAlgorithm: String): Pair<RSASecurity, RSASecurity> {
            val rsaPrivateSecurity = RSASecurity(rsaKeyPair.private, signatureAlgorithm)
            val rsaPublicSecurity = RSASecurity(rsaKeyPair.public, signatureAlgorithm)
            return rsaPrivateSecurity to rsaPublicSecurity
        }

        /**
         * * Create a security class from the given [bit] length with the key pair generator.
         * * 从给定的位长度 [bit] 用密钥对生成器创建安全类.
         *
         * @throws [IllegalArgumentException] If the bit length [bit] is less than [BIT_512].
         * @throws [IllegalArgumentException] 如果位长度 [bit] 小于 [BIT_512].
         * @throws [IllegalArgumentException] If the key pair generator is not available.
         * @throws [IllegalArgumentException] 如果密钥对生成器不可用.
         * @throws [IllegalArgumentException] If [signatureAlgorithm] is invalid.
         * @throws [IllegalArgumentException] 如果 [signatureAlgorithm] 是无效的.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromKeyPairGenerator(bit: Int, signatureAlgorithm: String): Pair<RSASecurity, RSASecurity> {
            @Suppress("DEPRECATION")
            if (bit < BIT_512)
                throw IllegalArgumentException("RSA keys must be at least 512 bits long.")
            val rsaKeyPair = try {
                val keyPairGenerator = KeyPairGenerator.getInstance(RSA)
                keyPairGenerator.initialize(bit, SecureRandom())
                keyPairGenerator.genKeyPair()
            } catch (e: Exception) {
                throw IllegalArgumentException("Unable to generate key pair.", e)
            }
            return fromKeyPair(rsaKeyPair, signatureAlgorithm)
        }

        /**
         * * Decode the `Base64` encoded public or private key of the given `PKCS#8` or `X.509`.
         * * 将给定的 `PKCS#8` 或 `X.509` 的 `Base64` 编码公私钥进行解码.
         *
         * @throws [IllegalArgumentException] If it is not a valid public and private key.
         * @throws [IllegalArgumentException] 如果不是有效的公私钥.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodeKey(pkcs8OrX509Base64EncodedKey: String): Key {
            val noNewline = pkcs8OrX509Base64EncodedKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            return if (noNewline.startsWith(PKCS8_PRIVATE_KEY_BEGIN) && noNewline.endsWith(PKCS8_PRIVATE_KEY_END))
                decodePrivateKey(noNewline)
            else if (noNewline.startsWith(X509_PUBLIC_KEY_BEGIN) && noNewline.endsWith(X509_PUBLIC_KEY_END))
                decodePublicKey(noNewline)
            else throw IllegalArgumentException("Invalid private or public key.")
        }

        /**
         * * Decode the given `PKCS#8` unencrypted `Base64` encoded private key.
         * * 将给定的 `PKCS#8` 未加密 `Base64` 编码私钥进行解码.
         *
         * @throws [IllegalArgumentException] If it is not a valid private key.
         * @throws [IllegalArgumentException] 如果不是有效的私钥.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePrivateKey(pkcs8NotEncryptedBase64EncodedPrivateKey: String): PrivateKey {
            val noNewline = pkcs8NotEncryptedBase64EncodedPrivateKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            if (!noNewline.startsWith(PKCS8_PRIVATE_KEY_BEGIN) ||
                !noNewline.endsWith(PKCS8_PRIVATE_KEY_END)) {
                throw IllegalArgumentException("Invalid private key header or end.")
            }
            val base64EncodedKey = noNewline
                .replace(PKCS8_PRIVATE_KEY_BEGIN, EMPTY)
                .replace(PKCS8_PRIVATE_KEY_END, EMPTY)
            val pkcs8EncodedPrivateKey = Base64.getDecoder().decode(base64EncodedKey)
            return decodePrivateKey(pkcs8EncodedPrivateKey)
        }

        /**
         * * Decode the `Base64` encoded public key of the given `X.509`.
         * * 将给定的 `X.509` 的 `Base64` 编码公钥进行解码.
         *
         * @throws [IllegalArgumentException] If it is not a valid public key.
         * @throws [IllegalArgumentException] 如果不是有效的公钥.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePublicKey(x509Base64EncodedPublicKey: String): PublicKey {
            val noNewline = x509Base64EncodedPublicKey.replace(NEWLINE, EMPTY).replace(NEWLINE2, EMPTY)
            if (!noNewline.startsWith(X509_PUBLIC_KEY_BEGIN) ||
                !noNewline.endsWith(X509_PUBLIC_KEY_END)) {
                throw IllegalArgumentException("Invalid public key header or end.")
            }
            val base64EncodedKey = noNewline
                .replace(X509_PUBLIC_KEY_BEGIN, EMPTY)
                .replace(X509_PUBLIC_KEY_END, EMPTY)
            val x509EncodedPublicKey = Base64.getDecoder().decode(base64EncodedKey)
            return decodePublicKey(x509EncodedPublicKey)
        }

        /**
         * * Decode the given `PKCS#8` unencrypted private key.
         * * 将给定的 `PKCS#8` 未加密私钥进行解码.
         *
         * @throws [IllegalArgumentException] If it is not a valid private key.
         * @throws [IllegalArgumentException] 如果不是有效的私钥.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePrivateKey(pkcs8EncodedPrivateKey: ByteArray): PrivateKey {
            return try {
                KeyFactory.getInstance(RSA).generatePrivate(PKCS8EncodedKeySpec(pkcs8EncodedPrivateKey))
            } catch (e: GeneralSecurityException) {
                throw IllegalArgumentException("Not a valid PKCS#8 format private key.", e)
            }
        }

        /**
         * * Decode the given `X.509` public key.
         * * 将给定的 `X.509` 公钥进行解码.
         *
         * @throws [IllegalArgumentException] If it is not a valid public key.
         * @throws [IllegalArgumentException] 如果不是有效的公钥.
         */
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun decodePublicKey(x509EncodedPublicKey: ByteArray): PublicKey {
            return try {
                KeyFactory.getInstance(RSA).generatePublic(X509EncodedKeySpec(x509EncodedPublicKey))
            } catch (e: GeneralSecurityException) {
                throw IllegalArgumentException("Not a valid X.509 format public key.", e)
            }
        }
    }

    /**
     * * RSA key bit length.
     * * RSA 密钥位长度.
     */
    val bit : Int

    init {

        // The key must be an RSA public or private key
        if (key !is RSAKey)
            throw IllegalArgumentException("Not a valid RSA public or private key.")

        // Perform a digital signature algorithm detection
        try {
            Signature.getInstance(signatureAlgorithm)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("Invalid signature algorithm: $signatureAlgorithm", e)
        }

        this.bit = key.modulus.bitLength()
    }

    /**************************************************************************
     *
     * Encrypt or Decrypt
     *
     **************************************************************************/

    private val decryptBlockSize = (bit shr 3)
    private val encryptBlockSize = (bit shr 3) - 0x0B

    /**
     * * Encrypt the given [data].
     * * 将给定的数据 [data] 进行加密.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun encrypt(data: ByteArray) = runEncryption(Cipher.ENCRYPT_MODE, encryptBlockSize, data)

    /**
     * * Decrypt the given [data].
     * * 将给定的数据 [data] 进行解密.
     *
     * @throws [IOException] I/O
     */
    @Throws(IOException::class)
    fun decrypt(data: ByteArray) = runEncryption(Cipher.DECRYPT_MODE, decryptBlockSize, data)

    @Throws(IOException::class)
    private fun runEncryption(mode: Int, blockSize: Int, data: ByteArray): ByteArray = try {
        val cipher = Cipher.getInstance(RSA).apply { init(mode, key) }
        val dataLength = data.size
        val output = ByteArrayOutputStream()
        var cache: ByteArray
        var offset = 0
        var index = 0
        while (dataLength - offset > 0) {
            cache = if (dataLength - offset > blockSize) cipher.doFinal(data, offset, blockSize)
            else cipher.doFinal(data, offset, dataLength - offset)
            output.write(cache)
            offset = ++index * blockSize
        }
        val result = output.toByteArray()
        output.close()
        result
    } catch (e: GeneralSecurityException) {
        throw IOException("Unable to run encryption.", e)
    }

    /**************************************************************************
     *
     * Signature or Verify
     *
     **************************************************************************/

    /**
     * * Indicates whether this RSA [key] is a private key.
     * * 表示此 RSA 密钥 [key] 是否为私钥.
     */
    val isPrivateKey = key is PrivateKey

    /**
     * * The given [data] is signed with the [key].
     * * 将给定的数据 [data] 使用密钥 [key] 进行签名.
     *
     * @throws [UnsupportedOperationException] If the key [key] is not a private key.
     * @throws [UnsupportedOperationException] 如果密钥 [key] 不是私钥.
     * @throws [IOException] I/O
     */
    @Throws(IOException::class, UnsupportedOperationException::class)
    fun signature(data: ByteArray): ByteArray {
        if (!isPrivateKey)
            throw UnsupportedOperationException("Only the RSA private key can be signature.")
        return try {
            val signature = Signature.getInstance(signatureAlgorithm)
            signature.initSign(key as PrivateKey)
            signature.update(data)
            signature.sign()
        } catch (e: Exception) {
            throw IOException("Unable to signature the data.", e)
        }
    }

    /**
     * * Verify the given [data] and [sign].
     * * 将给定的数据 [data] 和签名 [sign] 进行验证.
     *
     * @throws [UnsupportedOperationException] If the key [key] is not a public key.
     * @throws [UnsupportedOperationException] 如果密钥 [key] 不是公钥.
     * @throws [IOException] I/O
     */
    @Throws(UnsupportedOperationException::class)
    fun verify(data: ByteArray, sign: ByteArray): Boolean {
        if (isPrivateKey)
            throw UnsupportedOperationException("Only the RSA public key can be signature verify.")
        return try {
            val signature = Signature.getInstance(signatureAlgorithm)
            signature.initVerify(key as PublicKey)
            signature.update(data)
            signature.verify(sign)
        } catch (e: Exception) {
            false // if error
        }
    }

    /**************************************************************************
     *
     * Important
     *
     **************************************************************************/

    override fun toString(): String {
        return "RSASecurity(bit=$bit, signature=$signatureAlgorithm, isPrivateKey=$isPrivateKey)"
    }
}
