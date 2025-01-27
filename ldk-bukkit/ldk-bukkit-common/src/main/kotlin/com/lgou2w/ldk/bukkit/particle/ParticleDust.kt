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

package com.lgou2w.ldk.bukkit.particle

import org.bukkit.Color

/**
 * ## ParticleDust (粒子尘埃)
 *
 * * Before the `1.13` version, you need to set the of `count` to `0` and the `speed` to not `0` then the color will take effect.
 * * 在 `1.13` 版本之前需要将 `count`  数量设置为 `0` 且将 `speed` 速度设置不为 `0` 那么颜色才会生效.
 *
 * @see [Particle.DUST]
 * @author lgou2w
 */
data class ParticleDust(
        /**
         * * The color of this particle dust.
         * * 此粒子尘埃的颜色.
         */
        val color: Color,
        /**
         * * The size of this particle dust.
         * * 此粒子尘埃的大小.
         */
        val size: Float = 1f
)
