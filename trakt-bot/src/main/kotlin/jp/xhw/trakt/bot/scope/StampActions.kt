package jp.xhw.trakt.bot.scope

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampHandle
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType

context(scope: BotScope)
suspend fun fetchStamp(stampId: StampId): Stamp.Detail = scope.context.stampPort.fetchStamp(stampId)

context(scope: BotScope)
suspend fun StampHandle.resolve(): Stamp.Detail = scope.context.stampPort.fetchStamp(id)

context(scope: BotScope)
suspend fun fetchStamps(type: StampType? = null): List<Stamp.Detail> = scope.context.stampPort.fetchStamps(type)

context(scope: BotScope)
suspend fun Stamp.resolve(): Stamp.Detail = this as? Stamp.Detail ?: handle.resolve()

context(scope: BotScope)
suspend fun Stamp.Detail.refresh(): Stamp.Detail = handle.resolve()
