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

package com.lgou2w.ldk.common

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * ## LazyAnyOrNullClass (延迟加载任意或 `null` 类)
 *
 * * Used to lazy loading a any or `null` class.
 * * 用于延迟加载指定的任意或 `null` 类.
 *
 * > * `val clazz : Class<*>? by lazyAnyOrNullClass { Class.forName("404 Not Found") }`
 *
 * @see [lazyAnyOrNullClass]
 * @see [ReadOnlyProperty]
 * @see [Lazy]
 * @author lgou2w
 */
class LazyAnyOrNullClass(
        initializer: Callable<Class<*>?>
) : ReadOnlyProperty<Any, Class<*>?>,
        Lazy<Class<*>?> {

    private val lazyObj = lazy { initializer() }

    override fun getValue(thisRef: Any, property: KProperty<*>): Class<*>?
            = lazyObj.getValue(thisRef, property)

    override val value: Class<*>?
        get() = lazyObj.value

    override fun isInitialized(): Boolean
            = lazyObj.isInitialized()
}
