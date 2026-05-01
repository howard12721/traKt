package jp.xhw.trakt.bot.scope.bot

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampHandle
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType

/**
 * スタンプ詳細を取得します。
 *
 * @param stampId 取得対象スタンプID
 * @return スタンプ詳細情報
 */
context(ctx: BotContext)
suspend fun fetchStamp(stampId: StampId): Stamp.Detail = ctx.stampPort.fetchStamp(stampId)

/**
 * このハンドルが指すスタンプ詳細を取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BotContext)
suspend fun StampHandle.resolve(): Stamp.Detail = ctx.stampPort.fetchStamp(id)

/**
 * スタンプ一覧を取得します。
 *
 * @param type 取得対象のスタンプ種別。`null` の場合は全種別
 * @return スタンプ詳細一覧
 */
context(ctx: BotContext)
suspend fun fetchStamps(type: StampType? = null): List<Stamp.Detail> = ctx.stampPort.fetchStamps(type)

/**
 * スタンプを詳細型として解決します。
 *
 * 既に [Detail] の場合はそのまま返し、そうでなければ API から再取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BotContext)
suspend fun Stamp.resolve(): Stamp.Detail = this as? Stamp.Detail ?: handle.resolve()

/**
 * スタンプ詳細を再取得します。
 *
 * @return 最新のスタンプ詳細情報
 */
context(ctx: BotContext)
suspend fun Stamp.Detail.refresh(): Stamp.Detail = handle.resolve()
