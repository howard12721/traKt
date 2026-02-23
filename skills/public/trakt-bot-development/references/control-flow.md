# Control Flow Reference

## Lifecycle phases

1. Construct client: `trakt(...)` returns a configured `TraktClient`.
2. Register handlers: call `on<EventType> { ... }` one or more times.
3. Start runtime: call `start()` once to begin WebSocket subscriptions.
4. Handle events: each matching handler runs in `BotScope`.
5. Stop runtime: call `stop()` to cancel subscriptions and close resources.

## Mandatory ordering rules

1. create client
2. register all handlers
3. call `start()`
4. eventually call `stop()`

Runtime checks:

- Registering handlers after `start()` throws `Handlers must be registered before start()`.
- Calling `start()` while already running throws `Client is already started`.

## Event dispatch model

- Each `on<EventType>` registration installs an independent flow subscription.
- Multiple handlers for the same event type all run (no short-circuit).
- Handler exceptions are caught and logged by the runtime; processing continues for later events.
- `CancellationException` is rethrown to preserve coroutine cancellation semantics.

## Action execution context

- Handler receivers are `BotScope`.
- All action APIs in `scope/*Actions.kt` are `context(scope: BotScope)` functions.
- `execute { ... }` runs a one-shot block in current `BotScope`.
- `launchAndExecute { ... }` starts a one-shot block asynchronously and returns `Job`.

## `reply()` behavior

`Message.reply(content)` appends the source message URL to message text.
Use `channel.sendMessage(...)` to send plain content without URL append behavior.

## Safe runtime pattern

```kotlin
val client = trakt(token = token, botId = botId) {
    on<MessageCreated> { event ->
        event.message.reply("pong")
    }
}

try {
    client.start()
} finally {
    client.stop()
}
```
