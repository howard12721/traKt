plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "trakt"

include("trakt-websocket")
include("trakt-rest")
include("trakt-bot")