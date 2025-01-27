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

@file:Suppress("UNUSED")

package com.lgou2w.ldk.common

/**************************************************************************
 *
 * Type alias
 *
 **************************************************************************/

/**
 * * Predicate, conditional function type alias.
 * * 谓词, 条件函数类型别名.
 */
typealias Predicate<T> = (T) -> Boolean

/**
 * * Predicate of suspend, conditional function type alias.
 * * 挂起的谓词, 条件函数类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendPredicate<T> = suspend (T) -> Boolean

/**
 * * Function, with parameters and returned type aliases.
 * * 函数, 具有参数和返回的类型别名.
 */
typealias Function<T, R> = (T) -> R

/**
 * * Function of suspend, with parameters and returned type aliases.
 * * 挂起函数, 具有参数和返回的类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendFunction<T, R> = suspend (T) -> R

/**
 * * Double-parameter function, with double arguments and returned type aliases.
 * * 双参函数, 具有双参和返回的类型别名.
 */
typealias BiFunction<T, U, R> = (T, U) -> R

/**
 * * Double-parameter function of suspend, with double arguments and returned type aliases.
 * * 挂起双参函数, 具有双参和返回的类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendBiFunction<T, U, R> = suspend (T, U) -> R

/**
 * * Consumer type aliases.
 * * 消费者类型别名.
 */
typealias Consumer<T> = (T) -> Unit

/**
 * * Consumer type aliases of suspend.
 * * 挂起消费者类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendConsumer<T> = suspend (T) -> Unit

/**
 * * Applicator type aliases.
 * * 应用器类型名称.
 */
typealias Applicator<T> = T.() -> Unit

/**
 * * Applicator type aliases of suspend.
 * * 挂起应用器类型名称.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendApplicator<T> = suspend T.() -> Unit

/**
 * * The applicator function, with the type alias returned.
 * * 应用器函数, 具有返回的类型别名.
 */
typealias ApplicatorFunction<T, R> = T.() -> R

/**
 * * The applicator function of suspend, with the type alias returned.
 * * 挂起应用器函数, 具有返回的类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendApplicatorFunction<T, R> = suspend T.() -> R

/**
 * * Callable type aliases.
 * * 可回调类型别名.
 */
typealias Callable<T> = () -> T

/**
 * * Callable type aliases of suspend.
 * * 挂起可回调类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendCallable<T> = suspend () -> T

/**
 * * Runnable type aliases.
 * * 可运行类型别名.
 */
typealias Runnable = () -> Unit

/**
 * * Runnable type aliases of suspend.
 * * 挂起可运行类型别名.
 *
 * @since LDK 0.1.7-rc4
 */
typealias SuspendRunnable = suspend () -> Unit

/**************************************************************************
 *
 * Predicate Extended
 *
 * @see java.util.function.Predicate
 *
 **************************************************************************/

/**
 * @see [java.util.function.Predicate.and]
 */
inline infix fun <T> Predicate<T>.and(crossinline other: Predicate<T>): Predicate<T>
        = { t : T -> invoke(t) && other.invoke(t) }

/**
 * @see [java.util.function.Predicate.and]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> Predicate<T>.and(crossinline other: SuspendPredicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) && other.invoke(t) }

/**
 * @see [java.util.function.Predicate.and]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendPredicate<T>.and(crossinline other: Predicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) && other.invoke(t) }

/**
 * @see [java.util.function.Predicate.and]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendPredicate<T>.and(crossinline other: SuspendPredicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) && other.invoke(t) }

/**
 * @see [java.util.function.Predicate.or]
 */
inline infix fun <T> Predicate<T>.or(crossinline other: Predicate<T>): Predicate<T>
        = { t : T -> invoke(t) || other.invoke(t) }

/**
 * @see [java.util.function.Predicate.or]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> Predicate<T>.or(crossinline other: SuspendPredicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) || other.invoke(t) }

/**
 * @see [java.util.function.Predicate.or]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendPredicate<T>.or(crossinline other: Predicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) || other.invoke(t) }

/**
 * @see [java.util.function.Predicate.or]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendPredicate<T>.or(crossinline other: SuspendPredicate<T>): SuspendPredicate<T>
        = { t : T -> invoke(t) || other.invoke(t) }

/**
 * @see [java.util.function.Predicate.negate]
 */
fun <T> Predicate<T>.negate(): Predicate<T>
        = { t : T -> !invoke(t) }

/**
 * @see [java.util.function.Predicate.negate]
 * @since LDK 0.1.7-rc4
 */
fun <T> Predicate<T>.negateSuspend(): SuspendPredicate<T>
        = { t : T -> !invoke(t) }

/**
 * @see [java.util.function.Predicate.negate]
 * @since LDK 0.1.7-rc4
 */
