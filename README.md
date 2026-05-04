# traKt

Kotlin で [traQ](https://github.com/traptitech/traQ) Bot を書くための API ラッパーです。

# クイックスタート

## 前提

- **JDK 21** 以上
- **Gradle**

## 依存関係の追加

Gradle プロジェクトに以下の依存関係を追加してください。

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.howard12721.traKt:trakt-bot:3.0.0")
}
```

## Bot を動かしてみる

```kotlin
import jp.xhw.trakt.bot.model.BotMessageCreated
import jp.xhw.trakt.bot.context.base.reply
import jp.xhw.trakt.bot.trakt
import kotlin.uuid.Uuid

suspend fun main() {
    val token = System.getenv("TRAQ_BOT_TOKEN")
    require(!token.isNullOrBlank()) { "TRAQ_BOT_TOKEN is required" }

    val botId = System.getenv("TRAQ_BOT_ID")?.takeUnless(String::isBlank)?.let(Uuid::parse)

    val client = trakt(token = token, botId = botId) {
        on<BotMessageCreated> { event ->
            if (event.message.content.trim() == "ping") {
                event.message.reply("pong")
            }
        }
    }

    client.run()
}
```
