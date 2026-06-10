package jp.xhw.trakt.websocket.user

import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class WebRtcSession(
    val state: String,
    val sessionId: String,
)

@Serializable
data class ViewState(
    val key: String,
    val channelId: Uuid,
    val state: String,
)

@Serializable
data class ChannelViewer(
    val userId: Uuid,
    val state: String,
    val updatedAt: Instant,
)

@Serializable
data class QallParticipant(
    val attributes: Map<String, String>? = null,
    val canPublish: Boolean,
    val identity: String,
    val joinedAt: Instant,
    val name: String,
)

@Serializable
data class QallRoomWithParticipants(
    val isWebinar: Boolean,
    val metadata: String? = null,
    val participants: List<QallParticipant>,
    val roomId: Uuid,
)