fun <T> SuspendPredicate<T>.negate(): SuspendPredicate<T>
        = { t : T -> !invoke(t) }

/**************************************************************************
 *
 * Function and BiFunction Extended
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 *
 **************************************************************************/

/**
 * @see [java.util.function.Function.compose]
 */
inline infix fun <T, R, V> Function<T, R>.compose(crossinline before: Function<V, T>): Function<V, R>
        = { v : V -> invoke(before.invoke(v)) }

/**
 * @see [java.util.function.Function.compose]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, R, V> Function<T, R>.compose(crossinline before: SuspendFunction<V, T>): SuspendFunction<V, R>
        = { v : V -> invoke(before.invoke(v)) }

/**
 * @see [java.util.function.Function.compose]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, R, V> SuspendFunction<T, R>.compose(crossinline before: Function<V, T>): SuspendFunction<V, R>
        = { v : V -> invoke(before.invoke(v)) }

/**
 * @see [java.util.function.Function.compose]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, R, V> SuspendFunction<T, R>.compose(crossinline before: SuspendFunction<V, T>): SuspendFunction<V, R>
        = { v : V -> invoke(before.invoke(v)) }

/**
 * @see [java.util.function.Function.andThen]
 */
inline infix fun <T, R, V> Function<T, R>.andThen(crossinline after: Function<R, V>): Function<T, V>
        = { t : T -> after.invoke(invoke(t)) }

/**
 * @see [java.util.function.Function.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, R, V> Function<T, R>.andThen(crossinline after: SuspendFunction<R, V>): SuspendFunction<T, V>
        = { t : T -> after.invoke(invoke(t)) }

/**
 * @see [java.util.function.Function.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, R, V> SuspendFunction<T, R>.andThen(crossinline after: SuspendFunction<R, V>): SuspendFunction<T, V>
        = { t : T -> after.invoke(invoke(t)) }

/**
 * @see [java.util.function.BiFunction.andThen]
 */
inline infix fun <T, U, R, V> BiFunction<T, U, R>.andThen(crossinline after: Function<R, V>): BiFunction<T, U, V>
        = { t : T, u : U -> after.invoke(invoke(t, u)) }

/**
 * @see [java.util.function.BiFunction.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, U, R, V> BiFunction<T, U, R>.andThen(crossinline after: SuspendFunction<R, V>): SuspendBiFunction<T, U, V>
        = { t : T, u : U -> after.invoke(invoke(t, u)) }

/**
 * @see [java.util.function.BiFunction.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, U, R, V> SuspendBiFunction<T, U, R>.andThen(crossinline after: Function<R, V>): SuspendBiFunction<T, U, V>
        = { t : T, u : U -> after.invoke(invoke(t, u)) }

/**
 * @see [java.util.function.BiFunction.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T, U, R, V> SuspendBiFunction<T, U, R>.andThen(crossinline after: SuspendFunction<R, V>): SuspendBiFunction<T, U, V>
        = { t : T, u : U -> after.invoke(invoke(t, u)) }

/**************************************************************************
 *
 * Consumer Extended
 *
 * @see [java.util.function.Consumer]
 *
 **************************************************************************/

/**
 * @see [java.util.function.Consumer.andThen]
 */
inline infix fun <T> Consumer<T>.andThenConsume(crossinline after: Consumer<T>): Consumer<T>
        = { t : T -> invoke(t); after.invoke(t) }

/**
 * @see [java.util.function.Consumer.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> Consumer<T>.andThenConsume(crossinline after: SuspendConsumer<T>): SuspendConsumer<T>
        = { t : T -> invoke(t); after.invoke(t) }

/**
 * @see [java.util.function.Consumer.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendConsumer<T>.andThenConsume(crossinline after: Consumer<T>): SuspendConsumer<T>
        = { t : T -> invoke(t); after.invoke(t) }

/**
 * @see [java.util.function.Consumer.andThen]
 * @since LDK 0.1.7-rc4
 */
inline infix fun <T> SuspendConsumer<T>.andThenConsume(crossinline after: SuspendConsumer<T>): SuspendConsumer<T>
        = { t : T -> invoke(t); after.invoke(t) }

/**************************************************************************
 *
 * Extended functions
 *
 **************************************************************************/

/**
 * * Returns `null` if the receiver is `null`, otherwise returns the receiver itself.
 * * 如果接收器为 `null` 则返回 `null`, 否则返回接收器自身.
 *
 * @since LDK 0.1.7-rc4
 */
fun <T> T?.nullable(): T? = this // used in invoke java

