package jp.xhw.trakt.bot.port

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType

internal interface StampPort {
    /**
     * スタンプ詳細を取得します。
     *
     * @param stampId 取得対象スタンプ
     * @return スタンプ詳細
     */
    suspend fun fetchStamp(stampId: StampId): Stamp.Detail

    /**
     * スタンプ一覧を取得します。
     *
     * @param type 取得対象スタンプ種別
     * @return スタンプ詳細一覧
     */
    suspend fun fetchStamps(type: StampType? = null): List<Stamp.Detail>
}
