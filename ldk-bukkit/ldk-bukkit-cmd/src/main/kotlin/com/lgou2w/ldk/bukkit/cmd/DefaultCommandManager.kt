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

package com.lgou2w.ldk.bukkit.cmd

import org.bukkit.plugin.Plugin

/**
 * ## DefaultCommandManager (默认命令管理器)
 *
 * @see [SimpleCommandManager]
 * @author lgou2w
 */
class DefaultCommandManager(
        plugin: Plugin
) : SimpleCommandManager(plugin, DefaultCommandParser()) {

    override val parser : DefaultCommandParser
        get() = super.parser as DefaultCommandParser

    override fun registerCommand(source: Any): DefaultRegisteredCommand {
        val command = super.registerCommand(source) as DefaultRegisteredCommand
        command.isRegistered = true
        return command
    }
}
