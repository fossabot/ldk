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

package com.lgou2w.ldk.nbt

import java.io.DataInput
import java.io.DataOutput
import java.util.Arrays

/**
 * ## NBTTagIntArray (整数数组 NBT 标签)
 *
 * @see [IntArray]
 * @author lgou2w
 */
class NBTTagIntArray : NBTBase<IntArray> {

    @JvmOverloads
    constructor(name: String = "", value: IntArray = IntArray(0)) : super(name, value)
    constructor(value: IntArray = IntArray(0)) : super("", value)

    override val type = NBTType.TAG_INT_ARRAY

    override var value : IntArray
        get() {
            val value = IntArray(value0.size)
            for ((i, el) in value0.withIndex())
                value[i] = el
            return value
        }
        set(value) {
            val value0 = IntArray(value.size)
            for ((i, el) in value.withIndex())
                value0[i] = el
            super.value0 = value0
        }

    override fun read(input: DataInput) {
        val value = IntArray(input.readInt())
        (0 until value.size).forEach { value[it] = input.readInt() }
        super.value0 = value
    }

    override fun write(output: DataOutput) {
        output.writeInt(value0.size)
        (0 until value0.size).forEach { output.writeInt(value0[it]) }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other) && Arrays.equals((other as NBTTagIntArray).value0, value0)
    }

    override fun toString(): String {
        return "NBTTagIntArray(value=${value0.joinToString(",", "[", "]")})"
    }

    override fun clone(): NBTTagIntArray {
        return NBTTagIntArray(name, value)
    }

    override fun toJson(): String {
        return value0.joinToString(",", "[", "]")
    }

    override fun toMojangson(): String {
        val suffix = NBTType.TAG_INT.mojangsonSuffix
        return value0.joinToString("$suffix,", "[", "$suffix]")
    }

    override fun toMojangsonWithColor(): String {
        val suffix = "§c${NBTType.TAG_INT.mojangsonSuffix}§r"
        return value0.joinToString("$suffix, §6", "[§6", "$suffix]")
    }
}
