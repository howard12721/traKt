plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.openapi.generator) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.vanniktech.maven.publish) apply false
}

val projectVersion = providers.gradleProperty("releaseVersion").orElse("1.0.0-SNAPSHOT").get()

allprojects {
    group = "jp.xhw"
    version = projectVersion
}
