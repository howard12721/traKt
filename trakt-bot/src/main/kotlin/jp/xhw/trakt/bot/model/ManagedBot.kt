package jp.xhw.trakt.bot.model

import kotlin.jvm.JvmInline
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
    val bot: ManagedBot.Ref,
    val requestId: String,
    val event: String,
    val result: BotEventResult?,
    val code: Int,
    val datetime: Instant,
)

/** 管理 API で扱う Bot 情報。 */
sealed interface ManagedBot {
    val id: BotId

    /** ID のみを持つ Bot 参照。 */
    @JvmInline
    value class Ref(
        override val id: BotId,
    ) : ManagedBot

    sealed interface WithMeta : ManagedBot {
        val botUser: User.Ref
        val description: String
        val developer: User.Ref
        val subscribeEvents: List<String>
        val mode: ManagedBotMode
        val state: ManagedBotState
        val createdAt: Instant
        val updatedAt: Instant
    }

    class Basic internal constructor(
        override val id: BotId,
        override val botUser: User.Ref,
        override val description: String,
        override val developer: User.Ref,
        override val subscribeEvents: List<String>,
        override val mode: ManagedBotMode,
        override val state: ManagedBotState,
        override val createdAt: Instant,
        override val updatedAt: Instant,
    ) : WithMeta {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ManagedBot) return false
            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }

    class Detail internal constructor(
        override val id: BotId,
        override val botUser: User.Ref,
        override val description: String,
        override val developer: User.Ref,
        override val subscribeEvents: List<String>,
        override val mode: ManagedBotMode,
        override val state: ManagedBotState,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        val tokens: BotTokens,
        val endpoint: String,
        val privileged: Boolean,
        val channels: List<Channel.Ref>,
    ) : WithMeta {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ManagedBot) return false
            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}
