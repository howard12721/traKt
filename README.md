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
    implementation("jp.xhw:trakt-bot:5.1.0")
}
```

## Bot を動かしてみる

```kotlin
import jp.xhw.trakt.bot.onMessageCreated

suspend fun main() {
    val client = trakt(token = "replace-with-your-token") {
        onMessageCreated { event ->
            if (event.message.content == "ping") {
                event.message.reply("pong")
            }
        }
    }

    client.run()
}
```
