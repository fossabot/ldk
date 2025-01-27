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

package com.lgou2w.ldk.bukkit.chat

import com.google.gson.Gson
import com.lgou2w.ldk.bukkit.item.ItemFactory
import com.lgou2w.ldk.bukkit.packet.PacketFactory
import com.lgou2w.ldk.bukkit.reflect.MinecraftReflection
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClass
import com.lgou2w.ldk.bukkit.reflect.lazyMinecraftClassOrNull
import com.lgou2w.ldk.bukkit.version.MinecraftBukkitVersion
import com.lgou2w.ldk.chat.ChatAction
import com.lgou2w.ldk.chat.ChatComponent
import com.lgou2w.ldk.chat.ChatComponentFancy
import com.lgou2w.ldk.chat.ChatComponentTranslation
import com.lgou2w.ldk.chat.ChatSerializer
import com.lgou2w.ldk.common.Enums
import com.lgou2w.ldk.reflect.AccessorConstructor
import com.lgou2w.ldk.reflect.AccessorField
import com.lgou2w.ldk.reflect.FuzzyReflect
import com.lgou2w.ldk.reflect.Visibility
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * ## ChatFactory (聊天工厂)
 *
 * @author lgou2w
 */
object ChatFactory {

    @JvmStatic val CLASS_ICHAT_BASE_COMPONENT by lazyMinecraftClass("IChatBaseComponent")
    @JvmStatic val CLASS_CHAT_SERIALIZER by lazyMinecraftClass("IChatBaseComponent\$ChatSerializer", "ChatSerializer")

    @JvmStatic private val CLASS_PACKET_OUT_CHAT by lazyMinecraftClass("PacketPlayOutChat")
    @JvmStatic private val CLASS_CHAT_MESSAGE_TYPE by lazyMinecraftClassOrNull("ChatMessageType")

