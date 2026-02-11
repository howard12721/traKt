# traKt

Kotlin で [traQ](https://github.com/traptitech/traQ) Bot を書くための API ラッパーです。

## Quick Start

```kotlin
import jp.xhw.trakt.bot.event.MessageCreated
import jp.xhw.trakt.bot.model.MessageHistoryQuery
import jp.xhw.trakt.bot.model.MessageSearchQuery
import jp.xhw.trakt.bot.runTrakt

suspend fun main() {
    val token = System.getenv("TRAQ_BOT_TOKEN")
    require(!token.isNullOrBlank()) { "TRAQ_BOT_TOKEN is required" }

    runTrakt(token = token) {
        on<MessageCreated> { event ->
            when (val content = event.message.content.trim()) {
                "ping" -> event.message.sendReply("pong")

                "!whoami" -> {
                    val me = event.message.author.getSnapshot() ?: return@on
                    event.message.sendReply("you are ${me.displayName} (@${me.name})")
                }

                "!dm-me" -> {
                    event.message.author.sendDirectMessage("hello from bot")
                }

                "!edit-me" -> {
                    val sent = event.message.sendReply("draft")
                    sent.update(content = "final")
                }

                "!recent" -> {
                    val recent = event.message.channel.getMessages(MessageHistoryQuery(limit = 5))
                    event.message.sendReply("recent messages: ${recent.size}")
                }

                else -> {
                    if (content.startsWith("!find ")) {
                        val result =
                            searchMessages(
                                MessageSearchQuery(
                                    word = content.removePrefix("!find ").trim(),
                                    inChannel = event.message.channel,
                                    limit = 3,
                                ),
                            )
                        event.message.sendReply("hits=${result.totalHits}")
                    }
                }
            }
        }
    }
}
```

## Model Design

- Handle: `Channel`, `Message`, `User`
- Snapshot: `ChannelSnapshot`, `MessageSnapshot`, `UserSnapshot`

更新系操作は `Message` / `MessageSnapshot` どちらからでも呼べます。
`update` は必ず `content` 指定が必要です。

## API Spec

OpenAPI spec は再現性のため `trakt-bot/spec/v3-api.yaml` を使用します。
通常のビルドで毎回ダウンロードはしません。

spec を更新したい場合のみ、以下を実行してください。

```bash
bash ./gradlew :trakt-bot:refreshApiSpec
```
