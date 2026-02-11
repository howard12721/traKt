package jp.xhw.trakt.bot.model

import kotlin.time.Instant

enum class SortDirection {
    ASC,
    DESC,
}

data class MessageHistoryQuery(
    val limit: Int? = null,
    val offset: Int = 0,
    val since: Instant? = null,
    val until: Instant? = null,
    val inclusive: Boolean = false,
    val order: SortDirection = SortDirection.DESC,
)

enum class MessageSearchSort {
    CREATED_AT_DESC,
    CREATED_AT_ASC,
    UPDATED_AT_DESC,
    UPDATED_AT_ASC,
}

data class MessageSearchQuery(
    val word: String? = null,
    val after: Instant? = null,
    val before: Instant? = null,
    val inChannel: Channel? = null,
    val toUsers: List<User> = emptyList(),
    val fromUsers: List<User> = emptyList(),
    val citation: Message? = null,
    val fromBot: Boolean? = null,
    val hasUrl: Boolean? = null,
    val hasAttachments: Boolean? = null,
    val hasImage: Boolean? = null,
    val hasVideo: Boolean? = null,
    val hasAudio: Boolean? = null,
    val limit: Int? = null,
    val offset: Int? = null,
    val sort: MessageSearchSort = MessageSearchSort.CREATED_AT_DESC,
)
