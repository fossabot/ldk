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

package com.lgou2w.ldk.coroutines

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * ## CachedThreadPoolDispatcherProvider (可缓存线程池调度程序提供者)
 *
 * @see [DispatcherProvider]
 * @see [Executors.newCachedThreadPool]
 * @since LDK 0.1.8-rc
 */
class CachedThreadPoolDispatcherProvider(
        private val threadName: String
) : DispatcherProvider {

    private val threadNo = AtomicInteger()
    private val createPoolThread : (Runnable) -> Thread = { r ->
        Thread(r, threadName + "-" + threadNo.incrementAndGet())
    }

    override val dispatcher : ExecutorCoroutineDispatcher
            = Executors.newCachedThreadPool(createPoolThread).asCoroutineDispatcher()

    override fun close() {
        dispatcher.close()
    }
}
