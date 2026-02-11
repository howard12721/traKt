package jp.xhw.trakt.bot.model

import kotlin.uuid.Uuid

data class UserId(
    val value: Uuid,
)

data class User(
    val id: UserId,
)

data class UserTagId(
    val value: Uuid,
)