    // NMS.PacketPlayOutChat -> public constructor(NMS.IChatBaseComponent, ChatMessageType | Byte)
    @JvmStatic private val CONSTRUCTOR_PACKET_OUT_CHAT : AccessorConstructor<Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_CHAT, true)
            .useConstructorMatcher()
            .withParams(CLASS_ICHAT_BASE_COMPONENT, CLASS_CHAT_MESSAGE_TYPE ?: Byte::class.java)
            .resultAccessor()
    }

    // NMS.ChatSerializer -> public final static Gson GSON
    @JvmStatic val FIELD_CHAT_SERIALIZER_GSON : AccessorField<Any, Gson> by lazy {
        FuzzyReflect.of(CLASS_CHAT_SERIALIZER, true)
            .useFieldMatcher()
            .withVisibilities(Visibility.STATIC)
            .withType(Gson::class.java)
            .resultAccessorAs<Any, Gson>()
    }

    /**
     * * Convert the given chat [component] to an implementation of `NMS`.
     * * 将给定的聊天组件 [component] 转换为 `NMS` 的实现.
     */
    @JvmStatic
    fun toNMS(component: ChatComponent): Any {
        val gson = FIELD_CHAT_SERIALIZER_GSON[null]!!
        val json = component.toJson()
        return gson.fromJson<Any>(json, CLASS_ICHAT_BASE_COMPONENT)
    }

    /**
     * * Convert the given `NMS` chat object to an implementation of the [ChatComponent] wrapper.
     * * 将给定的 `NMS` 聊天对象转换为 [ChatComponent] 包装的实现.
     *
     * @throws [IllegalArgumentException] If the chat component object [icbc] is not the expected `NMS` instance.
     * @throws [IllegalArgumentException] 如果聊天组件对象 [icbc] 不是预期的 `NMS` 实例.
     */
    @JvmStatic
    fun fromNMS(icbc: Any): ChatComponent {
        MinecraftReflection.isExpected(icbc, CLASS_ICHAT_BASE_COMPONENT)
        val gson = FIELD_CHAT_SERIALIZER_GSON[null]!!
        val json = gson.toJson(icbc, CLASS_ICHAT_BASE_COMPONENT)
        return ChatSerializer.fromJsonOrLenient(json)
    }

    /**
     * * Create a chat packet with the given chat [component] with the given [action].
     * * 将给定的聊天组件 [component] 以给定的交互 [action] 创建聊天数据包.
     *
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun createChatPacket(component: ChatComponent, action: ChatAction = ChatAction.CHAT): Any {
        val value : Any? =
                if (CLASS_CHAT_MESSAGE_TYPE != null) Enums.fromOrdinal(CLASS_CHAT_MESSAGE_TYPE!!, action.ordinal)
                else action.id
        val icbc = toNMS(component)
        return CONSTRUCTOR_PACKET_OUT_CHAT.newInstance(icbc, value)
    }

    /**
     * * Sends the given chat [component] to the player with the given [action].
     * * 将给定的聊天组件 [component] 以给定的交互 [action] 发送给玩家.
     */
    @JvmStatic
    @JvmOverloads
    fun sendChat(player: Player, component: ChatComponent, action: ChatAction = ChatAction.CHAT)
            = sendChatTo(arrayOf(player), component, action)

    /**
     * * Sends the given chat [component] to the player with the given [action].
     * * 将给定的聊天组件 [component] 以给定的交互 [action] 发送给玩家.
     *
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendChatTo(players: Array<Player>, component: ChatComponent, action: ChatAction = ChatAction.CHAT) {
        val packet = createChatPacket(component, action)
        PacketFactory.sendPacketTo(packet, *players)
    }

    /**
     * * Send the given chat [component] to all online players with the given [action].
     * * 将给定的聊天组件 [component] 以给定的交互 [action] 发送给所有在线玩家.
     *
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendChatToAll(component: ChatComponent, action: ChatAction = ChatAction.CHAT)
            = sendChatTo(Bukkit.getOnlinePlayers().toTypedArray(), component, action)

    /**
     * * Display the given item stack [itemStack] to the fancy chat component.
     * * 将给定的物品栈 [itemStack] 显示到花式聊天组件上.
     *
     * @see [ChatComponentFancy]
     * @see [ChatComponentFancy.tooltipItem]
     */
    @JvmStatic
    fun tooltipItem(fancy: ChatComponentFancy, itemStack: ItemStack): ChatComponentFancy {
        val mojangson = ItemFactory.readItem(itemStack).toMojangson()
        return fancy.tooltipItem(mojangson)
    }

    //<editor-fold desc="Title - Temporary implements" defaultstate="collapsed">

    @JvmStatic private val CLASS_PACKET_OUT_TITLE by lazyMinecraftClass("PacketPlayOutTitle")
    @JvmStatic private val CLASS_ENUM_TITLE_ACTION by lazyMinecraftClass("PacketPlayOutTitle\$EnumTitleAction", "EnumTitleAction")

    // NMS.PacketPlayOutTitle -> public constructor(NMS.EnumTitleAction, NMS.IChatComponent, Int, Int, Int)
    @JvmStatic private val CONSTRUCTOR_PACKET_OUT_TITLE : AccessorConstructor<Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_TITLE, true)
            .useConstructorMatcher()
            .withParams(CLASS_ENUM_TITLE_ACTION, CLASS_ICHAT_BASE_COMPONENT, Int::class.java, Int::class.java, Int::class.java)
            .resultAccessor()
    }

    private const val PACKET_TITLE_ACTION_TITLE = "TITLE"
    private const val PACKET_TITLE_ACTION_SUBTITLE = "SUBTITLE"
    private const val PACKET_TITLE_ACTION_ACTIONBAR = "ACTIONBAR" // since Minecraft 1.11
    private const val PACKET_TITLE_ACTION_TIMES = "TIMES"
    private const val PACKET_TITLE_ACTION_RESET = "RESET"
    private const val PACKET_TITLE_ACTION_CLEAR = "CLEAR"

    /**
     * * TITLE | SUBTITLE | ACTIONBAR -> Action, ChatComponent
     * * TIMES -> Action, Int, Int, Int
     * * RESET | CLEAR -> Action
     */
    @JvmStatic
    private fun createTitlePacket(
            action: String,
            value: ChatComponent?,
            fadeIn: Int,
            stay: Int,
            fadeOut: Int
    ): Any {
        val enumAction = Enums.fromName(CLASS_ENUM_TITLE_ACTION, action)
        return when {
            value != null -> CONSTRUCTOR_PACKET_OUT_TITLE.newInstance(enumAction, toNMS(value), -1, -1, -1)
            action == PACKET_TITLE_ACTION_TIMES -> CONSTRUCTOR_PACKET_OUT_TITLE.newInstance(enumAction, null, fadeIn, stay, fadeOut)
            else -> CONSTRUCTOR_PACKET_OUT_TITLE.newInstance(enumAction, null, -1, -1, -1)
        }
    }

    @JvmStatic
    private fun sendTitle(
            player: Player,
            action: String,
            value: ChatComponent?,
            fadeIn: Int = 10,
            stay: Int = 70,
            fadeOut: Int = 20
    ) {
        val packet = createTitlePacket(action, value, fadeIn, stay, fadeOut)
        PacketFactory.sendPacket(player, packet)
    }

    @JvmStatic
    private fun sendTitleTo(
            players: Array<Player>,
            action: String,
            value: ChatComponent?,
            fadeIn: Int = 10,
            stay: Int = 70,
            fadeOut: Int = 20
    ) {
        val packet = createTitlePacket(action, value, fadeIn, stay, fadeOut)
        PacketFactory.sendPacketTo(packet, *players)
    }

    @JvmStatic
    private fun sendTitleToAll(
            action: String,
            value: ChatComponent?,
            fadeIn: Int = 10,
            stay: Int = 70,
            fadeOut: Int = 20
    ) = sendTitleTo(Bukkit.getOnlinePlayers().toTypedArray(), action, value, fadeIn, stay, fadeOut)

    /**
     * * Send a title chat message to the given player [player].
     * * 将给定的玩家 [player] 发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitle(player: Player, title: ChatComponent, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
            = sendTitle(player, title, null, fadeIn, stay, fadeOut)

    /**
     * * Send a title chat message to the given player [players].
     * * 将给定的玩家 [players] 发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitleTo(players: Array<Player>, title: ChatComponent, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
            = sendTitleTo(players, title, null, fadeIn, stay, fadeOut)

    /**
     * * Send a title chat message to all online players.
     * * 将在线的所有玩家发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitleToAll(title: ChatComponent, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
            = sendTitleTo(Bukkit.getOnlinePlayers().toTypedArray(), title, null, fadeIn, stay, fadeOut)

    /**
     * * Send a title chat message to the given player [player].
     * * 将给定的玩家 [player] 发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [subTitle] Sub title
     * @param [subTitle] 子标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitle(player: Player, title: ChatComponent, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20) {
        sendTitleTimes(player, fadeIn, stay, fadeOut)
        sendTitle(player, PACKET_TITLE_ACTION_TITLE, title)
        if (subTitle != null)
            sendTitle(player, PACKET_TITLE_ACTION_SUBTITLE, subTitle)
    }

    /**
     * * Send a title chat message to the given player [players].
     * * 将给定的玩家 [players] 发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [subTitle] Sub title
     * @param [subTitle] 子标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitleTo(players: Array<Player>, title: ChatComponent, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20) {
        sendTitleTimesTo(players, fadeIn, stay, fadeOut)
        sendTitleTo(players, PACKET_TITLE_ACTION_TITLE, title)
        if (subTitle != null)
            sendTitleTo(players, PACKET_TITLE_ACTION_SUBTITLE, subTitle)
    }

    /**
     * * Send a title chat message to all online players.
     * * 将在线的所有玩家发送标题聊天消息.
     *
     * @param [title] Title
     * @param [title] 标题
     * @param [subTitle] Sub title
     * @param [subTitle] 子标题
     * @param [fadeIn] Fade in time tick
     * @param [fadeIn] 淡入时间刻
     * @param [stay] Stay time tick
     * @param [stay] 停留时间刻
     * @param [fadeOut] Fade out time tick
     * @param [fadeOut] 淡出时间刻
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    @JvmOverloads
    fun sendTitleToAll(title: ChatComponent, subTitle: ChatComponent?, fadeIn: Int = 10, stay: Int = 70, fadeOut: Int = 20)
            = sendTitleTo(Bukkit.getOnlinePlayers().toTypedArray(), title, subTitle, fadeIn, stay, fadeOut)

    // ACTIONBAR => Since Minecraft 1.11
    @JvmStatic
    fun sendTitleBar(player: Player, title: ChatComponent) {
        if (MinecraftBukkitVersion.isV111OrLater) {
            sendTitle(player, PACKET_TITLE_ACTION_ACTIONBAR, title)
        } else {
            sendTitle(player, title)
        }
    }

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleBarTo(players: Array<Player>, title: ChatComponent) {
        if (MinecraftBukkitVersion.isV111OrLater) {
            sendTitleTo(players, PACKET_TITLE_ACTION_ACTIONBAR, title)
        } else {
            sendTitleTo(players, title)
        }
    }

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleBarToAll(title: ChatComponent)
            = sendTitleBarTo(Bukkit.getOnlinePlayers().toTypedArray(), title)

    @JvmStatic
    fun sendTitleTimes(player: Player, fadeIn: Int, stay: Int, fadeOut: Int)
            = sendTitle(player, PACKET_TITLE_ACTION_TIMES, null, fadeIn, stay, fadeOut)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleTimesTo(players: Array<Player>, fadeIn: Int, stay: Int, fadeOut: Int)
            = sendTitleTo(players, PACKET_TITLE_ACTION_TIMES, null, fadeIn, stay, fadeOut)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleTimesToAll(fadeIn: Int, stay: Int, fadeOut: Int)
            = sendTitleTo(Bukkit.getOnlinePlayers().toTypedArray(), PACKET_TITLE_ACTION_TIMES, null, fadeIn, stay, fadeOut)

    @JvmStatic
    fun sendTitleReset(player: Player)
            = sendTitle(player, PACKET_TITLE_ACTION_RESET, null)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleResetTo(players: Array<Player>)
            = sendTitleTo(players, PACKET_TITLE_ACTION_RESET, null)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleResetToAll()
            = sendTitleResetTo(Bukkit.getOnlinePlayers().toTypedArray())

    @JvmStatic
    fun sendTitleClear(player: Player)
            = sendTitle(player, PACKET_TITLE_ACTION_CLEAR, null)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleClearTo(players: Array<Player>)
            = sendTitleTo(players, PACKET_TITLE_ACTION_CLEAR, null)

    /**
     * @since LDK 0.1.7-rc3
     */
    @JvmStatic
    fun sendTitleClearToAll()
            = sendTitleClearTo(Bukkit.getOnlinePlayers().toTypedArray())

    //</editor-fold>

    //<editor-fold desc="PlayerListHeaderFooter - Temporary implements" defaultstate="collapsed">

    @JvmStatic private val CLASS_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER by lazyMinecraftClass("PacketPlayOutPlayerListHeaderFooter")

    // NMS.PacketPlayOutPlayerListHeaderFooter -> public constructor()
    @JvmStatic private val CONSTRUCTOR_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER : AccessorConstructor<Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER, true)
            .useConstructorMatcher()
            .withParamsCount(0) // No parameter constructor
            .resultAccessor()
    }

    // NMS.PacketPlayOutPlayerListHeaderFooter -> private or public NMS.IChatBaseComponent header
    @JvmStatic private val FIELD_PACKET_OUT_PLAYER_LIST_HEADER : AccessorField<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER, true)
            .useFieldMatcher()
            .withType(CLASS_ICHAT_BASE_COMPONENT)
            .resultAccessor()
    }

    // NMS.PacketPlayOutPlayerListHeaderFooter -> private or public NMS.IChatBaseComponent footer
    @JvmStatic private val FIELD_PACKET_OUT_PLAYER_LIST_FOOTER : AccessorField<Any, Any> by lazy {
        FuzzyReflect.of(CLASS_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER, true)
            .useFieldMatcher()
            .withType(CLASS_ICHAT_BASE_COMPONENT)
            .resultAccessors()
            .last()
    }

    @JvmStatic
    private fun createPlayerListHeaderFooterPacket(
            header: ChatComponent?,
            footer: ChatComponent?
    ): Any {
        val packet = CONSTRUCTOR_PACKET_OUT_PLAYER_LIST_HEADER_FOOTER.newInstance()
        val header0 = toNMS(header ?: ChatComponentTranslation("")) // empty translate to remove
        val footer0 = toNMS(footer ?: ChatComponentTranslation("")) // empty translate to remove
        FIELD_PACKET_OUT_PLAYER_LIST_HEADER[packet] = header0
        FIELD_PACKET_OUT_PLAYER_LIST_FOOTER[packet] = footer0
        return packet
    }

    /**
     * * Send the player list [header] and [footer] message to the given [player].
     * * 将给定的玩家 [player] 发送玩家列表页眉 [header] 和页脚 [footer] 消息.
     *
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    fun sendPlayerListHeaderFooter(
            player: Player,
            header: ChatComponent?,
            footer: ChatComponent? = null
    ) {
        val packet = createPlayerListHeaderFooterPacket(header, footer)
        PacketFactory.sendPacketTo(packet, player)
    }

    /**
     * * Send the player list [header] and [footer] message to the given [players].
     * * 将给定的玩家 [players] 发送玩家列表页眉 [header] 和页脚 [footer] 消息.
     *
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    fun sendPlayerListHeaderFooterTo(
            players: Array<Player>,
            header: ChatComponent?,
            footer: ChatComponent? = null
    ) {
        val packet = createPlayerListHeaderFooterPacket(header, footer)
        PacketFactory.sendPacketTo(packet, *players)
    }

    /**
     * * Sends a list [header] and [footer] message to all online players.
     * * 将在线的所有玩家发送玩家列表页眉 [header] 和页脚 [footer] 消息.
     *
     * @since LDK 0.1.8-rc
     */
    @JvmStatic
    @JvmOverloads
    fun sendPlayerListHeaderFooterToAll(
            header: ChatComponent?,
            footer: ChatComponent? = null
    ) {
        val packet = createPlayerListHeaderFooterPacket(header, footer)
        PacketFactory.sendPacketTo(packet, *Bukkit.getOnlinePlayers().toTypedArray())
    }

    //</editor-fold>
}
