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

package com.lgou2w.ldk.reflect

import java.lang.reflect.AccessibleObject
import java.lang.reflect.Member
import java.lang.reflect.Modifier

/**
 * ## Visibility (可见性)
 *
 * @see [Modifier]
 * @see [java.lang.reflect.Constructor]
 * @see [java.lang.reflect.Method]
 * @see [java.lang.reflect.Field]
 * @author lgou2w
 */
enum class Visibility {

    PRIVATE,
    PROTECTED,
    PUBLIC,
    STATIC,
    FINAL,
    SYNCHRONIZED,
    VOLATILE,
    TRANSIENT,
    NATIVE,
    INTERFACE,
    ABSTRACT,
    STRICT,
    ;

    /**
     * * Gets whether this visibility is matched from the given accessible object.
     * * 从给定的可访问对象获取是否匹配此可见性.
     *
     * @param accessible Accessible object
     * @param accessible 可访问对象
     */
    fun <E> isMatches(accessible: E): Boolean where E : AccessibleObject, E : Member {
        val modifiers = accessible.modifiers
        return when (this) {
            PRIVATE -> Modifier.isPrivate(modifiers)
            PROTECTED -> Modifier.isProtected(modifiers)
            PUBLIC -> Modifier.isPublic(modifiers)
            STATIC -> Modifier.isStatic(modifiers)
            FINAL -> Modifier.isFinal(modifiers)
            SYNCHRONIZED -> Modifier.isSynchronized(modifiers)
            VOLATILE -> Modifier.isVolatile(modifiers)
            TRANSIENT -> Modifier.isTransient(modifiers)
            NATIVE -> Modifier.isNative(modifiers)
            INTERFACE -> Modifier.isInterface(modifiers)
            ABSTRACT -> Modifier.isAbstract(modifiers)
            STRICT -> Modifier.isStrict(modifiers)
        }
    }
}