/**
 * * Throws a [NullPointerException] if the receiver is `null`.
 * * 如果接收器为 `null` 则抛出空指针异常.
 *
 * @param cause Cause
 * @param cause 原因
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun <T> T?.notNull(cause: String = "NPE"): T {
    if (this == null)
        throw NullPointerException(cause)
    return this
}

/**
 * * Throws a [NullPointerException] if the receiver string is `null` or [String.isBlank].
 * * 如果接收器字符串为 `null` 或为 [String.isBlank] 则抛出空指针异常.
 *
 * @param cause Cause
 * @param cause 原因
 * @since LDK 0.1.7-rc6
 */
@JvmOverloads
@Throws(NullPointerException::class)
fun String?.notNullAndBlank(cause: String = "String cannot be null or blank"): String {
    if (this == null || isBlank())
        throw NullPointerException(cause)
    return this
}

/**
 * * If the receiver is not `null`, then use `this` value as the receiver to call the specified [block] function.
 * * 如果接收器不为 `null`, 那么使用 `this` 值作为接收器调用指定的 [block] 函数.
 */
inline fun <T> T?.applyIfNotNull(block: Applicator<T>): T? {
    if (this != null)
        block(this)
    return this
}

/**
 * * If the receiver is not `null`, then the specified [block] function is called with the `this` value as the argument and the result is returned.
 * * 如果接收器不为 `null`, 那么使用 `this` 值作为参数调用指定的 [block] 函数并返回其结果.
 */
inline fun <T, R> T?.letIfNotNull(block: Function<T, R>): R? {
    if (this != null)
        return block(this)
    return null
}

/**
 * * Returns `true` if the receiver is `true`.
 * * 如果接收器是 `true`, 那么返回 `true`.
 */
fun Boolean?.isTrue(): Boolean
        = this != null && this

/**
 * * Returns `true` if the receiver is `null` or `false`.
 * * 如果接收器是 `null` 或 `false`, 那么返回 `true`.
 */
fun Boolean?.isFalse(): Boolean
        = this == null || !this

/**
 * * Returns `true` if the receiver is `null` or `true`.
 * * 如果接收器是 `null` 或 `true`, 那么返回 `true`.
 */
fun Boolean?.orTrue(): Boolean
        = this == null || this == true

/**
 * * Returns `false` if the receiver is `null` or `false`.
 * * 如果接收器是 `null` 或 `false`, 那么返回 `false`.
 */
fun Boolean?.orFalse(): Boolean
        = !(this == null || this == false)

/**
 * * Returns `true` If the receiver is after the given [other].
 * * 如果接收器是在给定的 [other] 之后, 那么返回 `true`.
 *
 * > * `compareTo(other) > 0`
 *
 * @see [Comparable]
 * @see [Comparable.compareTo]
 */
fun <T, C: Comparable<T>> C.isLater(other: T): Boolean
        = compareTo(other) > 0

/**
 * * Returns `true` if the receiver is equal or after the given [other].
 * * 如果接收器是在给定的 [other] 相等或之后, 那么返回 `true`.
 *
 * > * `compareTo(other) >= 0`
 *
 * @see [Comparable]
 * @see [Comparable.compareTo]
 */
fun <T, C: Comparable<T>> C.isOrLater(other: T): Boolean
        = compareTo(other) >= 0

/**
 * * Returns `true` if the receiver is between the given [min] and [max].
 * * 如果接收器是在给定的 [min] 和 [max] 之间, 那么返回 `true`.
 *
 * > * `compareTo(min) > 0 && compareTo(max) < 0`
 *
 * @see [Comparable]
 * @see [Comparable.compareTo]
 */
fun <T, C: Comparable<T>> C.isRange(min: T, max: T): Boolean
        = compareTo(min) > 0 && compareTo(max) < 0

/**
 * * Returns `true` if the receiver is equal or between the given [min] and [max].
 * * 如果接收器是在给定的 [min] 和 [max] 相等或之间, 那么返回 `true`.
 *
 * > * `compareTo(min) >= 0 && compareTo(max) <= 0`
 *
 * @see [Comparable]
 * @see [Comparable.compareTo]
 */
fun <T, C: Comparable<T>> C.isOrRange(min: T, max: T): Boolean
        = compareTo(min) >= 0 && compareTo(max) <= 0

/**************************************************************************
 *
 * Delegates
 *
 **************************************************************************/

/**
 * * Lazy loading class delegate.
 * * 延迟加载类委托.
 */
fun <T> lazyClass(initializer: Callable<Class<T>>)
        = LazyClass(initializer)

/**
 * * Lazy loading of any class delegate.
 * * 延迟加载任意类委托.
 */
fun lazyAnyClass(initializer: Callable<Class<*>>)
        = LazyAnyClass(initializer)

/**
 * * Lazy loading any or `null` class delegates.
 * * 延迟加载任意或 `null` 类委托.
 */
fun lazyAnyOrNullClass(initializer: Callable<Class<*>?>)
        = LazyAnyOrNullClass(initializer)
