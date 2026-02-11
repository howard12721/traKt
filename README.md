# traKt

Kotlinによる[traQ](https://github.com/traptitech/traQ)ボット開発のためのtraQ APIラッパー

注意: 作りかけです

## API specの取り扱い

OpenAPI specは再現性のため `trakt-bot/spec/v3-api.yaml` を使用します。
通常のビルドでネットワークから毎回ダウンロードはしません。

specを更新したい場合だけ、以下を手動で実行してください。

```bash
bash ./gradlew :trakt-bot:refreshApiSpec
```
