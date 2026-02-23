---
name: trakt-bot-development
description: Help users build traQ bots as consumers of the trakt-bot library. Use when generating runnable bot code, selecting event handlers, mapping events to actions, explaining control flow, or debugging handler behavior in application code.
---

# traKt Bot Development

## Objective

Implement runnable Kotlin bots that consume the published `trakt-bot` library.

## Reading order

1. Read [references/bot-quickstart.md](references/bot-quickstart.md) for dependency setup, configuration input examples, and minimal startup.
2. Read [references/event-action-playbook.md](references/event-action-playbook.md) to choose the right `on<Event>` handlers and action patterns.
3. Read [references/action-reference.md](references/action-reference.md) to confirm exact action names and overloads.
4. Read [references/model-reference.md](references/model-reference.md) for event payload fields, handle/entity distinctions, and model semantics.
5. Read [references/control-flow.md](references/control-flow.md) for lifecycle ordering, execution model, and shutdown behavior.

## Delivery checklist

When implementing or reviewing bot code, always provide:

1. `repositories` + `dependencies` snippet with `trakt-bot`
2. configuration input method for credentials/IDs (for example env vars, config file, or secret manager)
3. runnable Kotlin code with explicit imports
4. event-to-action reasoning with concrete API names
5. explicit action names used (must be present in `action-reference.md`)
6. lifecycle and failure checks (`start`/`stop`, handler registration timing)

## Rules

- Treat user code as an external consumer project, not this repository.
- Prefer copy-pasteable Kotlin snippets over pseudocode.
- Mention lifecycle ordering explicitly: register `on<...>` before `start()`.
- Mention `reply()` behavior explicitly: source message URL is appended to the text body.
- Use `channel.sendMessage(...)` when URL-appended replies are not desired.
- Use `references/action-reference.md` as the source of truth for available actions.
- Surface concrete runtime errors where relevant: `Handlers must be registered before start()` and `Client is already started`.
