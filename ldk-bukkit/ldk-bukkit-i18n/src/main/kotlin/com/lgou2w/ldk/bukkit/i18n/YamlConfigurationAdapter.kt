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

package com.lgou2w.ldk.bukkit.i18n

import com.lgou2w.ldk.i18n.LanguageAdapter
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 * ## YamlConfigurationAdapter (Yaml 配置文件语言适配器)
 *
 * @see [LanguageAdapter]
 * @author lgou2w
 */
class YamlConfigurationAdapter @JvmOverloads constructor(
        /**
         * * The encoding of this yaml configuration file.
         * * 此 YAML 配置文件的编码.
         */
        val charset: Charset = Charsets.UTF_8
) : LanguageAdapter {

    override val fileExtension : String = "yml"

    override fun adapt(input: InputStream): Map<String, String> {
        val reader = InputStreamReader(input, charset)
        val yaml = YamlConfiguration.loadConfiguration(reader)
        return yaml.getKeys(true)
            .map { key -> key to yaml.get(key) }
            .filter { pair -> pair.second !is ConfigurationSection }
            .associate { it.first to it.second.toString() }
    }

    override fun readapt(output: OutputStream, entries: MutableMap<String, String>) {
        val yaml = YamlConfiguration()
        entries.forEach { yaml.set(it.key, it.value) }
        val data = yaml.saveToString()
        val writer = OutputStreamWriter(output, charset)
        writer.write(data)
        writer.flush()
    }
}
