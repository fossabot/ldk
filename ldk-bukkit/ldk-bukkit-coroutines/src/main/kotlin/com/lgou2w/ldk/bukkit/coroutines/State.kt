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

package com.lgou2w.ldk.bukkit.coroutines

import org.bukkit.Bukkit

/**
 * ## State (Bukkit 线程状态)
 *
 * @see [currentState]
 * @author lgou2w
 */
enum class State {

    SYNC,
    ASYNC,
    ;

    companion object {
        /**
         * * Gets whether the current thread is the Bukkit main thread state.
         * * 获取当前线程是否为 Bukkit 主线程状态.
         *
         * @see [Bukkit.isPrimaryThread]
         */
        @JvmStatic
        fun currentState(): State
                = if (Bukkit.isPrimaryThread()) SYNC else ASYNC
    }
}
