# traKt

Kotlin で [traQ](https://github.com/traptitech/traQ) Bot を書くための API ラッパーです。

# クイックスタート

## 前提

- **JDK 21** 以上
- **Gradle**

## 依存関係の追加

Gradle プロジェクトに以下の依存関係を追加してください。

```kotlin
dependencies {
    implementation("jp.xhw:trakt-bot:6.0.0")
}
```

## Bot を動かしてみる

```kotlin
import jp.xhw.trakt.bot.context.base.reply
import jp.xhw.trakt.bot.onMessageCreated
import jp.xhw.trakt.bot.traktBot

suspend fun main() {
    val bot = traktBot(
        token = requireNotNull(System.getenv("TRAQ_BOT_TOKEN")),
    ) {
        onMessageCreated { event ->
            if (event.message.content == "ping") {
                event.message.reply("pong")
            }
        }
    }

    bot.run()
}
```

## 単発処理

イベントを購読せずに API を呼ぶ場合は `trakt(...)` を使います。クライアントが所有するリソースを確実に解放するため、
通常は `use` で囲んでください。複数回の単発処理で同じクライアントを再利用する場合は、最後に `close()` を呼びます。

```kotlin
import jp.xhw.trakt.bot.context.base.fetchChannelByPath
import jp.xhw.trakt.bot.context.base.sendMessage
import jp.xhw.trakt.bot.model.ChannelPath
import jp.xhw.trakt.bot.trakt

suspend fun main() {
    trakt(
        token = requireNotNull(System.getenv("TRAQ_BOT_TOKEN")),
    ).use { client ->
        client.execute {
            val channel = fetchChannelByPath(ChannelPath("general")) ?: return@execute
            channel.sendMessage("hello")
        }
    }
}
```
