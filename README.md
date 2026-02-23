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
    implementation("com.github.howard12721.traKt:trakt-bot:1.1.0")
}
```

## Bot を動かしてみる

```kotlin
import jp.xhw.trakt.bot.model.MessageCreated
import jp.xhw.trakt.bot.scope.reply
import jp.xhw.trakt.bot.trakt

suspend fun main() {
    val token = System.getenv("TRAQ_BOT_TOKEN")
    require(!token.isNullOrBlank()) { "TRAQ_BOT_TOKEN is required" }
    val botId = System.getenv("TRAQ_BOT_ID")
    require(!botId.isNullOrBlank()) { "TRAQ_BOT_ID is required" }

    val client = trakt(token = token, botId = Uuid.parse(botId)) {
        on<MessageCreated> { event ->
            if (event.message.content.trim() == "ping") {
                event.message.reply("pong")
            }
        }
    }

    try {
        client.start()
    } finally {
        client.stop()
    }
}
```

## 起動の流れ

```
trakt(token) { ... }   ← TraktClient 生成 + イベントハンドラ登録
        ↓
    client.start()      ← WebSocket 接続 & イベントループ開始
        ↓
    (イベント受信)       ← ハンドラが BotScope 内で実行される
        ↓
    client.stop()        ← 接続切断 & リソース解放
```

## コンフィグ

`trakt()` ファクトリ関数のパラメータ：

| パラメータ              | 型                  | デフォルト値          | 説明                     |
|--------------------|--------------------| --------------------- |------------------------|
| `token`            | `String`           | (必須)                | Bot アクセストークン           |
| `botId`            | `Uuid`             | (必須)                | Bot ID                 |
| `origin`           | `String`           | `"q.trap.jp"`         | traQ サーバーのホスト名         |
| `coroutineContext` | `CoroutineContext` | `Dispatchers.Default` | イベント処理に使用するコルーチンコンテキスト |

## 環境変数で管理する例

```bash
export TRAQ_BOT_TOKEN="your-bot-token-here"
```

```kotlin
val token = System.getenv("TRAQ_BOT_TOKEN")
    ?: error("TRAQ_BOT_TOKEN environment variable is required")
```
