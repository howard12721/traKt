package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType

/**
 * スタンプ詳細を取得します。
 *
 * @param stampId 取得対象スタンプID
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchStamp(stampId: StampId): Stamp.Detail = ctx.stampPort.fetchStamp(stampId)

/**
 * この ID が指すスタンプ詳細を取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun StampId.fetch(): Stamp.Detail = ctx.stampPort.fetchStamp(this)

/**
 * スタンプ一覧を取得します。
 *
 * @param type 取得対象のスタンプ種別。`null` の場合は全種別
 * @return スタンプ詳細一覧
 */
context(ctx: BaseContext)
suspend fun fetchStamps(type: StampType? = null): List<Stamp.Detail> = ctx.stampPort.fetchStamps(type)

/**
 * スタンプ詳細を取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun Stamp.fetch(): Stamp.Detail = id.fetch()
