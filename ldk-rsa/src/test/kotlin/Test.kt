/*
 * Copyright (C) 2018 The lgou2w (lgou2w@hotmail.com)
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

import com.lgou2w.ldk.rsa.RSAUtils
import org.junit.Ignore
import org.junit.Test
import java.io.File

class Test {

    @Test
    @Ignore // ignore
    fun test_rsa() {

        val dir = File("../key")
        val privateKeyFile = File(dir, "pkcs8_private.key")
        val publicKeyFile = File(dir, "public.key")

        val privateKey = RSAUtils.decodePrivateKey(privateKeyFile.readBytes())
        val publicKey = RSAUtils.decodePublicKey(publicKeyFile.readBytes())

        val encrypted = RSAUtils.encrypt(privateKey, "Hi, I'm server.".toByteArray())
        println("Encrypted: ${encrypted.toString(Charsets.UTF_8)}")
        val decrypted = RSAUtils.decrypt(publicKey, encrypted)
        println("Decrypted: " + decrypted.toString(Charsets.UTF_8))
    }

    @Test
    @Ignore // ignore
    fun test_rsa_sign() {

        val desktop = File("../desktop")
        val dir = File(desktop,  "key/")

        val publicKeyFile = File(dir, "public.key")
        val publicKey = RSAUtils.decodePublicKey(publicKeyFile.readBytes())

        val software = File(desktop, "software.exe")
        val signature = File(desktop, "software.exe.signature")
        val result = RSAUtils.verify(publicKey, software, signature)

        println("Verify: $result")
    }
}