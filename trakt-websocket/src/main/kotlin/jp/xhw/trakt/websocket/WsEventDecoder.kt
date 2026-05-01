package jp.xhw.trakt.websocket

fun interface WsEventDecoder<out E : Any> {
    fun decode(frameData: String): E?
}
