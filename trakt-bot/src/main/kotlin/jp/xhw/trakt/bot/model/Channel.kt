package jp.xhw.trakt.bot.model

import kotlin.uuid.Uuid

data class ChannelId(
    val value: Uuid,
)

data class Channel(
    val id: ChannelId,
)
