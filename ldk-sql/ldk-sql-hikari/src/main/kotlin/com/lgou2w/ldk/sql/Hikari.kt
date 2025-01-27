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

package com.lgou2w.ldk.sql

import java.util.Collections

/**
 * * Build the HikariCP configuration object.
 * * 构建 HikariCP 配置对象.
 *
 * @see [HikariConfiguration]
 * @see [HikariConfigurationBuilder]
 */
fun buildConfiguration(block: HikariConfigurationBuilder.() -> Unit) : HikariConfiguration {
    val builder = HikariConfigurationBuilder().also(block)
    return HikariConfiguration(
            builder.poolName,
            builder.address!!,
            builder.database!!,
            builder.username!!,
            builder.password!!,
            builder.maxPoolSize,
            builder.minIdleConnections,
            builder.maxLifetime,
            builder.connectionTimeout,
            Collections.unmodifiableMap(builder.properties)
    )
}
