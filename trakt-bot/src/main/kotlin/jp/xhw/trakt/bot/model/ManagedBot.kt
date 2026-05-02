package jp.xhw.trakt.bot.model

import kotlin.time.Instant

/** Bot 動作モード。 */
enum class ManagedBotMode {
    HTTP,
    WEBSOCKET,
}

/** Bot 状態。 */
enum class ManagedBotState {
    DEACTIVATED,
    ACTIVE,
    SUSPENDED,
}

/** Bot トークン。 */
@ConsistentCopyVisibility
data class BotTokens internal constructor(
    val verificationToken: String,
    val accessToken: String,
)

/** Bot イベントログ配送結果。 */
enum class BotEventResult {
    OK,
    NG,
    NETWORK_ERROR,
    DROPPED,
}

/** Bot イベントログ。 */
@ConsistentCopyVisibility
data class BotEventLog internal constructor(
    val botId: BotId,
    val requestId: String,
    val event: String,
    val result: BotEventResult?,
    val code: Int,
    val datetime: Instant,
) {
    val bot: BotHandle
        get() = BotHandle(botId)
}

/** Bot を参照するためのハンドル。 */
@JvmInline
value class BotHandle internal constructor(
    val id: BotId,
)

/** 管理 API で扱う Bot 情報。 */
sealed interface ManagedBot {
    val id: BotId
    val botUserId: UserId
    val description: String
    val developerId: UserId
    val subscribeEvents: List<String>
    val mode: ManagedBotMode
    val state: ManagedBotState
    val createdAt: Instant
    val updatedAt: Instant

    val handle: BotHandle
        get() = BotHandle(id)

    val botUser: UserHandle
        get() = UserHandle(botUserId)

    val developer: UserHandle
        get() = UserHandle(developerId)

    class Basic internal constructor(
        override val id: BotId,
        override val botUserId: UserId,
        override val description: String,
        override val developerId: UserId,
        override val subscribeEvents: List<String>,
        override val mode: ManagedBotMode,
        override val state: ManagedBotState,
        override val createdAt: Instant,
        override val updatedAt: Instant,
    ) : ManagedBot {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ManagedBot) return false
            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    class Detail internal constructor(
        override val id: BotId,
        override val botUserId: UserId,
        override val description: String,
        override val developerId: UserId,
        override val subscribeEvents: List<String>,
        override val mode: ManagedBotMode,
        override val state: ManagedBotState,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        val tokens: BotTokens,
        val endpoint: String,
        val privileged: Boolean,
        val channelIds: List<ChannelId>,
    ) : ManagedBot {
        val channels: List<ChannelHandle>
            get() = channelIds.map { ChannelHandle(it) }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ManagedBot) return false
            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}

/**
 * [BotId] から管理対象 Bot ハンドルを作成します。
 *
 * @param id Bot ID
 * @return Bot ハンドル
 */
fun managedBot(id: BotId) = BotHandle(id)
