package jp.xhw.trakt.bot.infrastructure.runtime

/** Runtime が所有する外部リソースの開始・停止処理。 */
internal interface RuntimeLifecycle {
    /** イベントソースや通信リソースを開始します。 */
    suspend fun start()

    /** [start] が呼ばれた後、イベントソースが利用可能になるまで待機します。 */
    suspend fun awaitStarted() = Unit

    /** [start] で開始したリソースを停止・解放します。 */
    suspend fun stop() = Unit

    companion object {
        val Noop =
            object : RuntimeLifecycle {
                override suspend fun start() = Unit
            }
    }
}
