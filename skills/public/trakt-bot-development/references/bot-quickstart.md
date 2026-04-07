# Bot Quickstart (Consumer App)

## 1. Dependency

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.howard12721.traKt:trakt-bot:1.1.1")
}
```

## 2. Configuration input (example: environment variables)

The library does not require environment variables specifically.  
It always requires `token`, and accepts `botId` when you need bot-specific actions such as channel join/leave.

Common env-var convention:

- `TRAQ_BOT_TOKEN`: traQ bot access token
- `TRAQ_BOT_ID`: bot UUID string

## 3. Minimal runnable bot

```kotlin
import jp.xhw.trakt.bot.model.MessageCreated
import jp.xhw.trakt.bot.scope.reply
import jp.xhw.trakt.bot.trakt
import kotlin.uuid.Uuid

suspend fun main() {
    val token = System.getenv("TRAQ_BOT_TOKEN")
    require(!token.isNullOrBlank()) { "TRAQ_BOT_TOKEN is required" }

    val botId = System.getenv("TRAQ_BOT_ID")
        ?.takeUnless(String::isBlank)
        ?.let(Uuid::parse)

    val client = trakt(token = token, botId = botId) {
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

## 4. Action variants

### Post to the same channel without reply URL

```kotlin
import jp.xhw.trakt.bot.scope.sendMessage

on<MessageCreated> { event ->
    event.message.channel.sendMessage("accepted")
}
```

### Send DM to the message author

```kotlin
import jp.xhw.trakt.bot.scope.sendDirectMessage

on<MessageCreated> { event ->
    event.message.author.sendDirectMessage("I got your message")
}
```

### Add a stamp reaction

```kotlin
import jp.xhw.trakt.bot.model.StampId
import jp.xhw.trakt.bot.scope.stamp
import kotlin.uuid.Uuid

on<MessageCreated> { event ->
    val stampId = StampId(Uuid.parse("00000000-0000-0000-0000-000000000000"))
    event.message.stamp(stampId)
}
```

## 5. Startup and shutdown checks

- Register every `on<...>` handler before `start()`.
- Wrap `start()` with `try/finally` and always call `stop()`.
- Import actions from `jp.xhw.trakt.bot.scope.*`.
- Use `sendMessage(...)` instead of `reply(...)` when URL-appended replies are not wanted.
