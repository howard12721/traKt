package jp.xhw.trakt.bot.infrastructure.gateway

import jp.xhw.trakt.bot.infrastructure.gateway.mapper.toModel
import jp.xhw.trakt.bot.model.Stamp
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.model.StampType
import jp.xhw.trakt.bot.port.StampPort
import jp.xhw.trakt.rest.apis.StampApi

internal class TraqStampPort(
    private val apiGateway: TraqApiGateway,
) : StampPort {
    override suspend fun fetchStamp(stampId: StampId): Stamp.Detail {
        val response = apiGateway.stampApi.getStamp(stampId.value)
        return response.bodyOrThrow(operation = "fetchStamp(stampId=${stampId.value})").toModel()
    }

    override suspend fun fetchStamps(type: StampType?): List<Stamp.Detail> {
        val response = apiGateway.stampApi.getStamps(type = type?.toApiModel())
        return response.bodyOrThrow(operation = "fetchStamps(type=$type)").map { it.toModel() }
    }
}

private fun StampType.toApiModel(): StampApi.TypeGetStamps =
    when (this) {
        StampType.UNICODE -> StampApi.TypeGetStamps.UNICODE
        StampType.ORIGINAL -> StampApi.TypeGetStamps.ORIGINAL
    }
