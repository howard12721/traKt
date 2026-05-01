package jp.xhw.trakt.bot.context.base

import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.Stamp.Detail
import jp.xhw.trakt.bot.model.StampHandle
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType
import kotlin.uuid.Uuid

/**
 * スタンプ詳細を取得します。
 *
 * @param stampId 取得対象スタンプID
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchStamp(stampId: StampId): Detail = ctx.stampPort.fetchStamp(stampId)

/**
 * スタンプ詳細を取得します。
 *
 * @param stampId 取得対象スタンプID(UUID)
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchStamp(stampId: Uuid): Detail = fetchStamp(StampId(stampId))

/**
 * スタンプ詳細を取得します。
 *
 * @param stampId 取得対象スタンプID(UUID文字列)
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun fetchStamp(stampId: String): Detail = fetchStamp(Uuid.parse(stampId))

/**
 * このハンドルが指すスタンプ詳細を取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun StampHandle.resolve(): Detail = ctx.stampPort.fetchStamp(id)

/**
 * スタンプ一覧を取得します。
 *
 * @param type 取得対象のスタンプ種別。`null` の場合は全種別
 * @return スタンプ詳細一覧
 */
context(ctx: BaseContext)
suspend fun fetchStamps(type: StampType? = null): List<Detail> = ctx.stampPort.fetchStamps(type)

/**
 * スタンプを詳細型として解決します。
 *
 * 既に [Detail] の場合はそのまま返し、そうでなければ API から再取得します。
 *
 * @return スタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun Stamp.resolve(): Detail = this as? Detail ?: handle.resolve()

/**
 * スタンプ詳細を再取得します。
 *
 * @return 最新のスタンプ詳細情報
 */
context(ctx: BaseContext)
suspend fun Detail.refresh(): Detail = handle.resolve()
