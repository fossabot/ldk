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

package com.lgou2w.ldk.exposed

import org.jetbrains.exposed.sql.Table
import java.util.UUID

/**
 * * Register as an unsigned UUID column type.
 * * 注册为无符号 UUID 列类型.
 *
 * @see [Table]
 * @see [UnsignedUUIDColumnType]
 * @since LDK 0.1.8-rc
 */
@JvmOverloads
fun Table.unsignedUUID(name: String = "", nullable: Boolean = false)
        = registerColumn<UUID>(name, UnsignedUUIDColumnType(nullable))
