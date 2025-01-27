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

package com.lgou2w.ldk.i18n

import java.util.Locale

/**
 * ## Formatter (格式化)
 *
 * @see [StringFormatter]
 * @see [MessageFormatter]
 * @author lgou2w
 */
interface Formatter {

    /**
     * * Format the given [value] with [locale] and [args].
     * * 将给定的值 [value] 用本地化 [locale] 和参数 [args] 进行格式化处理.
     */
    fun format(locale: Locale, value: String, vararg args: Any?) : String
}
