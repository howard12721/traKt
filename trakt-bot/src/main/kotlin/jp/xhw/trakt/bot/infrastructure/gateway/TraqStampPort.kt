package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toStatsModel
import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampStats
import jp.xhw.trakt.bot.model.StampType
import jp.xhw.trakt.bot.port.StampPort
import jp.xhw.trakt.rest.apis.StampApi

internal class TraqStampPort(
    private val stampApi: StampApi,
) : StampPort {
    constructor(apiGateway: TraqApiGateway) : this(apiGateway.stampApi)

    override suspend fun fetchStamp(stampId: StampId): Stamp.Detail {
        val response = stampApi.getStamp(stampId.value)
        return response.bodyOrThrow(operation = "fetchStamp(stampId=${stampId.value})").toModel()
    }

    override suspend fun fetchStampOrNull(stampId: StampId): Stamp.Detail? {
        val response = stampApi.getStamp(stampId.value)
        return response.bodyOrNullIfNotFound(operation = "fetchStampOrNull(stampId=${stampId.value})")?.toModel()
    }

    override suspend fun fetchStamps(type: StampType?): List<Stamp.Detail> {
        val response =
            stampApi.getStamps(
                includeUnicode = if (type == null) true else null,
                type = type?.toApiModel(),
            )
        return response.bodyOrThrow(operation = "fetchStamps(type=$type)").map { it.toModel() }
    }

    override suspend fun fetchStampStats(stampId: StampId): StampStats {
        val response = stampApi.getStampStats(stampId.value)
        return response.bodyOrThrow(operation = "fetchStampStats(stampId=${stampId.value})").toStatsModel()
    }
}

private fun StampType.toApiModel(): StampApi.TypeGetStamps =
    when (this) {
        StampType.UNICODE -> StampApi.TypeGetStamps.UNICODE
        StampType.ORIGINAL -> StampApi.TypeGetStamps.ORIGINAL
    }
