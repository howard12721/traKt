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
 * スタンプ詳細を取得します。存在しない場合は `null` を返します。
 *
 * ユーザー入力など、存在が保証できない ID を扱う場合に使います。
 *
 * @param stampId 取得対象スタンプID
 * @return スタンプ詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun fetchStampOrNull(stampId: StampId): Stamp.Detail? = ctx.stampPort.fetchStampOrNull(stampId)

/**
 * この ID が指すスタンプ詳細を取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun StampId.fetch(): Stamp.Detail = ctx.stampPort.fetchStamp(this)

/**
 * この ID が指すスタンプ詳細を取得します。存在しない場合は `null` を返します。
 *
 * ユーザー入力など、存在が保証できない ID を扱う場合に使います。
 *
 * @return スタンプ詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun StampId.fetchOrNull(): Stamp.Detail? = ctx.stampPort.fetchStampOrNull(this)

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

/**
 * スタンプ詳細を取得します。存在しない場合は `null` を返します。
 *
 * @return スタンプ詳細情報。存在しない場合は `null`
 */
context(ctx: BaseContext)
suspend fun Stamp.fetchOrNull(): Stamp.Detail? = id.fetchOrNull()
