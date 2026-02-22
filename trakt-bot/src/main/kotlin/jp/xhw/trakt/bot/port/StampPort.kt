package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType

internal interface StampPort {
    suspend fun fetchStamp(stampId: StampId): Stamp.Detail

    suspend fun fetchStamps(type: StampType? = null): List<Stamp.Detail>
}
