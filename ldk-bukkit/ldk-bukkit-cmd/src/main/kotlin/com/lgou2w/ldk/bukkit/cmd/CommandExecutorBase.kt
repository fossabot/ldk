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

import org.bukkit.permissions.PermissionDefault
import java.util.Arrays

/**
 * ## CommandExecutorBase (命令执行器基础)
 *
 * @see [CommandExecutor]
 * @see [DefaultCommandExecutor]
 * @author lgou2w
 */
abstract class CommandExecutorBase(
        final override val reference: Any,
        final override val name: String,
        final override val aliases: Array<out String>,
        final override val permission: Array<out String>?,
        final override val permissionDefault: PermissionDefault?,
        final override val sorted: Int?,
        final override val isPlayable: Boolean,
        final override val parameters: Array<out CommandExecutor.Parameter>
) : CommandExecutor {

    /**
     * * The length of the parameter of this executor.
     * * 此执行器的参数长度.
     */
    val length = parameters.size
    /**
     * * The maximum parameter length of this executor.
     * * 此执行器的最大参数长度.
     */
    val max = length
    /**
     * * The minimum parameter length of this executor.
     * * 此执行器的最小参数长度.
     */
    val min = max - parameters.count(CommandExecutor.Parameter::canNullable)

    override var description: String? = null

    override fun hashCode(): Int {
        var result = reference.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + Arrays.hashCode(aliases)
        result = 31 * result + Arrays.hashCode(permission)
        result = 31 * result + isPlayable.hashCode()
        result = 31 * result + Arrays.hashCode(parameters)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true
        if (other is CommandExecutorBase)
            return reference == other.reference &&
                   name == other.name &&
                   Arrays.equals(aliases, other.aliases) &&
                   Arrays.equals(permission, other.permission) &&
                   isPlayable == other.isPlayable &&
                   Arrays.equals(parameters, other.parameters)
        return false
    }
}
